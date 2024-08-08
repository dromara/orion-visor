import type { ISftpTransferHandler, ISftpTransferManager, SftpFile, SftpTransferItem, TransferOperatorResponse } from '../types/define';
import { sessionCloseMsg, TransferReceiver, TransferStatus, TransferType } from '../types/const';
import { Message } from '@arco-design/web-vue';
import { getTerminalTransferToken, openHostTransferChannel } from '@/api/asset/host-terminal';
import { nextId } from '@/utils';
import SftpTransferUploader from './sftp-transfer-uploader';
import SftpTransferDownloader from './sftp-transfer-downloader';

// sftp 传输管理器实现
export default class SftpTransferManager implements ISftpTransferManager {

  private client?: WebSocket;

  private run: boolean;

  private progressIntervalId?: any;

  private currentItem?: SftpTransferItem;

  private currentTransfer?: ISftpTransferHandler;

  public transferList: Array<SftpTransferItem>;

  constructor() {
    this.run = false;
    this.transferList = [];
  }

  // 添加上传任务
  addUpload(hostId: number, parentPath: string, files: Array<File>) {
    // 转为上传任务
    const items = files.map(s => {
      return {
        fileId: nextId(10),
        type: TransferType.UPLOAD,
        hostId: hostId,
        name: s.webkitRelativePath || s.name,
        currentSize: 0,
        totalSize: s.size,
        progress: 0,
        status: TransferStatus.WAITING,
        parentPath: parentPath,
        file: s
      };
    });
    // 开始传输
    this.startTransfer(items);
  }

  // 添加下载任务
  addDownload(hostId: number, currentPath: string, files: Array<SftpFile>) {
    let pathIndex = currentPath === '/' ? 1 : currentPath.length + 1;
    // 转为下载文件
    const items = files.map(s => {
      return {
        fileId: nextId(10),
        type: TransferType.DOWNLOAD,
        hostId: hostId,
        name: s.path.substring(pathIndex),
        parentPath: currentPath,
        currentSize: 0,
        totalSize: s.size,
        progress: 0,
        status: TransferStatus.WAITING,
      };
    }) as Array<SftpTransferItem>;
    // 开始传输
    this.startTransfer(items);
  }

  // 开始传输
  startTransfer(items: Array<SftpTransferItem>) {
    this.transferList.push(...items);
    // 开始传输
    if (!this.run) {
      this.openClient();
    }
  }

  // 取消传输
  cancelTransfer(fileId: string): void {
    const index = this.transferList.findIndex(s => s.fileId === fileId);
    if (index === -1) {
      return;
    }
    const item = this.transferList[index];
    if (item.status === TransferStatus.TRANSFERRING) {
      // 传输中则中断传输
      this.currentTransfer?.abort();
    }
    // 从列表中移除
    this.transferList.splice(index, 1);
  }

  // 取消全部传输
  cancelAllTransfer(): void {
    // 从列表中移除非传输中的元素
    this.transferList.reduceRight((_, value: SftpTransferItem, index: number) => {
      if (value.status !== TransferStatus.TRANSFERRING) {
        this.transferList.splice(index, 1);
      }
    }, null as any);
  }

  // 打开会话
  private async openClient() {
    this.run = true;
    // 获取 transferToken
    const { data: transferToken } = await getTerminalTransferToken();
    // 打开会话
    try {
      this.client = await openHostTransferChannel(transferToken);
    } catch (e) {
      // 打开失败将传输列表置为失效
      Message.error('会话打开失败');
      console.error('transfer error', e);
      // 将等待中和传输中任务修改为失败状态
      this.transferList.filter(s => {
        return s.status === TransferStatus.WAITING
          || s.status === TransferStatus.TRANSFERRING;
      }).forEach(s => {
        s.status = TransferStatus.ERROR;
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
    // 计算传输进度
    this.progressIntervalId = setInterval(this.calcProgress.bind(this), 500);
    // 打开后自动传输下一个任务
    this.transferNextItem();
  }

  // 计算传输进度
  private calcProgress() {
    this.transferList.forEach(item => {
      if (item.totalSize != 0) {
        item.progress = (item.currentSize / item.totalSize * 100).toFixed(2);
      }
    });
  }

  // 传输下一条任务
  private transferNextItem() {
    this.currentTransfer = undefined;
    // 释放内存
    if (this.currentItem) {
      this.currentItem.file = null as unknown as File;
    }
    // 获取任务
    this.currentItem = this.transferList.find(s => s.status === TransferStatus.WAITING);
    if (this.currentItem) {
      // 创建传输器
      this.currentTransfer = this.createTransfer();
      // 开始
      this.currentTransfer?.start();
    } else {
      // 无任务关闭会话
      this.client?.close();
    }
  }

  // 创建传输器
  private createTransfer(): ISftpTransferHandler | undefined {
    if (!this.currentItem) {
      return undefined;
    }
    if (this.currentItem.type === TransferType.UPLOAD) {
      // 上传
      return new SftpTransferUploader(TransferType.UPLOAD, this.currentItem, this.client as WebSocket);
    } else if (this.currentItem.type === TransferType.DOWNLOAD) {
      // 下载
      return new SftpTransferDownloader(TransferType.DOWNLOAD, this.currentItem, this.client as WebSocket);
    }
  }

  // 接收消息
  private async resolveMessage(message: MessageEvent) {
    // 文本消息
    const data = JSON.parse(message.data) as TransferOperatorResponse;
    if (data.type === TransferReceiver.NEXT_PART) {
      // 接收下一块数据回调
      await this.currentTransfer?.onNextPart();
    } else if (data.type === TransferReceiver.START) {
      // 开始回调
      this.currentTransfer?.onStart(data.channelId as string, data.transferToken as string);
    } else if (data.type === TransferReceiver.PROGRESS) {
      // 进度回调
      this.currentTransfer?.onProgress(data.totalSize, data.currentSize);
    } else if (data.type === TransferReceiver.FINISH) {
      // 完成回调
      this.currentTransfer?.onFinish();
      // 开始下一个传输任务
      this.transferNextItem();
    } else if (data.type === TransferReceiver.ERROR) {
      // 失败回调
      this.currentTransfer?.onError(data.msg);
      // 开始下一个传输任务
      this.transferNextItem();
    } else if (data.type === TransferReceiver.ABORT) {
      // 中断回调
      this.currentTransfer?.onAbort();
      // 开始下一个传输任务
      this.transferNextItem();
    }
  }

  // 关闭 释放资源
  private close() {
    // 重置 run
    this.run = false;
    // 关闭传输进度
    clearInterval(this.progressIntervalId);
    // 进行中和等待中的文件改为失败
    this.transferList.forEach(s => {
      if (s.status === TransferStatus.WAITING ||
        s.status === TransferStatus.TRANSFERRING) {
        s.status = TransferStatus.ERROR;
        s.errorMessage = sessionCloseMsg;
      }
    });
  }

}
