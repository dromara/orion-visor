import axios from 'axios';

/**
 * 中断命令请求
 */
export interface ExecTailRequest {
  execId?: number;
  hostExecIdList?: Array<number>;
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
  return axios.get('/asset/exec/download-log', { unwrap: true, params: { id } });
}
