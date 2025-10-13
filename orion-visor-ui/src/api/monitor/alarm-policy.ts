import type { TableData } from '@arco-design/web-vue';
import type { DataGrid, OrderDirection, Pagination } from '@/types/global';
import axios from 'axios';

/**
 * 监控告警策略创建请求
 */
export interface AlarmPolicyCreateRequest {
  name?: string;
  type?: string;
  description?: string;
  notifyIdList?: Array<number>;
}

/**
 * 监控告警策略更新请求
 */
export interface AlarmPolicyUpdateRequest extends AlarmPolicyCreateRequest {
  id?: number;
  updateNotify?: boolean;
}

/**
 * 监控告警策略查询请求
 */
export interface AlarmPolicyQueryRequest extends Pagination, OrderDirection {
  id?: number;
  type?: string;
  name?: string;
  description?: string;
}

/**
 * 监控告警策略查询响应
 */
export interface AlarmPolicyQueryResponse extends TableData {
  id: number;
  type: string;
  name: string;
  description: string;
  notifyIdList: Array<number>;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 创建监控告警策略
 */
export function createAlarmPolicy(request: AlarmPolicyCreateRequest) {
  return axios.post<number>('/monitor/alarm-policy/create', request);
}

/**
 * 更新监控告警策略
 */
export function updateAlarmPolicy(request: AlarmPolicyUpdateRequest) {
  return axios.put<number>('/monitor/alarm-policy/update', request);
}

/**
 * 复制监控告警策略
 */
export function copyAlarmPolicy(request: AlarmPolicyCreateRequest) {
  return axios.post<number>('/monitor/alarm-policy/copy', request);
}

/**
 * 查询监控告警策略
 */
export function getAlarmPolicy(id: number) {
  return axios.get<AlarmPolicyQueryResponse>('/monitor/alarm-policy/get', { params: { id } });
}

/**
 * 查询全部监控告警策略
 */
export function getAlarmPolicyList(type: string) {
  return axios.get<Array<AlarmPolicyQueryResponse>>('/monitor/alarm-policy/list', { params: { type } });
}

/**
 * 分页查询监控告警策略
 */
export function getAlarmPolicyPage(request: AlarmPolicyQueryRequest) {
  return axios.post<DataGrid<AlarmPolicyQueryResponse>>('/monitor/alarm-policy/query', request);
}

/**
 * 删除监控告警策略
 */
export function deleteAlarmPolicy(id: number) {
  return axios.delete<number>('/monitor/alarm-policy/delete', { params: { id } });
}
