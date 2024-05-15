// 上传操作类型
export const UploadOperatorType = {
  // 开始上传
  START: 'start',
  // 上传完成
  FINISH: 'finish',
  // 上传失败
  ERROR: 'error',
};

// 上传响应类型
export const UploadReceiverType = {
  // 请求下一块数据
  NEXT: 'next',
  // 上传完成
  FINISH: 'finish',
  // 上传失败
  ERROR: 'error',
};

// 请求消息体
export interface RequestMessageBody {
  type: string;
  fileId: string;
}

// 响应消息体
export interface ResponseMessageBody {
  type: string;
  fileId: string;
  path: string;
}

// 文件上传器 定义
export interface IFileUploader {
  // 开始
  start(): Promise<void>;

  // 设置 hook
  setHook(hook: Function): void;

  // 关闭
  close(): void;
}
