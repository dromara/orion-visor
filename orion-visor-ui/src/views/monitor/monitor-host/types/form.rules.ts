import type { FieldRule } from '@arco-design/web-vue';

const rules = {
  policyId: [{
    required: false,
    message: '请输入策略id'
  }],
  alarmSwitch: [{
    required: true,
    message: '请输入告警开关'
  }],
} as Record<string, FieldRule | FieldRule[]>;

export default rules;
