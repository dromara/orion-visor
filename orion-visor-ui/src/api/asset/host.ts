import type { DataGrid, OrderDirection, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue';
import axios from 'axios';
import qs from 'query-string';

// 主机类型
export type HostType = 'SSH' | string | undefined;

/**
 * 主机创建请求
 */
export interface HostCreateRequest {
  type?: string;
  osType?: string;
  name?: string;
  code?: string;
  address?: string;
  port?: number;
  tags?: Array<number>;
  groupIdList?: Array<number>;
  description?: string;
}

/**
 * 主机更新请求
 */
export interface HostUpdateRequest extends HostCreateRequest {
  id?: number;
}

/**
 * 主机更新状态请求
 */
export interface HostUpdateStatusRequest {
  id: number;
  status: string;
}

/**
 * 主机更新配置请求
 */
export interface HostUpdateConfigRequest {
  id: number;
  config: string;
}

/**
 * 主机查询请求
 */
export interface HostQueryRequest extends Pagination, OrderDirection {
  searchValue?: string;
  id?: number;
  type?: string;
  osType?: string;
  name?: string;
  code?: string;
  address?: string;
  status?: string;
  tags?: Array<number>;
  queryTag?: boolean;
  description?: string;
}

/**
 * 主机查询响应
 */
export interface HostQueryResponse extends TableData, HostQueryResponseExtra {
  id: number;
  type: string;
  osType?: string;
  name: string;
  code: string;
  address: string;
  port: string;
  status: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
  favorite: boolean;
  alias: string;
  color: string;
  tags: Array<{ id: number, name: string }>;
  groupIdList: Array<number>;
  description: string;
}

/**
 * 主机操作拓展
 */
export interface HostQueryResponseExtra {
  editable: boolean;
  loading: boolean;
  modCount: number;
}

/**
 * 主机 配置查询响应
 */
export interface HostConfigQueryResponse extends HostConfigQueryResponseExtra {
  id: number;
  type: string;
  config: Record<string, any>;
}

/**
 * 主机配置拓展
 */
export interface HostConfigQueryResponseExtra {
  current: number;
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
 * 通过 id 更新主机状态
 */
export function updateHostStatus(request: HostUpdateStatusRequest) {
  return axios.put('/asset/host/update-status', request);
}

/**
 * 通过 id 更新主机配置
 */
export function updateHostConfig(request: HostUpdateConfigRequest) {
  return axios.put('/asset/host/update-config', request);
}

/**
 * 查询主机
 */
export function getHost(id: number) {
  return axios.get<HostQueryResponse>('/asset/host/get', { params: { id } });
}

/**
 * 查询全部主机
 */
export function getHostList(type: HostType) {
  return axios.get<Array<HostQueryResponse>>('/asset/host/list', { params: { type } });
}

/**
 * 通过 id 查询主机配置
 */
export function getHostConfig(id: number) {
  return axios.get<HostConfigQueryResponse>('/asset/host/get-config', { params: { id } });
}

/**
 * 分页查询主机
 */
export function getHostPage(request: HostQueryRequest) {
  return axios.post<DataGrid<HostQueryResponse>>('/asset/host/query', request);
}

/**
 * 查询主机数量
 */
export function getHostCount(request: HostQueryRequest) {
  return axios.post<number>('/asset/host/count', request);
}

/**
 * 通过 id 删除主机
 */
export function deleteHost(id: number) {
  return axios.delete('/asset/host/delete', { params: { id } });
}

/**
 * 批量删除主机
 */
export function batchDeleteHost(idList: Array<number>) {
  return axios.delete('/asset/host/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
