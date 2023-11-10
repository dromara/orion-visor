import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';
import qs from 'query-string';

/**
 * 数据分组关联创建请求
 */
export interface DataGroupRelCreateRequest {
  groupId?: number;
  relId?: number;
  type?: string;
  sort?: number;
}

/**
 * 数据分组关联更新请求
 */
export interface DataGroupRelUpdateRequest extends DataGroupRelCreateRequest {
  id?: number;
}

/**
 * 数据分组关联查询请求
 */
export interface DataGroupRelQueryRequest extends Pagination {
  searchValue?: string;
  id?: number;
  groupId?: number;
  relId?: number;
  type?: string;
  sort?: number;
}

/**
 * 数据分组关联查询响应
 */
export interface DataGroupRelQueryResponse extends TableData {
  id: number;
  groupId: number;
  relId: number;
  type: string;
  sort: number;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 创建数据分组关联
 */
export function createDataGroupRel(request: DataGroupRelCreateRequest) {
  return axios.post('/infra/data-group-rel/create', request);
}

/**
 * 更新数据分组关联
 */
export function updateDataGroupRel(request: DataGroupRelUpdateRequest) {
  return axios.put('/infra/data-group-rel/update', request);
}

/**
 * 查询数据分组关联
 */
export function getDataGroupRel(id: number) {
  return axios.get<DataGroupRelQueryResponse>('/infra/data-group-rel/get', { params: { id } });
}

/**
 * 批量查询数据分组关联
 */
export function batchGetDataGroupRelList(idList: Array<number>) {
  return axios.get<DataGroupRelQueryResponse[]>('/infra/data-group-rel/batch-get', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询全部数据分组关联
 */
export function getDataGroupRelList(request: DataGroupRelQueryRequest) {
  return axios.post<Array<DataGroupRelQueryResponse>>('/infra/data-group-rel/list', request);
}

/**
 * 分页查询数据分组关联
 */
export function getDataGroupRelPage(request: DataGroupRelQueryRequest) {
  return axios.post<DataGrid<DataGroupRelQueryResponse>>('/infra/data-group-rel/query', request);
}

/**
 * 删除数据分组关联
 */
export function deleteDataGroupRel(id: number) {
  return axios.delete('/infra/data-group-rel/delete', { params: { id } });
}

/**
 * 批量删除数据分组关联
 */
export function batchDeleteDataGroupRel(idList: Array<number>) {
  return axios.delete('/infra/data-group-rel/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
