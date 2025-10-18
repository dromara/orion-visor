export const TableName = 'alarm_event';

// 最大清理数量
export const maxClearLimit = 2000;

// 是否为误报
export const FalseAlarm = {
  // 误报
  TRUE: 1,
  // 非误报
  FALSE: 0,
};

// 告警来源类型
export const AlarmSourceType = {
  HOST: 'HOST',
  UPTIME: 'UPTIME',
};

// 告警条件 字典项
export const TriggerConditionKey = 'alarmTriggerCondition';

// 告警事件处理状态 字典项
export const HandleStatusKey = 'alarmEventHandleStatus';

// 是否为误报 字典项
export const FalseAlarmKey = 'falseAlarm';

// 指标数据集 字典项
export const MetricsMeasurementKey = 'metricsMeasurement';

// 告警等级 字典项
export const AlarmLevelKey = 'alarmLevel';

// 加载的字典值
export const dictKeys = [TriggerConditionKey, HandleStatusKey, FalseAlarmKey, MetricsMeasurementKey, AlarmLevelKey];
