import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';

/**
 * 主机秘钥创建请求
 */
export interface HostKeyCreateRequest {
  name?: string;
  publicKey?: string;
  privateKey?: string;
  password?: string;
}

/**
 * 主机秘钥更新请求
 */
export interface HostKeyUpdateRequest extends HostKeyCreateRequest {
  id?: number;
  useNewPassword?: boolean;
}

/**
 * 主机秘钥查询请求
 */
export interface HostKeyQueryRequest extends Pagination {
  searchValue?: string;
  id?: number;
  name?: string;
  publicKey?: string;
  privateKey?: string;
}

/**
 * 主机秘钥查询响应
 */
export interface HostKeyQueryResponse extends TableData {
  id?: number;
  name?: string;
  publicKey?: string;
  privateKey?: string;
  password?: string;
  createTime: number;
  updateTime: number;
}

/**
 * 创建主机秘钥
 */
export function createHostKey(request: HostKeyCreateRequest) {
  return axios.post('/asset/host-key/create', request);
}

/**
 * 通过 id 更新主机秘钥
 */
export function updateHostKey(request: HostKeyUpdateRequest) {
  return axios.put('/asset/host-key/update', request);
}

/**
 * 通过 id 查询主机秘钥
 */
export function getHostKey(id: number) {
  return axios.get<HostKeyQueryResponse>('/asset/host-key/get', { params: { id } });
}

/**
 * 查询主机秘钥
 */
export function getHostKeyList() {
  return axios.get<Array<HostKeyQueryResponse>>('/asset/host-key/list');
}

/**
 * 分页查询主机秘钥
 */
export function getHostKeyPage(request: HostKeyQueryRequest) {
  return axios.post<DataGrid<HostKeyQueryResponse>>('/asset/host-key/query', request);
}

/**
 * 通过 id 删除主机秘钥
 */
export function deleteHostKey(id: number) {
  return axios.delete('/asset/host-key/delete', { params: { id } });
}
