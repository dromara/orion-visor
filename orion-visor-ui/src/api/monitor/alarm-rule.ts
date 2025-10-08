import type { TableData } from '@arco-design/web-vue';
import axios from 'axios';

/**
 * 监控告警规则创建请求
 */
export interface AlarmRuleCreateRequest {
  policyId?: number;
  metricsId?: number;
  tags?: string;
  level?: number;
  ruleSwitch?: number;
  allEffect?: number;
  triggerCondition?: string;
  threshold?: any;
  consecutiveCount?: number;
  silencePeriod?: number;
  description?: string;
}

/**
 * 监控告警规则更新请求
 */
export interface AlarmRuleUpdateRequest extends AlarmRuleCreateRequest {
  id?: number;
}

/**
 * 监控告警规则查询响应
 */
export interface AlarmRuleQueryResponse extends TableData {
  id: number;
  policyId: number;
  metricsId: number;
  metricsMeasurement: string;
  tags: string;
  ruleSwitch: number;
  allEffect: number;
  level: number;
  triggerCondition: string;
  threshold: any;
  consecutiveCount?: number;
  silencePeriod: number;
  description: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 创建监控告警规则
 */
export function createAlarmRule(request: AlarmRuleCreateRequest) {
  return axios.post<number>('/monitor/alarm-policy-rule/create', request);
}

/**
 * 更新监控告警规则
 */
export function updateAlarmRule(request: AlarmRuleUpdateRequest) {
  return axios.put<number>('/monitor/alarm-policy-rule/update', request);
}

/**
 * 更新监控告警规则
 */
export function updateAlarmRuleSwitch(request: AlarmRuleUpdateRequest) {
  return axios.put<number>('/monitor/alarm-policy-rule/update-switch', request);
}

/**
 * 查询全部监控告警规则
 */
export function getAlarmRuleList(policyId: number, measurement: string = '') {
  return axios.get<Array<AlarmRuleQueryResponse>>('/monitor/alarm-policy-rule/list', { params: { policyId, measurement } });
}

/**
 * 删除监控告警规则
 */
export function deleteAlarmRule(id: number) {
  return axios.delete<number>('/monitor/alarm-policy-rule/delete', { params: { id } });
}
