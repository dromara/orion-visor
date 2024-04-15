import axios from 'axios';

/**
 * cron 下次执行时间请求对象
 */
export interface CronNextRequest {
  expression: number;
  times: string;
}

/**
 * cron 下次执行时间响应对象
 */
export interface CronNextResponse {
  valid: boolean;
  next: Array<string>;
}

/**
 * 获取 cron 下次执行时间
 */
export function getCronNextTime(request: CronNextRequest) {
  return axios.get<CronNextResponse>('/infra/expression/cron-next', { params: request });
}
