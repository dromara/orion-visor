import axios from 'axios';
import { DataGrid, Pagination } from '@/types/global';

/**
 * 主机创建请求
 */
export interface HostCreateRequest {
  name?: string;
  code?: string;
  address?: string;
  tags?: Array<number>;
}

/**
 * 主机更新请求
 */
export interface HostUpdateRequest extends HostCreateRequest {
  id: number | undefined;
}

/**
 * 主机查询请求
 */
export interface HostQueryRequest extends Pagination {
  id?: number;
  name?: string;
  code?: string;
  address?: string;
  favorite?: boolean;
  tags?: Array<number>;
  extra?: boolean;
  config?: boolean;
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
  favorite: boolean;
  tags: Record<number, string>;
  configs: Record<string, HostConfigQueryResponse>;
}

/**
 * 主机配置请求
 */
export interface HostConfigRequest {
  id?: number;
  hostId?: number;
  version?: number;
  status?: number;
  config?: string;
}

/**
 * 主机配置查询响应
 */
export interface HostConfigQueryResponse {
  id: number;
  type: string;
  version: number;
  status: number;
  config: Record<string, any>;
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
export function getHost(params: HostQueryRequest) {
  return axios.get<HostQueryResponse>('/asset/host/get', { params });
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
 * 查询主机配置
 */
export function getHostConfig(params: HostConfigRequest) {
  return axios.get<HostConfigQueryResponse>('/asset/host/get-config', { params });
}

/**
 * 查询主机配置 - 全部
 */
export function getHostConfigAll(params: HostConfigRequest) {
  return axios.get<Array<HostConfigQueryResponse>>('/asset/host/get-config-all', { params });
}

/**
 * 更新主机配置
 */
export function updateHostConfig(request: HostConfigRequest) {
  return axios.put('/asset/host/update-config', request);
}

/**
 * 更新主机配置状态
 */
export function updateHostConfigStatus(request: HostConfigRequest) {
  return axios.put('/asset/host/update-config-status', request);
}
