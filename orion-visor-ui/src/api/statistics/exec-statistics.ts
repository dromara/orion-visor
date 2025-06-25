import type { LineSingleChartData } from '@/types/global';
import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
import axios from 'axios';

/**
 * 执行模块工作台响应
 */
export interface ExecWorkplaceStatisticsResponse {
  execJobCount: number;
  todayExecCommandCount: number;
  weekExecCommandCount: number;
  execCommandChart: LineSingleChartData;
  execLogList: Array<ExecLogQueryResponse>;
}

/**
 * 查询执行模块工作台统计信息
 */
export function getExecWorkplaceStatisticsData() {
  return axios.get<ExecWorkplaceStatisticsResponse>('/exec/statistics/get-workplace');
}
