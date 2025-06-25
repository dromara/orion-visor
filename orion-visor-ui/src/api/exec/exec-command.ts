import type { ExecLogQueryResponse } from './exec-log';
import axios from 'axios';

/**
 * 执行命令请求
 */
export interface ExecCommandRequest {
  logId?: number;
  description?: string;
  timeout?: number;
  scriptExec?: number;
  command?: string;
  parameterSchema?: string;
  hostIdList?: Array<number>;
}

/**
 * 批量执行命令
 */
export function batchExecCommand(request: ExecCommandRequest) {
  return axios.post<ExecLogQueryResponse>('/exec/exec-command/exec', request);
}

/**
 * 重新执行命令
 */
export function reExecCommand(request: ExecCommandRequest) {
  return axios.post<ExecLogQueryResponse>('/exec/exec-command/re-exec', request);
}
