// 主机连接类型
export const HostConnectType = {
  SSH: 'SSH',
  SFTP: 'SFTP',
};

// 主机连接状态
export const HostConnectStatus = {
  CONNECTING: 'CONNECTING',
  COMPLETE: 'COMPLETE',
  FAILED: 'FAILED',
  FORCE_OFFLINE: 'FORCE_OFFLINE',
};

// 主机连接状态 字典项
export const connectStatusKey = 'hostConnectStatus';

// 主机连接类型 字典项
export const connectTypeKey = 'hostConnectType';

// 加载的字典值
export const dictKeys = [connectStatusKey, connectTypeKey];
