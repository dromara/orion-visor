import type { InfraWorkplaceStatisticsResponse } from '@/api/statistics/infra-statistics';
import type { ExecWorkplaceStatisticsResponse } from '@/api/statistics/exec-statistics';
import type { TerminalWorkplaceStatisticsResponse } from '@/api/statistics/terminal-statistics';
import { execHostStatusKey } from '@/components/exec/log/const';

// 工作台统计数据
export interface WorkplaceStatisticsData {
  infra: InfraWorkplaceStatisticsResponse;
  exec: ExecWorkplaceStatisticsResponse;
  terminal: TerminalWorkplaceStatisticsResponse;
}

// 终端连接类型 字典项
export const terminalConnectTypeKey = 'terminalConnectType';

// 操作日志结果 字典项
export const operatorLogResultKey = 'operatorLogResult';

// 加载的字典值
export const dictKeys = [terminalConnectTypeKey, execHostStatusKey, operatorLogResultKey];
