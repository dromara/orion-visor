import axios from 'axios';
import qs from 'query-string';
import { DataGrid, Pagination } from '@/types/global';

/**
 * 主机创建请求
 */
export interface HostCreateRequest {
  name?: string;
  code?: string;
  address?: string;
}

/**
 * 主机更新请求
 */
export interface HostUpdateRequest extends HostCreateRequest {
  id: number;
}

/**
 * 主机查询请求
 */
export interface HostQueryRequest extends Pagination {
  id?: number;
  name?: string;
  code?: string;
  address?: string;
}

/**
 * 主机查询响应
 */
export interface HostQueryResponse {
  id?: number;
  name?: string;
  code?: string;
  address?: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 创建主机
 */
export function createHost(request: HostCreateRequest) {
  return axios.post('/asset/host/create', request);
}

/**
 * 通过 id 更新主机
 */
export function updateHost(request: HostUpdateRequest) {
  return axios.put('/asset/host/update', request);
}

/**
 * 通过 id 查询主机
 */
export function getHost(id: number) {
  return axios.get<HostQueryResponse>('/asset/host/get', { params: { id } });
}

/**
 * 通过 id 批量查询主机
 */
export function getHostList(idList: Array<number>) {
  return axios.get<HostQueryResponse[]>('/asset/host/list', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询主机
 */
export function getHostListAll(request: HostQueryRequest) {
  return axios.post<Array<HostQueryResponse>>('/asset/host/list-all', request);
}

/**
 * 分页查询主机
 */
export function getHostPage(request: HostQueryRequest) {
  return axios.post<DataGrid<HostQueryResponse>>('/asset/host/query', request);
}

/**
 * 通过 id 删除主机
 */
export function deleteHost(id: number) {
  return axios.delete('/asset/host/delete', { params: { id } });
}

/**
 * 通过 id 批量删除主机
 */
export function batchDeleteHost(idList: Array<number>) {
  return axios.delete('/asset/host/delete-batch', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
