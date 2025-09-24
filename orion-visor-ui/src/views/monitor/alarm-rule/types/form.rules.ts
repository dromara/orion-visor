import type { FieldRule } from '@arco-design/web-vue';

const rules = {
  policyId: [{
    required: true,
    message: '请输入策略id'
  }],
  metricsId: [{
    required: true,
    message: '请输入指标id'
  }],
  ruleSwitch: [{
    required: true,
    message: '请输入规则开关'
  }],
  level: [{
    required: true,
    message: '请输入告警级别'
  }],
  triggerCondition: [{
    required: true,
    message: '请输入告警条件'
  }, {
    maxLength: 8,
    message: '告警条件长度不能大于8位'
  }],
  threshold: [{
    required: true,
    message: '请输入触发阈值'
  }],
  silencePeriod: [{
    required: true,
    message: '请输入静默时间'
  }],
  consecutiveCount: [{
    required: true,
    message: '请输入持续数据点'
  }],
  description: [{
    maxLength: 255,
    message: '规则描述长度不能大于255位'
  }],
} as Record<string, FieldRule | FieldRule[]>;

export default rules;
