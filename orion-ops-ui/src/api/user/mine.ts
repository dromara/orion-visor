import type { LoginHistoryQueryResponse } from './operator-log';
import type { UserQueryResponse, UserUpdateRequest } from './user';
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

