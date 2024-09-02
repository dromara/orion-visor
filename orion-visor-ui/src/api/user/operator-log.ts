import type { ClearRequest, DataGrid, Pagination } from '@/types/global';
import axios from 'axios';
import qs from 'query-string';

/**
 * 操作日志查询参数
 */
export interface OperatorLogQueryRequest extends Pagination {
  userId?: number;
  username?: string;
  module?: string;
  type?: string;
  riskLevel?: string;
  result?: number;
  startTimeRange?: string[];
}

/**
 * 操作日志清理参数
 */
export interface OperatorLogClearRequest extends OperatorLogQueryRequest, ClearRequest {
}

/**
 * 操作日志查询响应
 */
export interface OperatorLogQueryResponse {
  id: number;
  userId: number;
  username: string;
  traceId: string;
  address: string;
  location: string;
  userAgent: string;
  riskLevel: string;
  module: string;
  type: string;
  logInfo: string;
  originLogInfo: string;
  extra: string;
  result: number;
  errorMessage: string;
  returnValue: string;
  duration: number;
  startTime: number;
  endTime: number;
  createTime: number;
}

/**
 * 分页操作日志
 */
export function getOperatorLogPage(request: OperatorLogQueryRequest) {
  return axios.post<DataGrid<OperatorLogQueryResponse>>('/infra/operator-log/query', request);
}

/**
 * 删除操作日志
 */
export function deleteOperatorLog(idList: Array<number>) {
  return axios.delete('/infra/operator-log/delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询操作日志数量
 */
export function getOperatorLogCount(request: OperatorLogQueryRequest) {
  return axios.post<number>('/infra/operator-log/count', request);
}

/**
 * 清空操作日志
 */
export function clearOperatorLog(request: OperatorLogClearRequest) {
  return axios.post<number>('/infra/operator-log/clear', request);
}
