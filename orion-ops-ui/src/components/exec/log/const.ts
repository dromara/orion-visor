// 批量执行状态
export const execStatus = {
  // 等待中
  WAITING: 'WAITING',
  // 运行中
  RUNNING: 'RUNNING',
  // 执行完成
  COMPLETED: 'COMPLETED',
  // 执行失败
  FAILED: 'FAILED',
};

// 主机执行状态
export const execHostStatus = {
  // 等待中
  WAITING: 'WAITING',
  // 运行中
  RUNNING: 'RUNNING',
  // 执行完成
  COMPLETED: 'COMPLETED',
  // 执行失败
  FAILED: 'FAILED',
  // 执行超时
  TIMEOUT: 'TIMEOUT',
  // 已中断
  INTERRUPTED: 'INTERRUPTED',
};

// 执行状态 字典项
export const execStatusKey = 'execStatus';

// 执行状态 字典项
export const execHostStatusKey = 'execHostStatus';

// 加载的字典值
export const dictKeys = [execStatusKey, execHostStatusKey];
