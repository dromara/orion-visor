import type { TableData } from '@arco-design/web-vue';
import type { DataGrid, Pagination, TimeChartSeries } from '@/types/global';
import type { HostAgentLogResponse } from '@/api/asset/host-agent';
import axios from 'axios';

/**
 * 监控主机更新请求
 */
export interface MonitorHostUpdateRequest {
  id?: number;
  policyId?: number;
  alarmSwitch?: number;
  ownerUserId?: number;
  cpuName?: string;
  diskName?: string;
  networkName?: string;
}

/**
 * 监控主机更新请求
 */
export interface MonitorHostSwitchUpdateRequest {
  idList?: Array<number>;
  alarmSwitch?: number;
}

/**
 * 监控主机查询请求
 */
export interface MonitorHostQueryRequest extends Pagination {
  agentKeys?: Array<string>;
  searchValue?: string;
  alarmSwitch?: number;
  policyId?: number;
  ownerUserId?: number;
  name?: string;
  code?: string;
  address?: string;
  agentKey?: string;
  agentInstallStatus?: number;
  agentOnlineStatus?: number;
  description?: string;
  tags?: Array<number>;
}

/**
 * 监控主机标签查询请求
 */
export interface MonitorHostQueryTagRequest {
  measurement?: string;
  policyId?: number;
  agentKeys?: Array<string>;
}

/**
 * 监控主机图表查询请求
 */
export interface MonitorHostChartRequest {
  agentKeys?: Array<string>;
  measurement?: string;
  fields?: Array<string>;
  window?: string;
  aggregate?: string;
  range?: string;
  start?: string;
  end?: string;
}

/**
 * 监控指标数据
 */
export interface MonitorMetricsData {
  timestamp: number;
  metrics: Array<MonitorMetrics>;
}

/**
 * 监控指标
 */
export interface MonitorMetrics {
  type: string;
  tags: Record<string, string>;
  values: Record<string, number>;
}

/**
 * 监控主机查询响应
 */
export interface MonitorHostQueryResponse extends TableData {
  id: number;
  hostId: number;
  policyId: number;
  policyName: string;
  osType: string;
  name: string;
  code: string;
  address: string;
  status: string;
  types: Array<string>;
  agentKey: string;
  agentVersion: string;
  latestVersion: string;
  agentInstallStatus: number;
  agentOnlineStatus: number;
  agentOnlineChangeTime: number;
  alarmSwitch: number;
  ownerUserId: number;
  ownerUsername: string;
  tags: Array<{ id: number, name: string }>;
  meta: MonitorHostMeta;
  config: MonitorHostConfig;
  metricsData: MonitorHostMetricsData;
  installLog: HostAgentLogResponse;
}

/**
 * 监控元数据
 */
export interface MonitorHostMeta {
  cpus: Array<string>;
  disks: Array<string>;
  nets: Array<string>;
  memoryBytes: number;
}

/**
 * 监控配置
 */
export interface MonitorHostConfig {
  cpuName: string;
  diskName: string;
  networkName: string;
}

/**
 * 监控数据
 */
export interface MonitorHostMetricsData {
  agentKey: string;
  noData: boolean;
  timestamp: number;
  cpuName: string;
  diskName: string;
  networkName: string;
  cpuUsagePercent: number;
  memoryUsagePercent: number;
  memoryUsageBytes: number;
  load1: number;
  load5: number;
  load15: number;
  diskUsagePercent: number;
  diskUsageBytes: number;
  networkSentPreBytes: number;
  networkRecvPreBytes: number;
}

/**
 * 查询监控主机指标
 */
export function getMonitorHostMetrics(agentKeys: Array<string>) {
  return axios.post<Array<MonitorHostMetricsData>>('/monitor/monitor-host/metrics', {
    agentKeys
  }, {
    promptBizErrorMessage: false,
    promptRequestErrorMessage: false
  });
}

/**
 * 获取监控主机概览
 */
export function getMonitorHostOverride(agentKey: string) {
  return axios.get<MonitorMetricsData>('/monitor/monitor-host/override', { params: { agentKey } });
}

/**
 * 查询监控主机图表
 */
export function getMonitorHostChart(request: MonitorHostChartRequest) {
  return axios.post<Array<TimeChartSeries>>('/monitor/monitor-host/chart', request, {
    timeout: 180000,
  });
}

/**
 * 分页查询监控主机
 */
export function getMonitorHostPage(request: MonitorHostQueryRequest) {
  return axios.post<DataGrid<MonitorHostQueryResponse>>('/monitor/monitor-host/query', request);
}

/**
 * 查询监控主机标签
 */
export function getMonitorHostTags(request: MonitorHostQueryTagRequest) {
  return axios.post<Array<string>>('/monitor/monitor-host/host-tags', request);
}

/**
 * 更新监控主机
 */
export function updateMonitorHost(request: MonitorHostUpdateRequest) {
  return axios.put<number>('/monitor/monitor-host/update', request);
}

/**
 * 更新监控主机告警开关
 */
export function updateMonitorHostAlarmSwitch(request: MonitorHostSwitchUpdateRequest) {
  return axios.put<number>('/monitor/monitor-host/update-switch', request);
}
