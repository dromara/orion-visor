import type { DataGrid, Options, OrderDirection, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue';
import axios from 'axios';
import qs from 'query-string';

/**
 * 字典配置值创建请求
 */
export interface DictValueCreateRequest {
  keyId?: number;
  name?: string;
  value?: string;
  label?: string;
  extra?: string;
  sort?: number;
}

/**
 * 字典配置值更新请求
 */
export interface DictValueUpdateRequest extends DictValueCreateRequest {
  id?: number;
}

/**
 * 字典配置值回滚请求
 */
export interface DictValueRollbackRequest {
  id?: number;
  valueId?: number;
}

/**
 * 字典配置值查询请求
 */
export interface DictValueQueryRequest extends Pagination, OrderDirection {
  keyId?: number;
  keyName?: string;
  value?: string;
  label?: string;
  extra?: string;
}

/**
 * 字典配置值查询响应
 */
export interface DictValueQueryResponse extends TableData {
  id: number;
  keyId: number;
  keyName: string;
  keyDescription: string;
  value: string;
  label: string;
  extra: string;
  sort: number;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 字典配置值选项查询响应
 */
export interface DictValueOptionsQueryResponse extends Options {
}

/**
 * 创建字典配置值
 */
export function createDictValue(request: DictValueCreateRequest) {
  return axios.post('/infra/dict-value/create', request);
}

/**
 * 更新字典配置值
 */
export function updateDictValue(request: DictValueUpdateRequest) {
  return axios.put('/infra/dict-value/update', request);
}

/**
 * 回滚字典配置值
 */
export function rollbackDictValue(request: DictValueRollbackRequest) {
  return axios.put('/infra/dict-value/rollback', request);
}

/**
 * 查询字典配置值
 */
export function getDictValueList(keys: string[]) {
  return axios.get<Record<string, Array<DictValueOptionsQueryResponse>>>('/infra/dict-value/list', {
    params: { keys },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 分页查询字典配置值
 */
export function getDictValuePage(request: DictValueQueryRequest) {
  return axios.post<DataGrid<DictValueQueryResponse>>('/infra/dict-value/query', request);
}

/**
 * 删除字典配置值
 */
export function deleteDictValue(id: number) {
  return axios.delete('/infra/dict-value/delete', { params: { id } });
}

/**
 * 批量删除字典配置值
 */
export function batchDeleteDictValue(idList: Array<number>) {
  return axios.delete('/infra/dict-value/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
