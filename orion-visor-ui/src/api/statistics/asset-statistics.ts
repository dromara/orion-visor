import type { PieChartData } from '@/types/global';
import axios from 'axios';

/**
 * 查询主机类型图表
 */
export function getHostTypeChart() {
  return axios.get<PieChartData>('/asset/statistics/host-type-chart');
}
