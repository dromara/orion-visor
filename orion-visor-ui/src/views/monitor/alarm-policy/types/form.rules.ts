import type { FieldRule } from '@arco-design/web-vue';

const rules = {
  name: [{
    required: true,
    message: '请输入策略名称'
  }, {
    maxLength: 64,
    message: '策略名称长度不能大于64位'
  }],
  description: [{
    maxLength: 255,
    message: '策略描述长度不能大于255位'
  }],
} as Record<string, FieldRule | FieldRule[]>;

export default rules;
