import type { ISftpTransferManager, SftpTransferItem } from '../types/terminal.type';
import { TransferStatus, TransferType } from '../types/terminal.const';
import { sleep } from '@/utils';
import { Message } from '@arco-design/web-vue';
import { getTerminalAccessToken } from '@/api/asset/host-terminal';

export const BLOCK_SIZE = 1024 * 1024;

export const wsBase = import.meta.env.VITE_WS_BASE_URL;

// todo 考虑一下单文件上传失败 (网络/文件被删除)

// sftp 传输管理器实现
export default class SftpTransferManager implements ISftpTransferManager {

  private client?: WebSocket;

  private run: boolean;

  transferList: Array<SftpTransferItem>;

  constructor() {
    this.run = false;
    this.transferList = [];
  }

  // 添加传输
  addTransfer(items: Array<SftpTransferItem>): void {
    this.transferList.push(...items);
    // 开始传输
    if (!this.run) {
      this.startTransfer();
    }
  }

  // 打开会话
  private async openClient() {
    // 获取 access
    const { data: accessToken } = await getTerminalAccessToken();
    // 打开会话
    this.client = new WebSocket(`${wsBase}/host/transfer/${accessToken}`);
    this.client.onerror = event => {
      // 打开失败将传输列表置为失效
      Message.error('会话打开失败');
      console.error('error', event);
      this.transferList.forEach(s => {
        s.status = TransferStatus.ERROR;
      });
    };
    this.client.onclose = event => {
      // 关闭会话重置 run
      this.run = false;
      console.warn('close', event);
    };
    this.client.onmessage = this.resolveMessage.bind(this);
    // 等待会话连接
    for (let i = 0; i < 100; i++) {
      await sleep(50);
      if (this.client.readyState !== WebSocket.CONNECTING) {
        break;
      }
    }
  }

  // 开始传输
  private async startTransfer() {
    this.run = true;
    // 打开会话
    await this.openClient();
    if (!this.run) {
      return;
    }
    // 开始传输
    while (true) {
      const item = this.transferList.find(s => s.status === TransferStatus.WAITING);
      if (!item) {
        break;
      }
      // 开始传输
      try {
        item.status = TransferStatus.TRANSFERRING;
        if (item.type === TransferType.UPLOAD) {
          // 上传
          await this.uploadFile(item);
        } else {
          // 下载
          await this.uploadDownload(item);
        }
        item.status = TransferStatus.SUCCESS;
      } catch (e) {
        item.status = TransferStatus.ERROR;
      }
    }
  }

  // 接收消息
  private async resolveMessage(message: MessageEvent) {
    // TODO
    console.log();
    const data = message.data;
    if (data === 'flush') {

    } else if (data === 'error') {

    } else if (data === 'close') {
      // TODO 关闭会话
      this.client?.close();
    }
  }

  // 上传文件
  private async uploadFile(item: SftpTransferItem) {
    const file = item.file;
    // TODO 发送开始
    // 计算分片数量
    const totalBlock = Math.ceil(file.size / BLOCK_SIZE);
    // 分片上传
    for (let i = 0; i < totalBlock; i++) {
      // TODO wait ACK
      // 读取数据
      const start = i * BLOCK_SIZE;
      const end = Math.min(file.size, start + BLOCK_SIZE);
      const chunk = file.slice(start, end);
      const reader = new FileReader();
      const arrayBuffer = await new Promise((resolve, reject) => {
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
        reader.readAsArrayBuffer(chunk);
      });
      // 上传 TODO
      console.log(arrayBuffer);
      this.client?.send(arrayBuffer as ArrayBuffer);
    }
    // TODO 发送 END
  }

  // 下载文件
  private async uploadDownload(item: SftpTransferItem) {
    // TODO
  }

}
