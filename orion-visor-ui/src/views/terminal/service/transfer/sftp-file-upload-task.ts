import type { FileTransferItem, IFileUploadTask, ITerminalSession } from '@/views/terminal/interfaces';
import { closeFileReader } from '@/utils/file';
import SftpBaseTransferTask from './sftp-base-transfer-task';

// 512 KB
export const PART_SIZE = 512 * 1024;

// sftp 上传任务实现
export default class SftpFileUploadTask extends SftpBaseTransferTask implements IFileUploadTask {

  private currentPart: number;
  private readonly totalPart: number;

  constructor(type: string, session: ITerminalSession, fileItem: FileTransferItem) {
    super(type, session, fileItem);
    this.currentPart = 0;
    this.totalPart = Math.ceil(fileItem.size / PART_SIZE);
  }

  // 上传完成
  finish() {
    super.finish();
    // 释放资源
    this.fileItem.file = undefined;
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
      this.client?.send(arrayBuffer as ArrayBuffer);
      this.currentPart++;
      this.state.currentSize += (end - start);
    } finally {
      // 释放资源
      closeFileReader(reader);
    }
  }

  // 是否有下一个分片
  private hasNextPart() {
    return this.currentPart < this.totalPart;
  }

}
