import type { LineSingleChartData } from '@/types/global';
import type { LoginHistoryQueryResponse } from '@/api/user/user';
import axios from 'axios';

/**
 * 基建模块工作台响应
 */
export interface InfraWorkplaceStatisticsResponse {
  userId: number;
  username: string;
  nickname: string;
  unreadMessageCount: number;
  lastLoginTime: number;
  userSessionCount: number;
  operatorChart: LineSingleChartData;
  loginHistoryList: Array<LoginHistoryQueryResponse>;
}

/**
 * 查询基建模块工作台统计信息
 */
export function getInfraWorkplaceStatisticsData() {
  return axios.get<InfraWorkplaceStatisticsResponse>('/infra/statistics/get-workplace');
}
