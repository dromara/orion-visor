import type { FileTransferItem, IFileUploadTask, IRdpSession } from '@/views/terminal/interfaces';
import { TransferType, TransferSource, TerminalMessages, TransferStatus } from '@/views/terminal/types/const';
import { closeFileReader } from '@/utils/file';
import BaseFileTransferTask from './base-file-transfer-task';
import Guacamole from 'guacamole-common-js';

// 6048
export const PART_SIZE = Guacamole.ArrayBufferWriter.DEFAULT_BLOB_LENGTH;

// rdp 上传任务实现
export default class RdpFileUploadTask extends BaseFileTransferTask implements IFileUploadTask {

  private session: IRdpSession;
  private writer?: Guacamole.ArrayBufferWriter;
  private stream?: Guacamole.OutputStream;
  private currentPart: number;
  private readonly totalPart: number;

  constructor(session: IRdpSession, fileItem: FileTransferItem) {
    super(TransferType.UPLOAD, TransferSource.RDP, session.info.hostId, session.sessionKey, fileItem, {});
    this.session = session;
    this.currentPart = 0;
    this.totalPart = Math.ceil(fileItem.size / PART_SIZE);
  }

  // 开始上传
  start() {
    this.state.status = TransferStatus.TRANSFERRING;
    try {
      const file = this.fileItem.file as File;
      const client = this.session.client;
      // 创建文件流
      this.stream = client.createFileStream(file.type, file.name);
      this.writer = new Guacamole.ArrayBufferWriter(this.stream);
      // 分片上传完成回调
      this.writer.onack = ({ code, message }) => {
        // 成功继续上传分片
        if (code === Guacamole.Status.Code.SUCCESS) {
          this.onNextPart();
          return;
        }
        // 失败关闭流
        if (this.stream) {
          this.stream.sendEnd();
        }
        // 响应错误
        this.onError(message);
      };
      // 开始上传分片
      this.onNextPart();
    } catch (e) {
      this.onError(TerminalMessages.fileTransferError);
    }
  }

  // 上传中断
  abort() {
    this.onAbort();
  }

  // 上传完成
  finish() {
    this.onFinish();
  }

  // 上传失败
  error(): void {
    this.onError(undefined as unknown as string);
  }

  // 上传下一个分片
  async onNextPart() {
    // 完成或者中断直接跳过
    if (this.state.aborted || this.state.finished) {
      return;
    }
    if (this.hasNextPart()) {
      try {
        // 有下一个分片则上传
        await this.uploadNextPart();
      } catch (e) {
        // 读取文件失败
        this.error();
      }
    } else {
      this.finish();
    }
  }

  // 执行上传下一分片
  private async uploadNextPart() {
    // 读取数据
    const start = this.currentPart * PART_SIZE;
    const end = Math.min(this.fileItem.size, start + PART_SIZE);
    const chunk = (this.fileItem.file as File).slice(start, end);
    const reader = new FileReader();
    try {
      const arrayBuffer = await new Promise((resolve, reject) => {
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
        reader.readAsArrayBuffer(chunk);
      });
      // 发送数据
      this.writer?.sendData(arrayBuffer as ArrayBuffer);
      this.currentPart++;
      this.state.currentSize += (end - start);
    } finally {
      // 释放资源
      closeFileReader(reader);
    }
  }

  // 上传中断
  onAbort() {
    if (this.state.aborted || this.state.finished) {
      return;
    }
    this.state.aborted = true;
    try {
      if (this.session.state.connected) {
        // 关闭流
        if (this.stream) {
          this.stream.sendEnd();
        }
      }
      // 触发失败
      this.onError(TerminalMessages.sessionClosed);
    } catch (e) {
      // 触发失败
      this.onError(TerminalMessages.fileTransferError);
    }
  }

  // 上传完成
  onFinish() {
    if (this.state.aborted || this.state.finished) {
      return;
    }
    this.state.finished = true;
    this.state.progress = 100;
    this.state.status = TransferStatus.SUCCESS;
    // 关闭流
    if (this.stream) {
      this.stream.sendEnd();
    }
    // 释放资源
    this.releaseResource();
  }

  // 上传错误回调
  onError(msg: string | undefined) {
    this.state.finished = true;
    this.state.status = TransferStatus.ERROR;
    this.state.errorMessage = msg || TerminalMessages.fileTransferError;
    // 释放资源
    this.releaseResource();
  }

  // 是否有下一个分片
  private hasNextPart() {
    return this.currentPart < this.totalPart;
  }

  // 释放资源
  private releaseResource() {
    if (this.writer) {
      this.writer.onack = null;
    }
    this.writer = undefined;
    this.stream = undefined;
    this.fileItem.file = undefined;
  }

}
