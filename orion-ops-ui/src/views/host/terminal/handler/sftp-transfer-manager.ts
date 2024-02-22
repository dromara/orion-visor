import type { ISftpTransferManager, ISftpTransferUploader, SftpTransferItem } from '../types/terminal.type';
import { TransferOperatorResponse } from '../types/terminal.type';
import { TransferOperatorType, TransferStatus, TransferType } from '../types/terminal.const';
import { Message } from '@arco-design/web-vue';
import { getTerminalAccessToken } from '@/api/asset/host-terminal';
import SftpTransferUploader from '@/views/host/terminal/handler/sftp-transfer-uploader';

export const wsBase = import.meta.env.VITE_WS_BASE_URL;

// todo 考虑一下单文件上传失败 (网络/文件被删除)
// todo 取消任务

// sftp 传输管理器实现
export default class SftpTransferManager implements ISftpTransferManager {

  private client?: WebSocket;

  private run: boolean;

  private currentItem?: SftpTransferItem;

  private currentUploader?: ISftpTransferUploader;

  public transferList: Array<SftpTransferItem>;

  constructor() {
    this.run = false;
    this.transferList = [];
  }

  // 添加传输
  addTransfer(items: Array<SftpTransferItem>): void {
    this.transferList.push(...items);
    // 开始传输
    if (!this.run) {
      this.openClient();
    }
  }

  // 打开会话
  private async openClient() {
    this.run = true;
    // 获取 access
    const { data: accessToken } = await getTerminalAccessToken();
    // 打开会话
    this.client = new WebSocket(`${wsBase}/host/transfer/${accessToken}`);
    this.client.onerror = event => {
      // 打开失败将传输列表置为失效
      Message.error('会话打开失败');
      console.error('error', event);
      // 将等待中和传输中任务修改为失败状态
      this.transferList.filter(s => {
        return s.status === TransferStatus.WAITING
          || s.status === TransferStatus.TRANSFERRING;
      }).forEach(s => {
        s.status = TransferStatus.ERROR;
      });
    };
    this.client.onclose = event => {
      // 关闭会话重置 run
      this.run = false;
      console.warn('close', event);
    };
    this.client.onopen = () => {
      // 打开后自动传输下一个任务
      this.transferNextItem();
    };
    this.client.onmessage = this.resolveMessage.bind(this);
  }

  // 传输下一条任务
  private transferNextItem() {
    this.currentUploader = undefined;
    // 获取任务
    this.currentItem = this.transferList.find(s => s.status === TransferStatus.WAITING);
    if (this.currentItem) {
      // 开始传输
      if (this.currentItem.type === TransferType.UPLOAD) {
        // 上传
        this.uploadFile();
      } else {
        // 下载
        this.uploadDownload();
      }
    } else {
      // 无任务关闭会话
      this.client?.close();
    }
  }

  // 接收消息
  private async resolveMessage(message: MessageEvent) {
    const data = JSON.parse(message.data) as TransferOperatorResponse;
    if (data.type === TransferOperatorType.PROCESSED) {
      // 接收处理完成
      this.resolveProcessed(data);
    }
  }

  // 上传文件
  private uploadFile() {
    // 创建上传器
    this.currentUploader = new SftpTransferUploader(this.currentItem as SftpTransferItem, this.client as WebSocket);
    // 开始上传
    this.currentUploader.startUpload();
  }

  // 下载文件
  private uploadDownload() {
    // TODO
  }

  // 接收处理完成回调
  private resolveProcessed(data: TransferOperatorResponse) {
    // 操作回调
    if (data.success) {
      // 操作成功
      if (this.currentUploader) {
        if (this.currentUploader.hasNextBlock()) {
          // 有下一个分片则上传 (上一个分片传输完成)
          this.currentUploader.uploadNextBlock();
        } else {
          // 没有下一个分片则检查是否完成
          if (this.currentUploader.finish) {
            // 已完成 开始下一个传输任务 (发送 finish 后的回调)
            this.transferNextItem();
          } else {
            // 未完成则发送完成 (最后一个分片传输完成但还未发送 finish 指令)
            this.currentUploader.uploadFinish();
          }
        }
      }
    } else {
      // 操作失败
      if (this.currentUploader) {
        // 上传失败
        this.currentUploader.uploadError(data.msg);
      }
      // 开始下一个传输任务
      this.transferNextItem();
    }
  }

}
