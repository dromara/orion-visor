import type { Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import { createAppWebSocket } from '@/utils/http';

/**
 * 执行日志查询请求
 */
export interface ExecLogQueryRequest extends Pagination {
  id?: number;
  userId?: number;
  sourceId?: number;
  description?: string;
  command?: string;
  status?: string;
  startTimeRange?: string[];
}

/**
 * 执行日志查询响应
 */
export interface ExecLogQueryResponse extends TableData, ExecLogQueryExtraResponse {
  id: number;
  userId: number;
  username: string;
  description: string;
  execSeq: number;
  command: string;
  parameterSchema: string;
  timeout: number;
  scriptExec?: number;
  status: string;
  startTime: number;
  finishTime: number;
  hostIdList: Array<number>;
}

/**
 * 执行日志查询响应 拓展
 */
export interface ExecLogQueryExtraResponse {
  hosts: Array<ExecHostLogQueryResponse>;
}

/**
 * 主机执行日志查询响应
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
  exitCode: number;
  errorMessage: string;
  startTime: number;
  finishTime: number;
}

/**
 * 执行状态查询响应
 */
export interface ExecLogStatusResponse {
  logList: Array<ExecLogQueryResponse>;
  hostList: Array<ExecHostLogQueryResponse>;
}

/**
 * 执行日志 tail 请求
 */
export interface ExecLogTailRequest {
  execId?: number;
  hostExecIdList?: Array<number>;
}

/**
 * 执行中断命令请求
 */
export interface ExecLogInterruptRequest {
  logId?: number;
  hostLogId?: number;
}

/**
 * 打开执行日志 websocket
 */
export const openExecLogChannel = (token: string) => {
  return createAppWebSocket(`/exec/log/${token}`);
};
