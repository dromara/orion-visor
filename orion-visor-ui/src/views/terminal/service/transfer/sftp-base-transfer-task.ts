import type { ISetTransferClient, FileTransferItem } from '@/views/terminal/interfaces';
import { TransferOperator, TransferStatus, TerminalMessages, TransferSource } from '../../types/const';
import { getPath } from '@/utils/file';
import BaseFileTransferTask from './base-file-transfer-task';

// sftp 传输任务一定义
export default abstract class SftpBaseTransferTask extends BaseFileTransferTask implements ISetTransferClient<WebSocket> {

  protected client?: WebSocket;

  protected constructor(type: string,
                        hostId: number,
                        fileItem: FileTransferItem) {
    super(type, TransferSource.SFTP, hostId, undefined as unknown as string, fileItem, {});
  }

  // 设置传输客户端
  setClient(client: WebSocket) {
    this.client = client;
  }

  // 开始
  start() {
    this.state.status = TransferStatus.TRANSFERRING;
    // 发送开始信息
    this.client?.send(JSON.stringify({
      operator: TransferOperator.START,
      type: this.type,
      hostId: this.hostId,
      path: getPath(this.fileItem.parentPath + '/' + this.fileItem.name),
      paths: this.fileItem.paths,
    }));
  };

  // 完成
  finish() {
    this.state.finished = true;
    this.state.progress = 100;
    this.state.status = TransferStatus.SUCCESS;
    // 发送完成的信息
    this.client?.send(JSON.stringify({
      operator: TransferOperator.FINISH,
      type: this.type,
      hostId: this.hostId,
    }));
  };

  // 失败
  error() {
    this.state.finished = true;
    this.state.status = TransferStatus.ERROR;
    // 发送上传失败的信息
    this.client?.send(JSON.stringify({
      operator: TransferOperator.ERROR,
      type: this.type,
      hostId: this.hostId,
    }));
  };

  // 中断
  abort() {
    this.state.aborted = true;
    // 发送中断的信息
    this.client?.send(JSON.stringify({
      operator: TransferOperator.ABORT,
      type: this.type,
      hostId: this.hostId,
    }));
  }

  // 失败回调
  onError(msg: string | undefined) {
    this.state.finished = true;
    this.state.status = TransferStatus.ERROR;
    this.state.errorMessage = msg || TerminalMessages.fileTransferError;
  }

  // 完成回调
  onFinish() {
    this.state.finished = true;
    this.state.progress = 100;
    this.state.status = TransferStatus.SUCCESS;
    this.state.currentSize = this.state.totalSize;
  };

  // 中断回调
  onAbort() {
    this.state.aborted = true;
  };

}
