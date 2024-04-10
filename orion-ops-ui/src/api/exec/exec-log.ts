import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';
import qs from 'query-string';

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
  hosts: Array<ExecHostLogQueryResponse>;
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
 * 查询执行记录
 */
export function getExecLog(id: number) {
  return axios.get<ExecLogQueryResponse>('/asset/exec-log/get', { params: { id } });
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
