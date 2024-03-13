import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';
import qs from 'query-string';

/**
 * 主机执行记录创建请求
 */
export interface ExecHostLogCreateRequest {
  logId?: number;
  hostId?: number;
  hostName?: string;
  status?: string;
  command?: string;
  parameter?: string;
  exitStatus?: number;
  logPath?: string;
  errorMessage?: string;
  startTime?: string;
  finishTime?: string;
}

/**
 * 主机执行记录更新请求
 */
export interface ExecHostLogUpdateRequest extends ExecHostLogCreateRequest {
  id?: number;
}

/**
 * 主机执行记录查询请求
 */
export interface ExecHostLogQueryRequest extends Pagination {
  searchValue?: string;
  id?: number;
  logId?: number;
  hostId?: number;
  hostName?: string;
  status?: string;
  command?: string;
  parameter?: string;
  exitStatus?: number;
  logPath?: string;
  errorMessage?: string;
  startTime?: string;
  finishTime?: string;
}

/**
 * 主机执行记录查询响应
 */
export interface ExecHostLogQueryResponse extends TableData {
  id: number;
  logId: number;
  hostId: number;
  hostName: string;
  status: string;
  command: string;
  parameter: string;
  exitStatus: number;
  logPath: string;
  errorMessage: string;
  startTime: number;
  finishTime: number;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 创建主机执行记录
 */
export function createExecHostLog(request: ExecHostLogCreateRequest) {
  return axios.post('/asset/exec-host-log/create', request);
}

/**
 * 更新主机执行记录
 */
export function updateExecHostLog(request: ExecHostLogUpdateRequest) {
  return axios.put('/asset/exec-host-log/update', request);
}

/**
 * 查询主机执行记录
 */
export function getExecHostLog(id: number) {
  return axios.get<ExecHostLogQueryResponse>('/asset/exec-host-log/get', { params: { id } });
}

/**
 * 批量查询主机执行记录
 */
export function batchGetExecHostLogList(idList: Array<number>) {
  return axios.get<ExecHostLogQueryResponse[]>('/asset/exec-host-log/batch-get', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询全部主机执行记录
 */
export function getExecHostLogList(request: ExecHostLogQueryRequest) {
  return axios.post<Array<ExecHostLogQueryResponse>>('/asset/exec-host-log/list', request);
}

/**
 * 分页查询主机执行记录
 */
export function getExecHostLogPage(request: ExecHostLogQueryRequest) {
  return axios.post<DataGrid<ExecHostLogQueryResponse>>('/asset/exec-host-log/query', request);
}

/**
 * 删除主机执行记录
 */
export function deleteExecHostLog(id: number) {
  return axios.delete('/asset/exec-host-log/delete', { params: { id } });
}

/**
 * 批量删除主机执行记录
 */
export function batchDeleteExecHostLog(idList: Array<number>) {
  return axios.delete('/asset/exec-host-log/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
