import type { FieldRule } from '@arco-design/web-vue';

export const policyId = [{
  required: false,
  message: '请输入策略id'
}] as FieldRule[];

export const alarmSwitch = [{
  required: true,
  message: '请输入告警开关'
}] as FieldRule[];

export default {
  policyId,
  alarmSwitch,
} as Record<string, FieldRule | FieldRule[]>;
