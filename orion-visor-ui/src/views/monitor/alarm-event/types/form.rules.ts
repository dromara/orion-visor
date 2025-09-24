import type { FieldRule } from '@arco-design/web-vue';

export const handleRules = {
  handleStatus: [{
    required: true,
    message: '请输入处理状态'
  }, {
    maxLength: 16,
    message: '处理状态长度不能大于16位'
  }],
  handleTime: [{
    required: true,
    message: '请输入处理时间'
  }],
  handleRemark: [{
    required: true,
    message: '请输入处理备注'
  }, {
    maxLength: 512,
    message: '处理备注长度不能大于512位'
  }],
} as Record<string, FieldRule | FieldRule[]>;
