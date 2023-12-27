import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';

/**
 * 主机连接日志查询请求
 */
export interface HostConnectLogQueryRequest extends Pagination {
  userId?: number;
  hostId?: number;
  hostAddress?: string;
  type?: string;
  token?: string;
  status?: string;
  startTimeRange?: string[];
}

/**
 * 主机连接日志查询响应
 */
export interface HostConnectLogQueryResponse extends TableData {
  id: number;
  userId: number;
  hostId: number;
  hostName: string;
  hostAddress: string;
  type: string;
  token: string;
  status: string;
  startTime: number;
  endTime: number;
  extraInfo: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 分页查询主机连接日志
 */
export function getHostConnectLogPage(request: HostConnectLogQueryRequest) {
  return axios.post<DataGrid<HostConnectLogQueryResponse>>('/asset/host-connect-log/query', request);
}
