import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';
import qs from 'query-string';

/**
 * 计划任务日志查询请求
 */
export interface ExecJobLogQueryRequest extends Pagination {
  id?: number;
  userId?: number;
  description?: string;
  command?: string;
  status?: string;
  startTimeRange?: string[];
}

/**
 * 计划任务日志查询响应
 */
export interface ExecJobLogQueryResponse extends TableData, ExecJobLogQueryExtraResponse {
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
  hosts: Array<ExecJobHostLogQueryResponse>;
}

/**
 * 计划任务日志查询响应 拓展
 */
export interface ExecJobLogQueryExtraResponse {
  hosts: Array<ExecJobHostLogQueryResponse>;
}

/**
 * 主机计划任务日志查询响应
 */
export interface ExecJobHostLogQueryResponse extends TableData {
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
 * 计划任务状态查询响应
 */
export interface ExecJobLogStatusResponse {
  logList: Array<ExecJobLogQueryResponse>;
  hostList: Array<ExecJobHostLogQueryResponse>;
}

/**
 * 计划任务日志 tail 请求
 */
export interface ExecJobLogTailRequest {
  execId?: number;
  hostExecIdList?: Array<number>;
}

/**
 * 分页查询计划任务日志
 */
export function getExecJobLogPage(request: ExecJobLogQueryRequest) {
  return axios.post<DataGrid<ExecJobLogQueryResponse>>('/asset/exec-job-log/query', request);
}

/**
 * 查询计划任务日志
 */
export function getExecJobLog(id: number) {
  return axios.get<ExecJobLogQueryResponse>('/asset/exec-job-log/get', { params: { id } });
}

/**
 * 查询主机计划任务日志
 */
export function getExecJobHostLogList(logId: number) {
  return axios.get<Array<ExecJobHostLogQueryResponse>>('/asset/exec-job-log/host-list', { params: { logId } });
}

/**
 * 查询命令执行状态
 */
export function getExecJobLogStatus(idList: Array<number>) {
  return axios.get<ExecJobLogStatusResponse>('/asset/exec-job-log/status', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 删除计划任务日志
 */
export function deleteExecJobLog(id: number) {
  return axios.delete('/asset/exec-job-log/delete', { params: { id } });
}

/**
 * 批量删除计划任务日志
 */
export function batchDeleteExecJobLog(idList: Array<number>) {
  return axios.delete('/asset/exec-job-log/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 删除主机计划任务日志
 */
export function deleteExecJobHostLog(id: number) {
  return axios.delete('/asset/exec-job-log/delete-host', { params: { id } });
}

/**
 * 查询计划任务日志数量
 */
export function getExecJobLogCount(request: ExecJobLogQueryRequest) {
  return axios.post<number>('/asset/exec-job-log/query-count', request);
}

/**
 * 清空计划任务日志
 */
export function clearExecJobLog(request: ExecJobLogQueryRequest) {
  return axios.post<number>('/asset/exec-job-log/clear', request);
}

/**
 * 查看计划任务日志
 */
export function getExecJobLogTailToken(request: ExecJobLogTailRequest) {
  return axios.post<string>('/asset/exec-job-log/tail', request);
}

/**
 * 下载计划任务日志文件
 */
export function downloadExecJobLogFile(id: number) {
  return axios.get('/asset/exec-job-log/download', { unwrap: true, params: { id } });
}
