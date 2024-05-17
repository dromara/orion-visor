import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';
import qs from 'query-string';

/**
 * 主机连接日志查询请求
 */
export interface HostConnectLogQueryRequest extends Pagination {
  id?: number;
  userId?: number;
  hostId?: number;
  hostAddress?: string;
  type?: string;
  token?: string;
  status?: string;
  startTimeRange?: string[];
}

/**
 * 主机连接日志查询响应
 */
export interface HostConnectLogQueryResponse extends TableData {
  id: number;
  userId: number;
  username: number;
  hostId: number;
  hostName: string;
  hostAddress: string;
  type: string;
  token: string;
  status: string;
  startTime: number;
  endTime: number;
  extra: HostConnectLogExtra;
}

/**
 * 主机连接日志拓展对象
 */
export interface HostConnectLogExtra {
  traceId: string;
  channelId: string;
  sessionId: string;
  address: string;
  location: string;
  userAgent: string;
  errorMessage: string;
}

/**
 * 分页查询主机连接日志
 */
export function getHostConnectLogPage(request: HostConnectLogQueryRequest) {
  return axios.post<DataGrid<HostConnectLogQueryResponse>>('/asset/host-connect-log/query', request);
}

/**
 * 查询全部主机连接会话
 */
export function getHostConnectSessions(request: HostConnectLogQueryRequest) {
  return axios.post<Array<HostConnectLogQueryResponse>>('/asset/host-connect-log/session', request);
}

/**
 * 查询用户最近连接的主机
 */
export function getLatestConnectHostId(type: string, limit: number) {
  return axios.post<Array<number>>('/asset/host-connect-log/latest-connect', {
    type,
    limit
  });
}

/**
 * 删除主机连接日志
 */
export function deleteHostConnectLog(idList: Array<number>) {
  return axios.delete('/asset/host-connect-log/delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询主机连接日志数量
 */
export function getHostConnectLogCount(request: HostConnectLogQueryRequest) {
  return axios.post<number>('/asset/host-connect-log/query-count', request);
}

/**
 * 清空主机连接日志
 */
export function clearHostConnectLog(request: HostConnectLogQueryRequest) {
  return axios.post<number>('/asset/host-connect-log/clear', request);
}

/**
 * 强制断开主机连接
 */
export function hostForceOffline(request: HostConnectLogQueryRequest) {
  return axios.put('/asset/host-connect-log/force-offline', request);
}
