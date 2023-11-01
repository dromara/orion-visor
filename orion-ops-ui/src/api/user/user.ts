import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';

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
}

/**
 * 用户更新请求
 */
export interface UserUpdateRequest extends UserCreateRequest {
  id?: number;
  roleIdList?: Array<number>;
  password?: string;
}

/**
 * 用户查询请求
 */
export interface UserQueryRequest extends Pagination {
  id?: number;
  username?: string;
  password?: string;
  nickname?: string;
  avatar?: string;
  mobile?: string;
  email?: string;
  status?: number;
  lastLoginTime?: string;
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
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
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
 * 通过 id 删除用户
 */
export function deleteUser(id: number) {
  return axios.delete('/infra/system-user/delete', { params: { id } });
}

