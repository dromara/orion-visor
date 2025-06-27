import type { DataGrid, OrderDirection, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue';
import type { HostQueryResponse } from '@/api/asset/host';
import axios from 'axios';
import qs from 'query-string';

/**
 * 计划任务创建请求
 */
export interface ExecJobCreateRequest {
  name?: string;
  expression?: string;
  timeout?: number;
  scriptExec?: number;
  command?: string;
  parameterSchema?: string;
  hostIdList?: Array<number>;
}

/**
 * 计划任务更新请求
 */
export interface ExecJobUpdateRequest extends ExecJobCreateRequest {
  id?: number;
}

/**
 * 计划任务状态更新请求
 */
export interface ExecJobUpdateStatusRequest {
  id: number;
  status: number;
}

/**
 * 更新计划任务执行用户
 */
export interface ExecJobUpdateExecUserRequest {
  id?: number;
  userId?: number;
}

/**
 * 计划任务查询请求
 */
export interface ExecJobQueryRequest extends Pagination, OrderDirection {
  id?: number;
  name?: string;
  command?: string;
  status?: number;
  queryRecentLog?: boolean;
  execUserId?: number;
}

/**
 * 计划任务查询响应
 */
export interface ExecJobQueryResponse extends TableData {
  id: number;
  name: string;
  expression: string;
  timeout: number;
  scriptExec?: number;
  command: string;
  parameterSchema: string;
  status: number;
  recentLogId: number;
  recentLogStatus: string;
  recentLogTime: number;
  execUserId: number;
  execUsername: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
  hostIdList: Array<number>;
  hostList: Array<HostQueryResponse>;
}

/**
 * 创建计划任务
 */
export function createExecJob(request: ExecJobCreateRequest) {
  return axios.post('/exec/exec-job/create', request);
}

/**
 * 更新计划任务
 */
export function updateExecJob(request: ExecJobUpdateRequest) {
  return axios.put('/exec/exec-job/update', request);
}

/**
 * 更新计划任务状态
 */
export function updateExecJobStatus(request: ExecJobUpdateStatusRequest) {
  return axios.put('/exec/exec-job/update-status', request);
}

/**
 * 更新计划任务执行用户
 */
export function updateExecJobExecUser(request: ExecJobUpdateExecUserRequest) {
  return axios.put<number>('/exec/exec-job/update-exec-user', request);
}

/**
 * 查询计划任务
 */
export function getExecJob(id: number) {
  return axios.get<ExecJobQueryResponse>('/exec/exec-job/get', { params: { id } });
}

/**
 * 查询全部计划任务
 */
export function getExecJobList() {
  return axios.get<Array<ExecJobQueryResponse>>('/exec/exec-job/list');
}

/**
 * 分页查询计划任务
 */
export function getExecJobPage(request: ExecJobQueryRequest) {
  return axios.post<DataGrid<ExecJobQueryResponse>>('/exec/exec-job/query', request);
}

/**
 * 查询计划任务数量
 */
export function getExecJobCount(request: ExecJobQueryRequest) {
  return axios.post<number>('/exec/exec-job/count', request);
}

/**
 * 删除计划任务
 */
export function deleteExecJob(id: number) {
  return axios.delete('/exec/exec-job/delete', { params: { id } });
}

/**
 * 批量删除计划任务
 */
export function batchDeleteExecJob(idList: Array<number>) {
  return axios.delete('/exec/exec-job/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 手动触发计划任务
 */
export function triggerExecJob(id: number) {
  return axios.post('/exec/exec-job/trigger', { id });
}
