import type { IRdpSession, SftpFile } from '@/views/terminal/interfaces';
import type { Reactive } from 'vue';
import type Guacamole from 'guacamole-common-js';

// 终端文件传输管理器定义
export interface ITerminalTransferManager {
  sftp: ISftpTransferManager;
  rdp: IRdpTransferManager;
}

// 文件传输文件项
export interface FileTransferItem {
  name: string;
  parentPath: string;
  size: number;
  file?: File;
  paths?: Array<string>;
  unknownSize?: boolean;
}

// 文件传输文件项
export interface FileTransferReactiveState {
  currentSize: number,
  totalSize: number;
  progress: number | string;
  status: string;
  errorMessage?: string;
  finished: boolean;
  aborted: boolean;
}

// 文件传输管理器定义
export interface ITransferManager {
  // 传输的文件列表
  tasks: Array<Reactive<FileTransferTaskType>>;

  // 取消传输
  cancelTransfer: (fileId: string) => void;
  // 取消全部执行中的传输
  cancelAllTransfer: () => void;
}

// SFTP 文件传输管理器定义
export interface ISftpTransferManager extends ITransferManager {
  // 添加上传任务
  addUpload: (hostId: number, parentPath: string, files: Array<File>) => Promise<void>;
  // 添加下载任务
  addDownload: (hostId: number, currentPath: string, files: Array<SftpFile>) => Promise<void>;
}

// RDP 文件传输管理器定义
export interface IRdpTransferManager extends ITransferManager {
  // 添加上传任务
  addUpload: (session: IRdpSession, file: File) => Promise<void>;
  // 添加下载任务
  addDownload: (session: IRdpSession, stream: Guacamole.InputStream, mimetype: string, name: string) => void;
  // 通过 sessionKey 关闭
  closeBySessionKey: (sessionKey: string) => void;
}

// 文件传输任务类型
export type FileTransferTaskType = IFileUploadTask | IFileDownloadTask;
export type MaybeFileTransferTask = IFileUploadTask & IFileDownloadTask;

// 设置传输客户端
export interface ISetTransferClient<T> {
  setClient: (client: T) => void;
}

// 文件传输任务定义
export interface IFileTransferTask {
  type: string;
  source: string;
  fileId: string;
  hostId: number;
  sessionKey: string;
  // 文件项
  fileItem: FileTransferItem;
  // 状态
  state: Reactive<FileTransferReactiveState>;

  // 开始
  start: () => void;
  // 完成
  finish: () => void;
  // 失败
  error: () => void;
  // 中断
  abort: () => void;

  // 传输完成回调
  onFinish: () => void;
  // 传输失败回调
  onError: (msg: string | undefined) => void;
  // 传输中断回调
  onAbort: () => void;
}

// 文件上传任务定义
export interface IFileUploadTask extends IFileTransferTask {
  // 请求上传下一个分片
  onNextPart: () => Promise<void>;
}

// 文件下载任务定义
export interface IFileDownloadTask extends IFileTransferTask {
  // 开始下载回调
  onStart: (channelId: string, token: string) => void;
  // 下载进度回调
  onProgress: (totalSize: number | undefined, currentSize: number | undefined) => void;
}

// 传输操作响应
export interface TransferOperatorResponse {
  channelId?: string;
  type: string;
  hostId?: number;
  currentSize?: number;
  transferToken?: string;
  success: boolean;
  msg?: string;
  totalSize?: number;
}
