import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue';
import axios from 'axios';
import qs from 'query-string';

/**
 * 执行模板创建请求
 */
export interface ExecTemplateCreateRequest {
  name?: string;
  command?: string;
  timeout?: number;
  scriptExec?: number;
  parameterSchema?: string;
  hostIdList?: Array<number>;
}

/**
 * 执行模板更新请求
 */
export interface ExecTemplateUpdateRequest extends ExecTemplateCreateRequest {
  id?: number;
}

/**
 * 执行模板查询请求
 */
export interface ExecTemplateQueryRequest extends Pagination {
  id?: number;
  name?: string;
  command?: string;
}

/**
 * 执行模板查询响应
 */
export interface ExecTemplateQueryResponse extends TableData {
  id: number;
  name: string;
  command: string;
  timeout: number;
  scriptExec?: number;
  parameterSchema: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
  hostIdList?: Array<number>;
}

/**
 * 创建执行模板
 */
export function createExecTemplate(request: ExecTemplateCreateRequest) {
  return axios.post('/asset/exec-template/create', request);
}

/**
 * 更新执行模板
 */
export function updateExecTemplate(request: ExecTemplateUpdateRequest) {
  return axios.put('/asset/exec-template/update', request);
}

/**
 * 查询执行模板
 */
export function getExecTemplate(id: number) {
  return axios.get<ExecTemplateQueryResponse>('/asset/exec-template/get', { params: { id } });
}

/**
 * 查询执行模板
 */
export function getExecTemplateWithAuthorized(id: number) {
  return axios.get<ExecTemplateQueryResponse>('/asset/exec-template/get-with-authorized', { params: { id } });
}

/**
 * 分页查询执行模板
 */
export function getExecTemplatePage(request: ExecTemplateQueryRequest) {
  return axios.post<DataGrid<ExecTemplateQueryResponse>>('/asset/exec-template/query', request);
}

/**
 * 删除执行模板
 */
export function deleteExecTemplate(id: number) {
  return axios.delete('/asset/exec-template/delete', { params: { id } });
}

/**
 * 批量删除执行模板
 */
export function batchDeleteExecTemplate(idList: Array<number>) {
  return axios.delete('/asset/exec-template/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
