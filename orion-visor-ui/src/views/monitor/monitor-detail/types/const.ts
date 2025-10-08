import type { WindowUnit, MetricUnitType, MetricUnitFormatOptions } from '@/utils/metrics';
import { dictKeys as alarmDictKeys } from '@/views/monitor/alarm-event/types/const';

// 图表组件配置
export interface MetricsChartProps {
  agentKeys: Array<string>;
  range: string;
  windowValue: number;
  windowUnit: WindowUnit;
  option: MetricsChartOption;
}

// 图表显示配置
export interface MetricsChartOption {
  name: string;
  type?: 'line' | 'bar';
  measurement: string;
  fields: Array<string>;
  span?: number;
  legend?: boolean;
  background?: boolean;
  smooth?: boolean;
  colors: Array<[string, string]>;
  aggregate: string;
  unit: MetricUnitType;
  unitOption: MetricUnitFormatOptions;
}

// tab
export const TabKeys = {
  OVERVIEW: 'overview',
  CHART: 'chart',
  ALARM: 'alarm',
};

export const TableName = 'host_alarm_event';

// 探针在线状态 字典项
export const OnlineStatusKey = 'agentOnlineStatus';

// 监控告警开关 字典项
export const AlarmSwitchKey = 'monitorAlarmSwitch';

// 指标图表区间 字典项
export const ChartRangeKey = 'metricsChartRange';

// 指标聚合函数 字典项
export const MetricsAggregateKey = 'metricsAggregate';

// 加载的字典值
export const dictKeys = [AlarmSwitchKey, OnlineStatusKey, ChartRangeKey, MetricsAggregateKey, ...alarmDictKeys];
