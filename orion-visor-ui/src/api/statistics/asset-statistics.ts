import type { LineSingleChartData } from '@/types/global';
import type { TerminalConnectLogQueryResponse } from '@/api/asset/terminal-connect-log';
import type { ExecLogQueryResponse } from '@/api/exec/exec-log';
import axios from 'axios';

/**
 * 资产模块工作台响应
 */
export interface AssetWorkplaceStatisticsResponse {
  execJobCount: number;
  todayTerminalConnectCount: number;
  todayExecCommandCount: number;
  weekTerminalConnectCount: number;
  weekExecCommandCount: number;
  execCommandChart: LineSingleChartData;
  terminalConnectChart: LineSingleChartData;
  terminalConnectList: Array<TerminalConnectLogQueryResponse>;
  execLogList: Array<ExecLogQueryResponse>;
}

/**
 * 查询资产模块工作台统计信息
 */
export function getAssetWorkplaceStatisticsData() {
  return axios.get<AssetWorkplaceStatisticsResponse>('/asset/statistics/get-workplace');
}
