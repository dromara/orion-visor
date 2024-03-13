/**
 * 执行状态
 */
// FIXME 检查这里的类型
export const execHostStatus = {
  // 等待中
  WAITING: 'WAITING',
  // 运行中
  RUNNING: 'RUNNING',
  // 执行完成
  COMPLETED: 'COMPLETED',
  // 执行失败
  FAILED: 'FAILED',
  // 已中断
  INTERRUPTED: 'INTERRUPTED',
}

// 执行状态 字典项
export const execHostStatusKey = 'execHostStatus';

// 加载的字典值
export const dictKeys = [execHostStatusKey];
