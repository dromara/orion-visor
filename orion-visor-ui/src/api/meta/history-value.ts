import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';

/**
 * 历史归档查询请求
 */
export interface HistoryValueQueryRequest extends Pagination {
  searchValue?: string;
  relId?: number;
  type?: string;
}

/**
 * 历史归档查询响应
 */
export interface HistoryValueQueryResponse extends TableData {
  id: number;
  beforeValue: string;
  afterValue: string;
  createTime: number;
  creator: string;
}

/**
 * 分页查询历史归档
 */
export function getHistoryValuePage(request: HistoryValueQueryRequest) {
  return axios.post<DataGrid<HistoryValueQueryResponse>>('/infra/history-value/query', request);
}
