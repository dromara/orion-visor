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
  parameterSchema: string;
  timeout: number;
  status: string;
  startTime: number;
  finishTime: number;
  hostIdList: Array<number>;
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
  hostAddress: string;
  status: string;
  command: string;
  parameter: string;
  exitStatus: number;
  errorMessage: string;
  startTime: number;
  finishTime: number;
}

/**
 * 执行状态查询响应
 */
export interface ExecStatusResponse {
  logList: Array<ExecLogQueryResponse>;
  hostList: Array<ExecHostLogQueryResponse>;
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
 * 查询命令执行状态
 */
export function getExecLogStatus(idList: Array<number>) {
  return axios.get<ExecStatusResponse>('/asset/exec-log/status', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询历史执行记录
 */
export function getExecLogHistory(limit: number) {
  return axios.get<Array<ExecLogQueryResponse>>('/asset/exec-log/history', { params: { page: 1, limit } });
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

/**
 * 删除主机执行记录
 */
export function deleteExecHostLog(id: number) {
  return axios.delete('/asset/exec-log/delete-host', { params: { id } });
}

/**
 * 查询操作日志数量
 */
export function getExecLogCount(request: ExecLogQueryRequest) {
  return axios.post<number>('/asset/exec-log/query-count', request);
}

/**
 * 清空操作日志
 */
export function clearExecLog(request: ExecLogQueryRequest) {
  return axios.post<number>('/asset/exec-log/clear', request);
}
