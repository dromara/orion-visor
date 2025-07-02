import type {
  FileTransferTaskType,
  ISetTransferClient,
  ISftpTransferManager,
  ITerminalSession,
  MaybeFileTransferTask,
  SftpFile,
  TransferOperatorResponse
} from '@/views/terminal/interfaces';
import { TerminalMessages, TransferReceiver, TransferStatus, TransferType } from '../../types/const';
import { Message } from '@arco-design/web-vue';
import { getTerminalTransferToken, openTerminalTransferChannel } from '@/api/terminal/terminal';
import BaseTransferManager from './base-transfer-manager';
import SftpFileUploadTask from './sftp-file-upload-task';
import SftpFileDownloadTask from './sftp-file-download-task';

// sftp 传输管理器实现
export default class SftpTransferManager extends BaseTransferManager implements ISftpTransferManager {

  private client?: WebSocket;

  private run: boolean;

  private currentTask?: FileTransferTaskType;

  constructor() {
    super();
    this.run = false;
  }

  // 添加上传任务
  async addUpload(session: ITerminalSession, parentPath: string, files: Array<File>) {
    Message.info(TerminalMessages.fileUploading);
    // 创建任务
    for (let file of files) {
      const task = new SftpFileUploadTask(TransferType.UPLOAD, session, {
        name: file.webkitRelativePath || file.name,
        parentPath: parentPath,
        size: file.size,
        file,
      });
      this.tasks.push(task);
    }
    // 开始传输
    await this.startTransfer();
  }

  // 添加下载任务
  async addDownload(session: ITerminalSession, currentPath: string, files: Array<SftpFile>) {
    Message.info(TerminalMessages.fileDownloading);
    let pathIndex = currentPath === '/' ? 1 : currentPath.length + 1;
    for (let file of files) {
      // 创建任务
      const task = new SftpFileDownloadTask(TransferType.DOWNLOAD, session, {
        name: file.path.substring(pathIndex),
        parentPath: currentPath,
        size: file.size,
      });
      this.tasks.push(task);
    }
    // 开始传输
    await this.startTransfer();
  }

  // 开始传输
  private async startTransfer() {
    // 开始传输
    if (!this.run) {
      await this.openClient();
    }
    // 开始计算进度
    this.resetProgressTimer();
  }

  // 取消传输
  cancelTransfer(fileId: string): void {
    const index = this.tasks.findIndex(s => s.fileId === fileId);
    if (index === -1) {
      return;
    }
    const task = this.tasks[index];
    if (task.state.status === TransferStatus.TRANSFERRING) {
      // 传输中则中断传输
      this.currentTask?.abort();
    }
    // 从列表中移除
    this.tasks.splice(index, 1);
  }

  // 打开会话
  private async openClient() {
    this.run = true;
    // 获取 transferToken
    const { data: transferToken } = await getTerminalTransferToken();
    // 打开会话
    try {
      this.client = await openTerminalTransferChannel(transferToken);
    } catch (e) {
      // 打开失败将传输列表置为失效
      Message.error('会话打开失败');
      console.error('transfer error', e);
      // 将等待中和传输中任务修改为失败状态
      this.tasks.filter(s => {
        return s.state.status === TransferStatus.WAITING || s.state.status === TransferStatus.TRANSFERRING;
      }).forEach(s => {
        s.state.status = TransferStatus.ERROR;
      });
      // 关闭会话
      this.close();
      return;
    }
    // 关闭会话
    this.client.onclose = event => {
      console.warn('transfer close', event);
      this.close();
    };
    // 处理消息
    this.client.onmessage = this.resolveMessage.bind(this);
    // 打开后自动传输下一个任务
    this.transferNextItem();
  }

  // 传输下一条任务
  private transferNextItem() {
    // 获取任务
    this.currentTask = this.tasks.find(s => s.state.status === TransferStatus.WAITING);
    if (this.currentTask) {
      // 设置 client
      (this.currentTask as unknown as ISetTransferClient<WebSocket>).setClient(this.client as WebSocket);
      // 开始
      this.currentTask?.start();
    } else {
      // 无任务关闭会话
      this.client?.close();
    }
  }

  // 接收消息
  private async resolveMessage(message: MessageEvent) {
    // 文本消息
    const data = JSON.parse(message.data) as TransferOperatorResponse;
    if (data.type === TransferReceiver.NEXT_PART) {
      // 接收下一块数据回调
      await (this.currentTask as MaybeFileTransferTask)?.onNextPart?.();
    } else if (data.type === TransferReceiver.START) {
      // 开始下载回调
      (this.currentTask as MaybeFileTransferTask)?.onStart?.(data.channelId as string, data.transferToken as string);
    } else if (data.type === TransferReceiver.PROGRESS) {
      // 下载进度回调
      (this.currentTask as MaybeFileTransferTask)?.onProgress?.(data.totalSize, data.currentSize);
    } else if (data.type === TransferReceiver.FINISH) {
      // 完成回调
      this.currentTask?.onFinish();
      // 开始下一个传输任务
      this.transferNextItem();
    } else if (data.type === TransferReceiver.ERROR) {
      // 失败回调
      this.currentTask?.onError(data.msg);
      // 开始下一个传输任务
      this.transferNextItem();
    } else if (data.type === TransferReceiver.ABORT) {
      // 中断回调
      this.currentTask?.onAbort();
      // 开始下一个传输任务
      this.transferNextItem();
    }
  }

  // 关闭 释放资源
  protected close() {
    // 重置 run
    this.run = false;
    // 关闭
    super.close();
  }

}
