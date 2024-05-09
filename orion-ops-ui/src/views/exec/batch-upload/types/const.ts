// 上传任务状态
export const UploadTaskStatus = {
  // 准备中
  PREPARATION: 'PREPARATION',
  // 上传中
  UPLOADING: 'UPLOADING',
  // 已完成
  FINISHED: 'FINISHED',
  // 已取消
  CANCELED: 'CANCELED',
};

// 上传步骤
export const UploadStep = {
  // 准备中
  PREPARATION: 1,
  // 请求中
  REQUESTING: 2,
  // 分发中
  UPLOADING: 3,
  // 已完成
  FINISHED: 4,
};

// 上传步骤状态
export const UploadStepStatus = {
  // 处理中
  PROCESS: 'process',
  // 上传完成
  FINISH: 'finish',
  // 上传失败
  ERROR: 'error',
};

// 上传任务状态 字典项
export const taskStatusKey = 'uploadTaskStatus';

// 上传任务文件状态 字典项
export const fileStatusKey = 'uploadTaskFileStatus';

// 加载的字典值
export const dictKeys = [taskStatusKey, fileStatusKey];
