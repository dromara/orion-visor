// 表名称
export const TableName = 'conn-log';

// 终端连接类型
export const HostConnectType = {
  SSH: 'SSH',
  SFTP: 'SFTP',
  RDP: 'RDP',
  VNC: 'VNC',
};

// 终端连接状态
export const HostConnectStatus = {
  CONNECTING: 'CONNECTING',
  COMPLETE: 'COMPLETE',
  FAILED: 'FAILED',
  FORCE_OFFLINE: 'FORCE_OFFLINE',
};

// 最大清理数量
export const maxClearLimit = 2000;

// 终端连接状态 字典项
export const connectStatusKey = 'terminalConnectStatus';

// 终端连接类型 字典项
export const connectTypeKey = 'terminalConnectType';

// 加载的字典值
export const dictKeys = [connectStatusKey, connectTypeKey];
