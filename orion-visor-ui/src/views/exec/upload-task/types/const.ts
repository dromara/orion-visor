// 上传任务状态
export const UploadTaskStatus = {
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

// 最大清理数量
export const maxClearLimit = 2000;

// 上传任务状态 字典项
export const uploadTaskStatusKey = 'uploadTaskStatus';

// 加载的字典值
export const dictKeys = [uploadTaskStatusKey];
