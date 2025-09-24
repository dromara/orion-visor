// 告警规则标签
export interface RuleTag {
  key: string;
  value: string[];
}

// 表格名称
export const TableName = 'alarm-rule';

// 默认告警条件
export const DefaultCondition = 'GE';

// 默认告警等级
export const DefaultLevel = 0;

// 指标度量 字典项
export const MeasurementKey = 'metricsMeasurement';

// 监控指标单位 字典项
export const MetricsUnitKey = 'metricsUnit';

// 规则开关 字典项
export const RuleSwitchKey = 'monitorAlarmSwitch';

// 告警条件 字典项
export const TriggerConditionKey = 'alarmTriggerCondition';

// 告警等级 字典项
export const LevelKey = 'alarmLevel';

// 加载的字典值
export const dictKeys = [MetricsUnitKey, MeasurementKey, TriggerConditionKey, RuleSwitchKey, LevelKey];
