import type { LineSingleChartData } from '@/types/global';
import type { TerminalConnectLogQueryResponse } from '@/api/terminal/terminal-connect-log';
import axios from 'axios';

/**
 * 终端模块工作台响应
 */
export interface TerminalWorkplaceStatisticsResponse {
  todayTerminalConnectCount: number;
  todayTerminalCommandCount: number;
  weekTerminalConnectCount: number;
  weekTerminalCommandCount: number;
  terminalConnectChart: LineSingleChartData;
  terminalCommandChart: LineSingleChartData;
  terminalConnectList: Array<TerminalConnectLogQueryResponse>;
}

/**
 * 查询终端模块工作台统计信息
 */
export function getTerminalWorkplaceStatisticsData() {
  return axios.get<TerminalWorkplaceStatisticsResponse>('/terminal/statistics/get-workplace');
}
