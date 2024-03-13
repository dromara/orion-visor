import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';
import qs from 'query-string';

/**
 * 执行记录查询请求
 */
export interface ExecLogQueryRequest extends Pagination {
  id?: number;
  userId?: number;
  description?: string;
  command?: string;
  status?: string;
  startTimeRange?: string[];
}

/**
 * 执行记录查询响应
 */
export interface ExecLogQueryResponse extends TableData, ExecLogQueryExtraResponse {
  id: number;
  userId: number;
  username: string;
  description: string;
  command: string;
  timeout: number;
  status: string;
  startTime: number;
  finishTime: number;
}

/**
 * 执行记录查询响应 拓展
 */
export interface ExecLogQueryExtraResponse {
  hosts: Array<ExecHostLogQueryResponse>;
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
  errorMessage: string;
  startTime: number;
  finishTime: number;
}

/**
 * 查询执行记录
 */
export function getExecLog(id: number) {
  return axios.get<ExecLogQueryResponse>('/asset/exec-log/get', { params: { id } });
}

/**
 * 分页查询执行记录
 */
export function getExecLogPage(request: ExecLogQueryRequest) {
  return axios.post<DataGrid<ExecLogQueryResponse>>('/asset/exec-log/query', request);
}

/**
 * 查询主机执行记录
 */
export function getExecHostLogList(logId: number) {
  return axios.get<Array<ExecHostLogQueryResponse>>('/asset/exec-log/host-list', { params: { logId } });
}

/**
 * 删除执行记录
 */
export function deleteExecLog(id: number) {
  return axios.delete('/asset/exec-log/delete', { params: { id } });
}

/**
 * 批量删除执行记录
 */
export function batchDeleteExecLog(idList: Array<number>) {
  return axios.delete('/asset/exec-log/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
