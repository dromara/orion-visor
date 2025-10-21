import type { TableData } from '@arco-design/web-vue';
import type { DataGrid, OrderDirection, Pagination, ClearRequest } from '@/types/global';
import axios from 'axios';
import qs from 'query-string';

/**
 * 告警事件处理请求
 */
export interface AlarmEventHandleRequest {
  idList?: Array<number>;
  handleStatus?: string;
  handleTime?: number;
  handleRemark?: string;
}

/**
 * 告警事件误报请求
 */
export interface AlarmEventFalseAlarmRequest {
  idList?: Array<number>;
}

/**
 * 告警事件查询请求
 */
export interface AlarmEventQueryRequest extends Pagination, OrderDirection {
  id?: number;
  sourceType?: string;
  sourceId?: number;
  agentKey?: string;
  policyId?: number;
  metricsId?: number;
  metricsMeasurement?: string;
  alarmLevel?: number;
  falseAlarm?: number;
  handleStatus?: string;
  handleRemark?: string;
  handleUserId?: number;
  createTimeRange?: string[];
}

/**
 * 告警事件清理请求
 */
export interface AlarmEventClearRequest extends AlarmEventQueryRequest, ClearRequest {
}

/**
 * 告警事件查询响应
 */
export interface AlarmEventQueryResponse extends TableData {
  id: number;
  sourceType: string;
  sourceId: number;
  sourceInfo: Record<string, any>;
  hostName: string;
  hostAddress: string;
  agentKey: string;
  policyId: number;
  metricsId: number;
  metricsMeasurement: string;
  alarmTags: string;
  alarmValue: any;
  alarmThreshold: any;
  alarmInfo: string;
  alarmLevel: number;
  triggerCondition: string;
  consecutiveCount: number;
  falseAlarm: number;
  handleStatus: string;
  handleTime: number;
  handleRemark: string;
  handleUserId: number;
  handleUsername: string;
  createTime: number;
  updateTime: number;
  updater: string;
}

/**
 * 处理告警事件
 */
export function handleAlarmEvent(request: AlarmEventHandleRequest) {
  return axios.post<number>('/monitor/alarm-event/handle', request);
}

/**
 * 设置为误报
 */
export function setAlarmEventFalse(request: AlarmEventFalseAlarmRequest) {
  return axios.post<number>('/monitor/alarm-event/set-false', request);
}

/**
 * 分页查询告警事件
 */
export function getAlarmEventPage(request: AlarmEventQueryRequest) {
  return axios.post<DataGrid<AlarmEventQueryResponse>>('/monitor/alarm-event/query', request);
}

/**
 * 查询告警事件数量
 */
export function getAlarmEventCount(request: AlarmEventQueryRequest) {
  return axios.post<number>('/monitor/alarm-event/count', request);
}

/**
 * 删除告警事件
 */
export function deleteAlarmEvent(id: number) {
  return axios.delete<number>('/monitor/alarm-event/delete', { params: { id } });
}

/**
 * 批量删除告警事件
 */
export function batchDeleteAlarmEvent(idList: Array<number>) {
  return axios.delete<number>('/monitor/alarm-event/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 清理告警事件
 */
export function clearMonitorAlarmEvent(request: AlarmEventClearRequest) {
  return axios.post<number>('/monitor/alarm-event/clear', request);
}
