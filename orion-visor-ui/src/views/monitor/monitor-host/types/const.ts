export const TableName = 'monitor_host';

// 监控告警开关
export const AlarmSwitch = {
  OFF: 0,
  ON: 1,
};

// 探针日志状态
export const AgentLogStatus = {
  WAIT: 'WAIT',
  RUNNING: 'RUNNING',
  SUCCESS: 'SUCCESS',
  FAILED: 'FAILED',
};

export const NODATA_TIPS = '暂无数据';

// 探针安装状态 字典项
export const InstallStatusKey = 'agentInstallStatus';

// 探针在线状态 字典项
export const OnlineStatusKey = 'agentOnlineStatus';

// 监控告警开关 字典项
export const AlarmSwitchKey = 'monitorAlarmSwitch';

// 探针日志状态 字典项
export const AgentLogStatusKey = 'agentLogStatus';

// 加载的字典值
export const dictKeys = [InstallStatusKey, AlarmSwitchKey, OnlineStatusKey, AgentLogStatusKey];
