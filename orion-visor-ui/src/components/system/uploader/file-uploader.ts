import type { FileItem } from '@arco-design/web-vue';
import type { IFileUploader, ResponseMessageBody } from './const';
import { UploadOperatorType, UploadReceiverType } from './const';
import { openFileUploadChannel } from '@/api/system/upload';
import { closeFileReader } from '@/utils/file';

// 512 KB
export const PART_SIZE = 512 * 1024;

// 文件上传器 实现
export default class FileUploader implements IFileUploader {

  private readonly token: string;

  private readonly fileList: Array<FileItem>;

  private currentIndex: number;

  private currentFileItem: FileItem;

  private currentFile: File;

  private currentFileSize: number;

  private currentPart: number;

  private totalPart: number;

  private client?: WebSocket;

  private hook?: Function;

  constructor(token: string, fileList: Array<FileItem>) {
    this.token = token;
    this.fileList = fileList;
    this.currentIndex = 0;
    this.currentFileItem = undefined as unknown as FileItem;
    this.currentFile = undefined as unknown as File;
    this.currentFileSize = 0;
    this.currentPart = 0;
    this.totalPart = 0;
  }

  // 开始
  async start(): Promise<void> {
    try {
      // 打开管道
      this.client = await openFileUploadChannel(this.token);
      this.client.onclose = () => {
        this.hook && this.hook();
      };
    } catch (e) {
      // 修改状态
      this.fileList.forEach(s => s.status = 'error');
      throw e;
    }
    // 处理消息
    this.client.onmessage = this.resolveMessage.bind(this);
    // 打开后自动上传下一个文件
    this.uploadNextFile();
  }

  // 上传下一个文件
  private uploadNextFile() {
    // 获取文件
    if (this.fileList.length > this.currentIndex) {
      this.currentFileItem = this.fileList[this.currentIndex++];
      this.currentFile = this.currentFileItem.file as File;
      this.currentFileSize = 0;
      this.currentPart = 0;
      this.totalPart = Math.ceil(this.currentFile.size / PART_SIZE);
      // 开始上传 发送开始上传信息
      this.client?.send(JSON.stringify({
        type: UploadOperatorType.START,
        fileId: this.currentFileItem.uid,
      }));
    } else {
      // 无文件关闭会话
      this.client?.close();
    }
  }

  // 上传下一块数据
  private async uploadNextPart() {
    let reader = undefined as unknown as FileReader;
    try {
      if (this.currentPart < this.totalPart) {
        // 有下一个分片则上传
        const start = this.currentPart++ * PART_SIZE;
        const end = Math.min(this.currentFile.size, start + PART_SIZE);
        const chunk = this.currentFile.slice(start, end);
        reader = new FileReader();
        // 读取数据
        const arrayBuffer = await new Promise((resolve, reject) => {
          reader.onload = () => resolve(reader.result);
          reader.onerror = (error) => reject(error);
          reader.readAsArrayBuffer(chunk);
        });
        // 发送数据
        this.client?.send(arrayBuffer as ArrayBuffer);
        // 计算进度
        this.currentFileSize += (end - start);
        this.currentFileItem.percent = (this.currentFileSize / this.currentFile.size);
      } else {
        // 没有下一个分片则发送完成
        this.client?.send(JSON.stringify({
          type: UploadOperatorType.FINISH,
          fileId: this.currentFileItem.uid,
        }));
      }
    } catch (e) {
      // 发送读取文件失败
      this.client?.send(JSON.stringify({
        type: UploadOperatorType.ERROR,
        fileId: this.currentFileItem.uid,
      }));
      // 释放资源
      if (reader) {
        closeFileReader(reader);
      }
    }
  }

  // 接收消息
  private async resolveMessage(message: MessageEvent) {
    // 文本消息
    const data = JSON.parse(message.data) as ResponseMessageBody;
    if (data.type === UploadReceiverType.NEXT) {
      // 上传下一块数据
      await this.uploadNextPart();
    } else if (data.type === UploadReceiverType.FINISH) {
      this.currentFileItem.status = 'done';
      // 上传下一个文件
      this.uploadNextFile();
    } else if (data.type === UploadReceiverType.ERROR) {
      this.currentFileItem.status = 'error';
      // 上传下一个文件
      this.uploadNextFile();
    }
  }

  // 设置 hook
  setHook(hook: Function): void {
    this.hook = hook;
  }

  // 关闭
  close(): void {
    this.client?.close();
  }

}
