import axios from 'axios';
import qs from 'query-string';
import { DataGrid, Pagination } from '@/types/global';

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
  status?: number;
  lastLoginTime?: string;
}

/**
 * 用户更新请求
 */
export interface UserUpdateRequest extends UserCreateRequest {
  id: number;
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
export interface UserQueryResponse {
  id?: number;
  username?: string;
  password?: string;
  nickname?: string;
  avatar?: string;
  mobile?: string;
  email?: string;
  status?: number;
  lastLoginTime?: number;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
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
 * 通过 id 查询用户
 */
export function getUser(id: number) {
  return axios.get<UserQueryResponse>('/infra/system-user/get', { params: { id } });
}

/**
 * 通过 id 批量查询用户
 */
export function getUserList(idList: Array<number>) {
  return axios.get<UserQueryResponse[]>('/infra/system-user/list', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
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

/**
 * 通过 id 批量删除用户
 */
export function batchDeleteUser(idList: Array<number>) {
  return axios.delete('/infra/system-user/delete-batch', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
