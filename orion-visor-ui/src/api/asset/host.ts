import type { HostExtraUpdateRequest, HostSpecExtraModel } from './host-extra';
import type { TableData } from '@arco-design/web-vue';
import type { DataGrid, FavoriteItem, OrderDirection, Pagination } from '@/types/global';
import axios from 'axios';
import qs from 'query-string';

// 主机类型
export type HostType = 'SSH' | 'RDP' | 'VNC' | string | undefined;

/**
 * 主机创建请求
 */
export interface HostCreateRequest {
  types?: Array<string>;
  osType?: string;
  archType?: string;
  name?: string;
  code?: string;
  address?: string;
  description?: string;
  tags?: Array<number>;
  groupIdList?: Array<number>;
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
 * 主机查询请求
 */
export interface HostQueryRequest extends Pagination, OrderDirection {
  searchValue?: string;
  id?: number;
  type?: string;
  osType?: string;
  archType?: string;
  name?: string;
  code?: string;
  address?: string;
  status?: string;
  tags?: Array<number>;
  queryTag?: boolean;
  queryGroup?: boolean;
  querySpec?: boolean;
  description?: string;
}

/**
 * 主机查询基础响应
 */
export interface HostQueryBaseResponse {
  id: number;
  types: Array<string>;
  osType: string;
  archType: string;
  name: string;
  code: string;
  address: string;
  status: string;
  description: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 主机查询响应
 */
export interface HostQueryResponse extends HostQueryBaseResponse, TableData, FavoriteItem, HostQueryResponseExtra {
  alias: string;
  color: string;
  tags: Array<{ id: number, name: string }>;
  groupIdList: Array<number>;
  spec: HostSpecExtraModel;
}

/**
 * 主机操作拓展
 */
export interface HostQueryResponseExtra {
  editable: boolean;
  loading: boolean;
  modCount: number;
  extra?: Record<string, any>;
}

/**
 * 主机测试连接请求
 */
export interface HostTestConnectRequest {
  id: number;
  type: string;
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
 * 复制主机
 */
export function copyHost(request: HostUpdateRequest) {
  return axios.post('/asset/host/copy', request);
}

/**
 * 通过 id 更新主机状态
 */
export function updateHostStatus(request: HostUpdateStatusRequest) {
  return axios.put('/asset/host/update-status', request);
}

/**
 * 修改主机规格信息
 */
export function updateHostSpec(request: Partial<HostExtraUpdateRequest>) {
  return axios.put('/asset/host/update-spec', request);
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

/**
 * 测试连接主机
 */
export function testHostConnect(request: HostTestConnectRequest) {
  return axios.post('/asset/host/test-connect', request, { timeout: 60000 });
}
