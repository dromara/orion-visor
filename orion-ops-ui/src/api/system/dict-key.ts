import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';
import qs from 'query-string';

/**
 * 字典配置项创建请求
 */
export interface DictKeyCreateRequest {
  keyName?: string;
  valueType?: string;
  extraSchema?: string;
  description?: string;
}

/**
 * 字典配置项更新请求
 */
export interface DictKeyUpdateRequest extends DictKeyCreateRequest {
  id?: number;
}

/**
 * 字典配置项查询请求
 */
export interface DictKeyQueryRequest extends Pagination {
  searchValue?: string;
  id?: number;
  keyName?: string;
  description?: string;
}

/**
 * 字典配置项查询响应
 */
export interface DictKeyQueryResponse extends TableData {
  id?: number;
  keyName?: string;
  valueType?: string;
  extraSchema?: string;
  description?: string;
}

/**
 * 创建字典配置项
 */
export function createDictKey(request: DictKeyCreateRequest) {
  return axios.post('/infra/dict-key/create', request);
}

/**
 * 更新字典配置项
 */
export function updateDictKey(request: DictKeyUpdateRequest) {
  return axios.put('/infra/dict-key/update', request);
}

/**
 * 查询全部字典配置项
 */
export function getDictKeyList() {
  return axios.post<Array<DictKeyQueryResponse>>('/infra/dict-key/list');
}

/**
 * 分页查询字典配置项
 */
export function getDictKeyPage(request: DictKeyQueryRequest) {
  return axios.post<DataGrid<DictKeyQueryResponse>>('/infra/dict-key/query', request);
}

/**
 * 删除字典配置项
 */
export function deleteDictKey(id: number) {
  return axios.delete('/infra/dict-key/delete', { params: { id } });
}

/**
 * 批量删除字典配置项
 */
export function batchDeleteDictKey(idList: Array<number>) {
  return axios.delete('/infra/dict-key/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
