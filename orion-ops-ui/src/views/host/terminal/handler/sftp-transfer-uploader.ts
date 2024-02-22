import type { ISftpTransferUploader, SftpTransferItem } from '../types/terminal.type';
import { TransferOperatorType, TransferStatus } from '../types/terminal.const';
import { getPath } from '@/utils/file';

export const BLOCK_SIZE = 1024 * 1024;

// sftp 上传器实现
export default class SftpTransferUploader implements ISftpTransferUploader {

  public abort: boolean;
  public finish: boolean;
  private currentBlock: number;
  private totalBlock: number;
  private client: WebSocket;
  private item: SftpTransferItem;
  private file: File;

  constructor(item: SftpTransferItem, client: WebSocket) {
    this.abort = false;
    this.finish = false;
    this.item = item;
    this.client = client;
    this.file = item.file;
    this.currentBlock = 0;
    this.totalBlock = Math.ceil(item.file.size / BLOCK_SIZE);
  }

  // 开始上传
  startUpload() {
    this.item.status = TransferStatus.TRANSFERRING;
    // 发送开始上传信息
    this.client?.send(JSON.stringify({
      type: TransferOperatorType.UPLOAD_START,
      path: getPath(this.item.parentPath + '/' + this.item.name),
      hostId: this.item.hostId
    }));
  }

  // 是否有下一个分片
  hasNextBlock() {
    return this.currentBlock < this.totalBlock;
  }

  // 上传下一个分片
  async uploadNextBlock() {
    // 读取数据
    const start = this.currentBlock * BLOCK_SIZE;
    const end = Math.min(this.file.size, start + BLOCK_SIZE);
    const chunk = this.file.slice(start, end);
    const reader = new FileReader();
    const arrayBuffer = await new Promise((resolve, reject) => {
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
      reader.readAsArrayBuffer(chunk);
    });
    // 发送数据
    this.client?.send(arrayBuffer as ArrayBuffer);
    this.currentBlock++;
    this.item.currentSize += (end - start);
  }

  // 上传完成
  uploadFinish() {
    this.finish = true;
    this.item.status = TransferStatus.SUCCESS;
    // 发送上传完成的信息
    this.client?.send(JSON.stringify({
      type: TransferOperatorType.UPLOAD_FINISH,
      hostId: this.item.hostId
    }));
  }

  // 上传失败
  uploadError(msg: string | undefined) {
    this.finish = true;
    this.item.status = TransferStatus.ERROR;
    this.item.errorMessage = msg || '上传失败';
    // 发送上传完成的信息
    this.client?.send(JSON.stringify({
      type: TransferOperatorType.UPLOAD_ERROR,
      hostId: this.item.hostId
    }));
  }

  // 上传中断
  uploadAbort() {
    this.abort = true;
  }

}
