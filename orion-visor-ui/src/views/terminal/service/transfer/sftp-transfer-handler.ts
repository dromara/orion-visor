import type { ISftpTransferHandler, SftpTransferItem } from '@/views/terminal/interfaces';
import { TransferOperator, TransferStatus } from '../../types/const';
import { getPath } from '@/utils/file';

// sftp 传输处理器定义
export default abstract class SftpTransferHandler implements ISftpTransferHandler {

  public type: string;
  public finished: boolean;
  public aborted: boolean;
  protected client: WebSocket;
  protected item: SftpTransferItem;

  protected constructor(type: string, item: SftpTransferItem, client: WebSocket) {
    this.type = type;
    this.finished = false;
    this.aborted = false;
    this.item = item;
    this.client = client;
  }

  // 开始
  start() {
    this.item.status = TransferStatus.TRANSFERRING;
    // 发送开始信息
    this.client?.send(JSON.stringify({
      operator: TransferOperator.START,
      type: this.type,
      path: getPath(this.item.parentPath + '/' + this.item.name),
      hostId: this.item.hostId,
      paths: this.item.paths,
    }));
  };

  // 完成
  finish() {
    this.finished = true;
    this.item.status = TransferStatus.SUCCESS;
    // 发送完成的信息
    this.client?.send(JSON.stringify({
      operator: TransferOperator.FINISH,
      type: this.type,
      hostId: this.item.hostId
    }));
  };

  // 失败
  error() {
    this.finished = true;
    this.item.status = TransferStatus.ERROR;
    // 发送上传失败的信息
    this.client?.send(JSON.stringify({
      operator: TransferOperator.ERROR,
      type: this.type,
      hostId: this.item.hostId
    }));
  };

  // 中断
  abort() {
    this.aborted = true;
    // 发送中断的信息
    this.client?.send(JSON.stringify({
      operator: TransferOperator.ABORT,
      type: this.type,
      hostId: this.item.hostId
    }));
  }

  // 是否有下一个分片
  hasNextPart() {
    return false;
  };

  // 下一页分片回调
  onNextPart() {
    return undefined as unknown as any;
  };

  // 开始回调
  onStart(channelId: string, token: string) {
  };

  // 进度回调
  onProgress(totalSize: number | undefined, currentSize: number | undefined) {
    if (this.item) {
      if (totalSize) {
        this.item.totalSize = totalSize;
      }
      if (currentSize) {
        this.item.currentSize = currentSize;
      }
    }
  };

  // 失败回调
  onError(msg: string | undefined) {
    this.finished = true;
    this.item.status = TransferStatus.ERROR;
    this.item.errorMessage = msg || '传输失败';
  }

  // 完成回调
  onFinish() {
    this.finished = true;
    this.item.status = TransferStatus.SUCCESS;
    this.item.currentSize = this.item.totalSize;
  };

  // 中断回调
  onAbort() {
    this.aborted = true;
  };

}
