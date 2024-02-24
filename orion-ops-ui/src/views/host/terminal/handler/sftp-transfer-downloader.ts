import type { ISftpTransferDownloader, SftpTransferItem } from '../types/terminal.type';
import { TransferOperatorType, TransferStatus } from '../types/terminal.const';
import { getFileName, getPath } from '@/utils/file';
import { saveAs } from 'file-saver';

// sftp 上传器实现
export default class SftpTransferDownloader implements ISftpTransferDownloader {

  public abort: boolean;

  private blobArr: Array<Blob>;
  private client: WebSocket;
  private item: SftpTransferItem;

  constructor(item: SftpTransferItem, client: WebSocket) {
    this.abort = false;
    this.blobArr = [];
    this.item = item;
    this.client = client;
  }

  // 开始下载
  startDownload() {
    this.item.status = TransferStatus.TRANSFERRING;
    // 发送开始下载信息
    this.client?.send(JSON.stringify({
      type: TransferOperatorType.DOWNLOAD_START,
      path: getPath(this.item.parentPath + '/' + this.item.name),
      hostId: this.item.hostId
    }));
  }

  // 接收 blob
  resolveBlob(blob: Blob) {
    this.blobArr.push(blob);
    this.item.currentSize += blob.size;
  }

  // 下载完成
  downloadFinish() {
    if (this.abort) {
      // 中断则不触发下载
      return;
    }
    try {
      // 触发下载
      saveAs(new Blob(this.blobArr, {
        type: 'application/octet-stream'
      }), getFileName(this.item.name));
      this.item.status = TransferStatus.SUCCESS;
    } catch (e) {
      this.item.status = TransferStatus.ERROR;
      this.item.errorMessage = '保存失败';
    } finally {
      this.blobArr = [];
    }
  }

  // 下载失败
  downloadError(msg: string | undefined) {
    this.blobArr = [];
    this.item.status = TransferStatus.ERROR;
    this.item.errorMessage = msg || '下载失败';
  }

  // 下载中断
  downloadAbort() {
    this.abort = true;
    // 发送下载中断信息
    this.client?.send(JSON.stringify({
      type: TransferOperatorType.DOWNLOAD_ABORT,
      hostId: this.item.hostId
    }));
  }

}
