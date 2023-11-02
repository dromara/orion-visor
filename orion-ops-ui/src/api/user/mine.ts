import type { DataGrid } from '@/types/global';
import type { LoginHistoryQueryResponse, OperatorLogQueryRequest, OperatorLogQueryResponse } from './operator-log';
import type { UserQueryResponse, UserSessionOfflineRequest, UserSessionQueryResponse, UserUpdateRequest } from './user';
import axios from 'axios';

/**
 * 修改密码请求
 */
export interface UserUpdatePasswordRequest {
  beforePassword?: string;
  password?: string;
}

/**
 * 修改当前用户密码
 */
export function updateCurrentUserPassword(request: UserUpdatePasswordRequest) {
  return axios.put('/infra/mine/update-password', request);
}

/**
 * 查询当前用户
 */
export function getCurrentUser() {
  return axios.get<UserQueryResponse>('/infra/mine/get-user');
}

/**
 * 更新当前用户
 */
export function updateCurrentUser(request: UserUpdateRequest) {
  return axios.put('/infra/mine/update-user', request);
}

/**
 * 查询当前用户登录日志
 */
export function getCurrentLoginHistory() {
  return axios.get<LoginHistoryQueryResponse[]>('/infra/mine/login-history');
}

/**
 * 获取当前用户会话列表
 */
export function getCurrentUserSessionList() {
  return axios.get<UserSessionQueryResponse[]>('/infra/mine/user-session');
}

/**
 * 下线当前用户会话
 */
export function offlineCurrentUserSession(request: UserSessionOfflineRequest) {
  return axios.put('/infra/mine/offline-session', request);
}

/**
 * 查询当前用户操作日志
 */
export function getCurrentUserOperatorLog(request: OperatorLogQueryRequest) {
  return axios.post<DataGrid<OperatorLogQueryResponse>>('/infra/mine/query-operator-log', request);
}
