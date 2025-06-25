import type { DataGrid, OrderDirection, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue';
import type { RoleQueryResponse } from '@/api/user/role';
import axios from 'axios';
import qs from 'query-string';

/**
 * 用户创建请求
 */
export interface UserCreateRequest {
  username?: string;
  password?: string;
  nickname?: string;
  avatar?: string;
  mobile?: string;
  email?: string;
  description?: string;
}

/**
 * 用户更新请求
 */
export interface UserUpdateRequest extends UserCreateRequest {
  id?: number;
  status?: number;
  roleIdList?: Array<number>;
}

/**
 * 用户查询请求
 */
export interface UserQueryRequest extends Pagination, OrderDirection {
  id?: number;
  username?: string;
  password?: string;
  nickname?: string;
  mobile?: string;
  email?: string;
  status?: number;
  description?: string;
}

/**
 * 用户查询响应
 */
export interface UserQueryResponse extends TableData {
  id: number;
  username: string;
  nickname: string;
  avatar: string;
  mobile: string;
  email: string;
  status: number;
  lastLoginTime?: number;
  description: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
  roles: Array<RoleQueryResponse>;
}

/**
 * 用户会话查询响应
 */
export interface UserSessionQueryResponse {
  visible: boolean;
  current: boolean;
  address: string;
  location: string;
  userAgent: string;
  loginTime: number;
}

/**
 * 用户会话下线请求
 */
export interface UserSessionOfflineRequest {
  userId?: number;
  timestamp: number;
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
 * 创建用户
 */
export function createUser(request: UserCreateRequest) {
  return axios.post('/infra/system-user/create', request);
}

/**
 * 通过 id 更新用户
 */
export function updateUser(request: UserUpdateRequest) {
  return axios.put('/infra/system-user/update', request);
}

/**
 * 修改用户状态
 */
export function updateUserStatus(request: UserUpdateRequest) {
  return axios.put('/infra/system-user/update-status', request);
}

/**
 * 修改用户角色
 */
export function grantUserRole(request: UserUpdateRequest) {
  return axios.put('/infra/system-user/grant-role', request);
}

/**
 * 重置用户密码
 */
export function resetUserPassword(request: UserUpdateRequest) {
  return axios.put('/infra/system-user/reset-password', request);
}

/**
 * 通过 id 查询用户
 */
export function getUser(id: number) {
  return axios.get<UserQueryResponse>('/infra/system-user/get', { params: { id } });
}

/**
 * 查询所有用户
 */
export function getUserList() {
  return axios.get<UserQueryResponse[]>('/infra/system-user/list');
}

/**
 * 查询用户的 roleId
 */
export function getUserRoleIdList(userId: number) {
  return axios.get<Array<number>>('/infra/system-user/get-roles', { params: { userId } });
}

/**
 * 分页查询用户
 */
export function getUserPage(request: UserQueryRequest) {
  return axios.post<DataGrid<UserQueryResponse>>('/infra/system-user/query', request);
}

/**
 * 查询用户数量
 */
export function getUserCount(request: UserQueryRequest) {
  return axios.post<number>('/infra/system-user/count', request);
}

/**
 * 通过 id 删除用户
 */
export function deleteUser(id: number) {
  return axios.delete('/infra/system-user/delete', { params: { id } });
}

/**
 * 批量删除用户
 */
export function batchDeleteUser(idList: Array<number>) {
  return axios.delete('/infra/system-user/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 获取用户会话列表
 */
export function getUserSessionList(id: number) {
  return axios.get<Array<UserSessionQueryResponse>>('/infra/system-user/session/list', { params: { id } });
}

/**
 * 下线用户会话
 */
export function offlineUserSession(request: UserSessionOfflineRequest) {
  return axios.put('/infra/system-user/session/offline', request);
}

/**
 * 查询登录日志
 */
export function getLoginHistory(username: string, count: number) {
  return axios.get<LoginHistoryQueryResponse[]>('/infra/system-user/login-history', { params: { username, count } });
}
