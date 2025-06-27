import type { DataGrid } from '@/types/global';
import type {
  ExecHostLogQueryResponse,
  ExecLogClearRequest,
  ExecLogInterruptRequest,
  ExecLogQueryRequest,
  ExecLogQueryResponse,
  ExecLogStatusResponse,
} from '../exec/exec-log';
import axios from 'axios';
import qs from 'query-string';

/**
 * 分页查询计划任务日志
 */
export function getExecJobLogPage(request: ExecLogQueryRequest) {
  return axios.post<DataGrid<ExecLogQueryResponse>>('/exec/exec-job-log/query', request);
}

/**
 * 查询计划任务日志
 */
export function getExecJobLog(id: number) {
  return axios.get<ExecLogQueryResponse>('/exec/exec-job-log/get', { params: { id } });
}

/**
 * 查询主机计划任务日志
 */
export function getExecJobHostLog(id: number) {
  return axios.get<ExecHostLogQueryResponse>('/exec/exec-job-log/get-host', { params: { id } });
}

/**
 * 查询主机计划任务日志
 */
export function getExecJobHostLogList(logId: number) {
  return axios.get<Array<ExecHostLogQueryResponse>>('/exec/exec-job-log/host-list', { params: { logId } });
}

/**
 * 查询命令执行状态
 */
export function getExecJobLogStatus(idList: Array<number>) {
  return axios.get<ExecLogStatusResponse>('/exec/exec-job-log/status', {
    params: { idList },
    promptBizErrorMessage: false,
    promptRequestErrorMessage: false,
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 删除计划任务日志
 */
export function deleteExecJobLog(id: number) {
  return axios.delete('/exec/exec-job-log/delete', { params: { id } });
}

/**
 * 批量删除计划任务日志
 */
export function batchDeleteExecJobLog(idList: Array<number>) {
  return axios.delete('/exec/exec-job-log/batch-delete', {
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
  return axios.delete('/exec/exec-job-log/delete-host', { params: { id } });
}

/**
 * 查询计划任务日志数量
 */
export function getExecJobLogCount(request: ExecLogQueryRequest) {
  return axios.post<number>('/exec/exec-job-log/count', request);
}

/**
 * 清空计划任务日志
 */
export function clearExecJobLog(request: ExecLogClearRequest) {
  return axios.post<number>('/exec/exec-job-log/clear', request, {
    timeout: 60000,
  });
}

/**
 * 查看计划任务日志
 */
export function getExecJobLogTailToken(id: number) {
  return axios.get<string>('/exec/exec-job-log/tail', { params: { id } });
}

/**
 * 下载计划任务日志文件
 */
export function downloadExecJobLogFile(id: number) {
  return axios.get('/exec/exec-job-log/download', {
    unwrap: true,
    responseType: 'blob',
    params: { id },
  });
}

/**
 * 中断计划任务执行
 */
export function interruptExecJob(request: ExecLogInterruptRequest) {
  return axios.put('/exec/exec-job-log/interrupt', request);
}

/**
 * 中断计划任务执行主机
 */
export function interruptHostExecJob(request: ExecLogInterruptRequest) {
  return axios.put('/exec/exec-job-log/interrupt-host', request);
}
