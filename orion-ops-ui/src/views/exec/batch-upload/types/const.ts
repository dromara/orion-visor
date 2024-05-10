// 上传任务状态定义
export interface UploadTaskStatusType {
  value: string;
  step: number;
  status: string;
  formPanel: boolean;
}

// 上传任务状态
export const UploadTaskStatus = {
  // 等待中
  WAITING: {
    value: 'WAITING',
    step: 1,
    status: 'process',
    formPanel: true,
  },
  // 请求中
  REQUESTING: {
    value: 'REQUESTING',
    step: 2,
    status: 'process',
    formPanel: true,
  },
  // 上传中
  UPLOADING: {
    value: 'UPLOADING',
    step: 3,
    status: 'process',
    formPanel: false,
  },
  // 已完成
  FINISHED: {
    value: 'FINISHED',
    step: 4,
    status: 'finish',
    formPanel: false,
  },
  // 已失败
  FAILED: {
    value: 'FAILED',
    step: 4,
    status: 'error',
    formPanel: false,
  },
};

// 上传任务文件状态
export const UploadTaskFileStatus = {
  // 等待中
  WAITING: 'WAITING',
  // 上传中
  UPLOADING: 'UPLOADING',
  // 已完成
  FINISHED: 'FINISHED',
  // 已完成
  FAILED: 'FAILED',
  // 已取消
  CANCELED: 'CANCELED',
};

// 上传任务状态 字典项
export const taskStatusKey = 'uploadTaskStatus';

// 上传任务文件状态 字典项
export const fileStatusKey = 'uploadTaskFileStatus';

// 加载的字典值
export const dictKeys = [taskStatusKey, fileStatusKey];
