import type { IFileDownloadTask, FileTransferItem, IRdpSession } from '@/views/terminal/interfaces';
import { TransferType, TransferStatus, TerminalMessages, TransferSource } from '../../types/const';
import { saveAs } from 'file-saver';
import Guacamole from 'guacamole-common-js';
import BaseFileTransferTask from './base-file-transfer-task';

// rdp 下载任务实现
export default class RdpFileDownloadTask extends BaseFileTransferTask implements IFileDownloadTask {

  private session: IRdpSession;
  private stream: Guacamole.InputStream;
  private reader: Guacamole.BlobReader;

  constructor(session: IRdpSession, stream: Guacamole.InputStream, mimetype: string, fileItem: FileTransferItem) {
    super(TransferType.DOWNLOAD, TransferSource.RDP, session.info.hostId, session.sessionKey, fileItem, {});
    this.stream = stream;
    this.session = session;
    this.reader = new Guacamole.BlobReader(this.stream, mimetype);
  }

  // 开始下载
  start(): void {
    this.onStart();
  }

  // 下载完成
  finish(): void {
    this.onFinish();
  }

  // 下载中断
  abort(): void {
    this.onAbort();
  }

  // 下载失败
  error(): void {
    this.onError(undefined as unknown as string);
  }

  // 开始回调
  onStart(): void {
    try {
      this.state.status = TransferStatus.TRANSFERRING;
      // 发送开始
      this.stream.sendAck('OK', Guacamole.Status.Code.SUCCESS);
      // 进度回调
      this.reader.onprogress = (len) => this.onProgress(undefined, len);
      // 结束回调
      this.reader.onend = this.onFinish.bind(this);
    } catch (e) {
      this.onError(TerminalMessages.fileTransferError);
    }
  }

  // 进度发生变化
  onProgress(_: number | undefined, currentSize: number | undefined): void {
    if (this.state.aborted || this.state.finished) {
      return;
    }
    // 设置进度
    this.state.totalSize += currentSize || 0;
    this.state.currentSize += currentSize || 0;
    // 发送 ACK
    if (this.stream) {
      this.stream.sendAck('Received', Guacamole.Status.Code.SUCCESS);
    }
  }

  // 完成回调
  onFinish(): void {
    if (this.state.aborted || this.state.finished) {
      return;
    }
    this.state.finished = true;
    this.state.progress = 100;
    this.state.status = TransferStatus.SUCCESS;
    // 完成下载文件
    const blob = this.reader.getBlob();
    saveAs(blob, this.fileItem.name);
    // 释放资源
    this.releaseResource();
  }

  // 下载终端回调
  onAbort(): void {
    if (this.state.aborted || this.state.finished) {
      return;
    }
    this.state.aborted = true;
    try {
      if (this.session.state.connected) {
        if (this.stream) {
          // 发送 ACK
          this.stream.sendAck('Aborted', Guacamole.Status.Code.RESOURCE_CLOSED);
          // 关闭流
          this.session.client.endStream(this.stream.index);
        }
      }
      // 触发失败
      this.onError(TerminalMessages.sessionClosed);
    } catch (e) {
      // 触发失败
      this.onError(TerminalMessages.fileTransferError);
    }
  }

  // 下载失败回调
  onError(msg: string | undefined): void {
    this.state.finished = true;
    this.state.status = TransferStatus.ERROR;
    this.state.errorMessage = msg || TerminalMessages.fileTransferError;
    // 释放资源
    this.releaseResource();
  }

  // 释放资源
  private releaseResource() {
    if (this.stream) {
      this.stream.onend = null;
      this.stream.onblob = null;
    }
    this.stream = undefined as unknown as Guacamole.InputStream;
    this.reader = undefined as unknown as Guacamole.BlobReader;
  }

}
