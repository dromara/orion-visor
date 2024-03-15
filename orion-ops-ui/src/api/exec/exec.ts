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
  hostIdList?: number[];
}

/**
 * 中断命令请求
 */
export interface ExecInterruptRequest {
  logId?: number;
  hostLogId?: number;
}

/**
 * 执行命令响应
 */
export interface ExecCommandResponse {
  id: number;
  hosts: {
    id: number;
    hostId: number;
  };
}

/**
 * 批量执行命令
 */
export function batchExecCommand(request: ExecCommandRequest) {
  return axios.post<ExecCommandResponse>('/asset/exec/exec-command', request);
}

/**
 * 重新执行命令
 */
export function reExecCommand(request: ExecCommandRequest) {
  return axios.post<ExecCommandResponse>('/asset/exec/re-exec-command', request);
}

/**
 * 中断执行命令
 */
export function interruptExec(request: ExecInterruptRequest) {
  return axios.put('/asset/exec/interrupt', request);
}

/**
 * 中断执行主机命令
 */
export function interruptHostExec(request: ExecInterruptRequest) {
  return axios.put('/asset/exec/interrupt-host', request);
}
