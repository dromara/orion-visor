import type { SftpFile } from '@/views/terminal/interfaces';

// sftp 传输管理器定义
export interface ISftpTransferManager {
  transferList: Array<SftpTransferItem>;
  // 添加上传任务
  addUpload: (hostId: number, parentPath: string, files: Array<File>) => void;
  // 添加下载任务
  addDownload: (hostId: number, currentPath: string, files: Array<SftpFile>) => void;
  // 取消传输
  cancelTransfer: (fileId: string) => void;
  // 取消全部传输
  cancelAllTransfer: () => void;
}

// sftp 传输处理回调定义
export interface ISftpTransferCallback {
  // 下一分片回调
  onNextPart: () => Promise<void>;
  // 开始回调
  onStart: (channelId: string, token: string) => void;
  // 进度回调
  onProgress: (totalSize: number | undefined, currentSize: number | undefined) => void;
  // 失败回调
  onError: (msg: string | undefined) => void;
  // 完成回调
  onFinish: () => void;
  // 中断回调
  onAbort: () => void;
}

// sftp 传输处理器定义
export interface ISftpTransferHandler extends ISftpTransferCallback {
  // 类型
  type: string;
  // 是否完成
  finished: boolean;
  // 是否中断
  aborted: boolean;
  // 开始
  start: () => void;
  // 完成
  finish: () => void;
  // 失败
  error: () => void;
  // 中断
  abort: () => void;
  // 是否有下一个分片
  hasNextPart: () => boolean;
}

// sftp 上传文件项
export interface SftpTransferItem {
  fileId: string;
  type: string;
  hostId: number;
  name: string;
  parentPath: string;
  currentSize: number,
  totalSize: number;
  progress: number | string;
  status: string;
  errorMessage?: string;
  file: File;
  paths?: Array<string>;
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
