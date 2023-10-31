import type { DataGrid, Pagination } from '@/types/global';
import axios from 'axios';

/**
 * 操作日志查询参数
 */
export interface OperatorLogQueryRequest extends Pagination {
  userId?: number;
  username?: string;
  module?: string;
  type?: string;
  riskLevel?: string;
  result?: number;
  startTimeStart?: string;
  startTimeEnd?: string;
}

/**
 * 操作日志查询响应
 */
export interface OperatorLogQueryResponse {
  id: number;
  userId: number;
  username: string;
  traceId: string;
  address: string;
  location: string;
  userAgent: string;
  riskLevel: string;
  module: string;
  type: string;
  logInfo: string;
  extra: string;
  result: number;
  errorMessage: string;
  returnValue: string;
  duration: number;
  startTime: number;
  endTime: number;
  createTime: number;
}

/**
 * 登录日志查询响应
 */
export interface LoginHistoryQueryResponse {
  id: number;
  address: string;
  location: string;
  userAgent: string;
  result: number;
  errorMessage: string;
  createTime: number;
}

/**
 * 分页操作日志
 */
export function getOperatorLogPage(request: OperatorLogQueryRequest) {
  return axios.post<DataGrid<OperatorLogQueryResponse>>('/infra/operator-log/query', request);
}

/**
 * 查询登录日志
 */
export function getLoginHistory(username: string) {
  return axios.get<LoginHistoryQueryResponse[]>('/infra/operator-log/login-history', { params: { username } });
}

/**
 * 查询当前用户登录日志
 */
export function getCurrentLoginHistory() {
  return axios.get<LoginHistoryQueryResponse[]>('/infra/operator-log/current-login-history');
}
