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
export interface ExecInterruptRequest {
  logId?: number;
  hostLogId?: number;
}

/**
 * 中断命令请求
 */
export interface ExecTailRequest {
  execId?: number;
  hostExecIdList?: Array<number>;
}

/**
 * 执行命令响应
 */
export interface ExecCommandResponse {
  id: number;
  status: string;
  startTime: number;
  finishTime: number;
  hosts: Array<ExecCommandHostResponse>;
}

/**
 * 执行命令主机响应
 */
export interface ExecCommandHostResponse {
  id: number;
  hostId: number;
  hostName: string;
  hostAddress: string;
  status: string;
  exitStatus: number;
  errorMessage: string;
  startTime: number;
  finishTime: number;
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

/**
 * 查看执行日志
 */
export function getExecLogTailToken(request: ExecTailRequest) {
  return axios.post<string>('/asset/exec/tail-log', request);
}

/**
 * 下载执行日志文件
 */
export function downloadExecLogFile(id: number) {
  return axios.get<Blob>('/asset/exec/download-log', { unwrap: true, params: { id } });
}
