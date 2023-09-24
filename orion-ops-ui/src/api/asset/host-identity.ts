import axios from 'axios';
import { DataGrid, Pagination } from '@/types/global';
import { TableData } from '@arco-design/web-vue/es/table/interface';

/**
 * 主机身份创建请求
 */
export interface HostIdentityCreateRequest {
  name?: string;
  username?: string;
  password?: string;
  keyId?: number;
}

/**
 * 主机身份更新请求
 */
export interface HostIdentityUpdateRequest extends HostIdentityCreateRequest {
  id: number | undefined;
  useNewPassword?: boolean;
}

/**
 * 主机身份查询请求
 */
export interface HostIdentityQueryRequest extends Pagination {
  id?: number;
  name?: string;
  username?: string;
  password?: string;
  keyId?: number;
}

/**
 * 主机身份查询响应
 */
export interface HostIdentityQueryResponse extends TableData {
  id?: number;
  name?: string;
  username?: string;
  password?: string;
  keyId?: number;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 创建主机身份
 */
export function createHostIdentity(request: HostIdentityCreateRequest) {
  return axios.post('/asset/host-identity/create', request);
}

/**
 * 通过 id 更新主机身份
 */
export function updateHostIdentity(request: HostIdentityUpdateRequest) {
  return axios.put('/asset/host-identity/update', request);
}

/**
 * 通过 id 查询主机身份
 */
export function getHostIdentity(id: number) {
  return axios.get<HostIdentityQueryResponse>('/asset/host-identity/get', { params: { id } });
}

/**
 * 查询主机身份
 */
export function getHostIdentityList() {
  return axios.get<Array<HostIdentityQueryResponse>>('/asset/host-identity/list');
}

/**
 * 分页查询主机身份
 */
export function getHostIdentityPage(request: HostIdentityQueryRequest) {
  return axios.post<DataGrid<HostIdentityQueryResponse>>('/asset/host-identity/query', request);
}

/**
 * 通过 id 删除主机身份
 */
export function deleteHostIdentity(id: number) {
  return axios.delete('/asset/host-identity/delete', { params: { id } });
}

