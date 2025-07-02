import type { DataGrid, OrderDirection, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue';
import axios from 'axios';
import qs from 'query-string';

/**
 * 终端文件操作日志 查询请求
 */
export interface TerminalFileLogQueryRequest extends Pagination, OrderDirection {
  userId?: number;
  hostId?: number;
  type?: string;
  result?: number;
  startTimeRange?: string[];
}

/**
 * 终端文件操作日志 查询响应
 */
export interface TerminalFileLogQueryResponse extends TableData {
  id: number;
  userId: number;
  username: number;
  hostId: number;
  hostName: string;
  hostAddress: string;
  address: string;
  location: string;
  userAgent: string;
  paths: string[];
  type: string;
  result: string;
  startTime: number;
  extra: TerminalFileLogExtra;
}

/**
 * 终端文件操作日志 拓展对象
 */
export interface TerminalFileLogExtra {
  mod: number;
  target: string;
  maxCount: number;
}

/**
 * 分页查询终端文件操作日志
 */
export function getTerminalFileLogPage(request: TerminalFileLogQueryRequest) {
  return axios.post<DataGrid<TerminalFileLogQueryResponse>>('/terminal/terminal-file-log/query', request);
}

/**
 * 查询终端文件操作日志数量
 */
export function getTerminalFileLogCount(request: TerminalFileLogQueryRequest) {
  return axios.post<number>('/terminal/terminal-file-log/count', request);
}

/**
 * 删除终端文件操作日志
 */
export function deleteTerminalFileLog(idList: Array<number>) {
  return axios.delete('/terminal/terminal-file-log/delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}
