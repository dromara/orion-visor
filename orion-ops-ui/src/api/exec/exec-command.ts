import type { ExecCommandLogQueryResponse } from './exec-command-log';
import axios from 'axios';

/**
 * 执行命令请求
 */
export interface ExecCommandRequest {
  logId?: number;
  description?: string;
  timeout?: number;
  command?: string;
  parameterSchema?: string;
  hostIdList?: Array<number>;
}

/**
 * 中断命令请求
 */
export interface ExecCommandInterruptRequest {
  logId?: number;
  hostLogId?: number;
}

/**
 * 批量执行命令
 */
export function batchExecCommand(request: ExecCommandRequest) {
  return axios.post<ExecCommandLogQueryResponse>('/asset/exec-command/exec', request);
}

/**
 * 重新执行命令
 */
export function reExecCommand(request: ExecCommandRequest) {
  return axios.post<ExecCommandLogQueryResponse>('/asset/exec-command/re-exec', request);
}

/**
 * 中断执行命令
 */
export function interruptExecCommand(request: ExecCommandInterruptRequest) {
  return axios.put('/asset/exec-command/interrupt', request);
}

/**
 * 中断执行主机命令
 */
export function interruptHostExecCommand(request: ExecCommandInterruptRequest) {
  return axios.put('/asset/exec-command/interrupt-host', request);
}
