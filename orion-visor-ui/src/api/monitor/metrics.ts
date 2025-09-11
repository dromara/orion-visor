import type { TableData } from '@arco-design/web-vue';
import type { DataGrid, OrderDirection, Pagination } from '@/types/global';
import axios from 'axios';

/**
 * 监控指标创建请求
 */
export interface MetricsCreateRequest {
  name?: string;
  measurement?: string;
  value?: string;
  unit?: string;
  suffix?: string;
  description?: string;
}

/**
 * 监控指标更新请求
 */
export interface MetricsUpdateRequest extends MetricsCreateRequest {
  id?: number;
}

/**
 * 监控指标查询请求
 */
export interface MetricsQueryRequest extends Pagination, OrderDirection {
  searchValue?: string;
  id?: number;
  name?: string;
  measurement?: string;
  value?: string;
  unit?: string;
  suffix?: string;
  description?: string;
}

/**
 * 监控指标查询响应
 */
export interface MetricsQueryResponse extends TableData {
  id: number;
  name: string;
  measurement: string;
  value: string;
  unit: string;
  suffix: string;
  description: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 创建监控指标
 */
export function createMetrics(request: MetricsCreateRequest) {
  return axios.post<number>('/monitor/monitor-metrics/create', request);
}

/**
 * 更新监控指标
 */
export function updateMetrics(request: MetricsUpdateRequest) {
  return axios.put<number>('/monitor/monitor-metrics/update', request);
}

/**
 * 查询监控指标
 */
export function getMetrics(id: number) {
  return axios.get<MetricsQueryResponse>('/monitor/monitor-metrics/get', { params: { id } });
}

/**
 * 查询全部监控指标
 */
export function getMetricsList() {
  return axios.get<Array<MetricsQueryResponse>>('/monitor/monitor-metrics/list');
}

/**
 * 分页查询监控指标
 */
export function getMetricsPage(request: MetricsQueryRequest) {
  return axios.post<DataGrid<MetricsQueryResponse>>('/monitor/monitor-metrics/query', request);
}

/**
 * 删除监控指标
 */
export function deleteMetrics(id: number) {
  return axios.delete<number>('/monitor/monitor-metrics/delete', { params: { id } });
}
