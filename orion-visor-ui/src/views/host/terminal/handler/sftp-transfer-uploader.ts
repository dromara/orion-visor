import type { SftpTransferItem } from '../types/terminal.type';
import { TransferType } from '../types/terminal.const';
import SftpTransferHandler from './sftp-transfer-handler';

// 512 KB
export const PART_SIZE = 512 * 1024;

// sftp 上传器实现
export default class SftpTransferUploader extends SftpTransferHandler {

  private currentPart: number;
  private readonly totalPart: number;
  private file: File;

  constructor(item: SftpTransferItem, client: WebSocket) {
    super(TransferType.UPLOAD, item, client);
    this.file = item.file;
    this.currentPart = 0;
    this.totalPart = Math.ceil(item.file.size / PART_SIZE);
  }

  // 是否有下一个分片
  hasNextPart() {
    return this.currentPart < this.totalPart;
  }

  // 上传下一个分片
  async onNextPart() {
    super.onNextPart();
    // 完成或者中断直接跳过
    if (this.aborted || this.finished) {
      return;
    }
    if (this.hasNextPart()) {
      try {
        // 有下一个分片则上传
        await this.doUploadNextPart();
      } catch (e) {
        // 读取文件失败
        this.error();
      }
    } else {
      this.finish();
    }
  }

  // 执行上传下一分片
  private async doUploadNextPart() {
    // 读取数据
    const start = this.currentPart * PART_SIZE;
    const end = Math.min(this.file.size, start + PART_SIZE);
    const chunk = this.file.slice(start, end);
    const reader = new FileReader();
    const arrayBuffer = await new Promise((resolve, reject) => {
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
      reader.readAsArrayBuffer(chunk);
    });
    // 发送数据
    this.client?.send(arrayBuffer as ArrayBuffer);
    this.currentPart++;
    this.item.currentSize += (end - start);
  }

}
