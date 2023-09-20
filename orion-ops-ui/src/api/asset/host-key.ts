import axios from 'axios';
import qs from 'query-string';
import { DataGrid, Pagination } from '@/types/global';

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
  id: number;
}

/**
 * 主机秘钥查询请求
 */
export interface HostKeyQueryRequest extends Pagination {
  id?: number;
  name?: string;
  publicKey?: string;
  privateKey?: string;
  password?: string;
}

/**
 * 主机秘钥查询响应
 */
export interface HostKeyQueryResponse {
  id?: number;
  name?: string;
  publicKey?: string;
  privateKey?: string;
  password?: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
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
 * 通过 id 批量查询主机秘钥
 */
export function getHostKeyList(idList: Array<number>) {
  return axios.get<HostKeyQueryResponse[]>('/asset/host-key/list', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询主机秘钥
 */
export function getHostKeyListAll(request: HostKeyQueryRequest) {
  return axios.post<Array<HostKeyQueryResponse>>('/asset/host-key/list-all', request);
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

/**
 * 通过 id 批量删除主机秘钥
 */
export function batchDeleteHostKey(idList: Array<number>) {
  return axios.delete('/asset/host-key/delete-batch', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 导出主机秘钥
 */
export function exportHostKey(request: HostKeyQueryRequest) {
  return axios.post('/asset/host-key/export', request);
}
