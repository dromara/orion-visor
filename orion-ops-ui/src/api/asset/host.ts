import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';

/**
 * 主机创建请求
 */
export interface HostCreateRequest {
  name?: string;
  code?: string;
  address?: string;
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
 * 主机查询请求
 */
export interface HostQueryRequest extends Pagination {
  searchValue?: string;
  id?: number;
  name?: string;
  code?: string;
  address?: string;
  tags?: Array<number>;
  queryTag?: boolean;
}

/**
 * 主机查询响应
 */
export interface HostQueryResponse extends TableData {
  id: number;
  name: string;
  code: string;
  address: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
  favorite: boolean;
  alias: string;
  tags: Array<{ id: number, name: string }>;
  groupIdList: Array<number>;

  editable: boolean;
  loading: boolean;
  modCount: number;
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
 * 查询全部主机
 */
export function getHostList() {
  return axios.get<Array<HostQueryResponse>>('/asset/host/list');
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
