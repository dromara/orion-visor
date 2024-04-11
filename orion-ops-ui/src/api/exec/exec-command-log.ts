import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';
import qs from 'query-string';

/**
 * 批量执行日志查询请求
 */
export interface ExecCommandLogQueryRequest extends Pagination {
  id?: number;
  userId?: number;
  description?: string;
  command?: string;
  status?: string;
  startTimeRange?: string[];
}

/**
 * 批量执行日志查询响应
 */
export interface ExecCommandLogQueryResponse extends TableData, ExecCommandLogQueryExtraResponse {
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
  hosts: Array<ExecCommandHostLogQueryResponse>;
}

/**
 * 批量执行日志查询响应 拓展
 */
export interface ExecCommandLogQueryExtraResponse {
  hosts: Array<ExecCommandHostLogQueryResponse>;
}

/**
 * 主机批量执行日志查询响应
 */
export interface ExecCommandHostLogQueryResponse extends TableData {
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
 * 批量执行状态查询响应
 */
export interface ExecCommandLogStatusResponse {
  logList: Array<ExecCommandLogQueryResponse>;
  hostList: Array<ExecCommandHostLogQueryResponse>;
}

/**
 * 批量执行日志 tail 请求
 */
export interface ExecCommandLogTailRequest {
  execId?: number;
  hostExecIdList?: Array<number>;
}

/**
 * 分页查询批量执行日志
 */
export function getExecCommandLogPage(request: ExecCommandLogQueryRequest) {
  return axios.post<DataGrid<ExecCommandLogQueryResponse>>('/asset/exec-command-log/query', request);
}

/**
 * 查询批量执行日志
 */
export function getExecCommandLog(id: number) {
  return axios.get<ExecCommandLogQueryResponse>('/asset/exec-command-log/get', { params: { id } });
}

/**
 * 查询主机批量执行日志
 */
export function getExecCommandHostLogList(logId: number) {
  return axios.get<Array<ExecCommandHostLogQueryResponse>>('/asset/exec-command-log/host-list', { params: { logId } });
}

/**
 * 查询命令执行状态
 */
export function getExecCommandLogStatus(idList: Array<number>) {
  return axios.get<ExecCommandLogStatusResponse>('/asset/exec-command-log/status', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询历史批量执行日志
 */
export function getExecCommandLogHistory(limit: number) {
  return axios.get<Array<ExecCommandLogQueryResponse>>('/asset/exec-command-log/history', { params: { page: 1, limit } });
}

/**
 * 删除批量执行日志
 */
export function deleteExecCommandLog(id: number) {
  return axios.delete('/asset/exec-command-log/delete', { params: { id } });
}

/**
 * 批量删除批量执行日志
 */
export function batchDeleteExecCommandLog(idList: Array<number>) {
  return axios.delete('/asset/exec-command-log/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 删除主机批量执行日志
 */
export function deleteExecCommandHostLog(id: number) {
  return axios.delete('/asset/exec-command-log/delete-host', { params: { id } });
}

/**
 * 查询批量执行日志数量
 */
export function getExecCommandLogCount(request: ExecCommandLogQueryRequest) {
  return axios.post<number>('/asset/exec-command-log/query-count', request);
}

/**
 * 清空批量执行日志
 */
export function clearExecCommandLog(request: ExecCommandLogQueryRequest) {
  return axios.post<number>('/asset/exec-command-log/clear', request);
}

/**
 * 查看批量执行日志
 */
export function getExecCommandLogTailToken(request: ExecCommandLogTailRequest) {
  return axios.post<string>('/asset/exec-command-log/tail', request);
}

/**
 * 下载批量执行日志文件
 */
export function downloadExecCommandLogFile(id: number) {
  return axios.get('/asset/exec-command-log/download', { unwrap: true, params: { id } });
}
