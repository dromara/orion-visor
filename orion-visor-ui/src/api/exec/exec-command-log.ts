import type { DataGrid } from '@/types/global';
import type {
  ExecHostLogQueryResponse,
  ExecLogClearRequest,
  ExecLogInterruptRequest,
  ExecLogQueryRequest,
  ExecLogQueryResponse,
  ExecLogStatusResponse
} from './exec-log';
import axios from 'axios';
import qs from 'query-string';

/**
 * 分页查询批量执行日志
 */
export function getExecCommandLogPage(request: ExecLogQueryRequest) {
  return axios.post<DataGrid<ExecLogQueryResponse>>('/asset/exec-command-log/query', request);
}

/**
 * 查询批量执行日志
 */
export function getExecCommandLog(id: number) {
  return axios.get<ExecLogQueryResponse>('/asset/exec-command-log/get', { params: { id } });
}

/**
 * 查询主机计划任务日志
 */
export function getExecCommandHostLog(id: number) {
  return axios.get<ExecHostLogQueryResponse>('/asset/exec-command-log/get-host', { params: { id } });
}

/**
 * 查询主机批量执行日志
 */
export function getExecCommandHostLogList(logId: number) {
  return axios.get<Array<ExecHostLogQueryResponse>>('/asset/exec-command-log/host-list', { params: { logId } });
}

/**
 * 查询命令执行状态
 */
export function getExecCommandLogStatus(idList: Array<number>) {
  return axios.get<ExecLogStatusResponse>('/asset/exec-command-log/status', {
    params: { idList },
    promptBizErrorMessage: false,
    promptRequestErrorMessage: false,
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询历史批量执行日志
 */
export function getExecCommandLogHistory(limit: number) {
  return axios.get<Array<ExecLogQueryResponse>>('/asset/exec-command-log/history', { params: { page: 1, limit } });
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
export function getExecCommandLogCount(request: ExecLogQueryRequest) {
  return axios.post<number>('/asset/exec-command-log/count', request);
}

/**
 * 清空批量执行日志
 */
export function clearExecCommandLog(request: ExecLogClearRequest) {
  return axios.post<number>('/asset/exec-command-log/clear', request, {
    timeout: 60000,
  });
}

/**
 * 查看批量执行日志
 */
export function getExecCommandLogTailToken(id: number) {
  return axios.get<string>('/asset/exec-command-log/tail', { params: { id } });
}

/**
 * 下载批量执行日志文件
 */
export function downloadExecCommandLogFile(id: number) {
  return axios.get('/asset/exec-command-log/download', {
    unwrap: true,
    responseType: 'blob',
    params: { id },
  });
}

/**
 * 中断执行命令
 */
export function interruptExecCommand(request: ExecLogInterruptRequest) {
  return axios.put('/asset/exec-command-log/interrupt', request);
}

/**
 * 中断执行主机命令
 */
export function interruptHostExecCommand(request: ExecLogInterruptRequest) {
  return axios.put('/asset/exec-command-log/interrupt-host', request);
}
