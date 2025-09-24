import type { FieldRule } from '@arco-design/web-vue';

// 通知模板配置
const channelConfig = {
  webhook: [{
    required: true,
    message: '请输入 webhook'
  }],
  title: [{
    required: true,
    message: '请输入消息标题'
  }],
  messageClassify: [{
    required: true,
    message: '请选择消息分类'
  }],
  messageType: [{
    required: true,
    message: '请选择消息类型'
  }],
};

export default {
  name: [{
    required: true,
    message: '请输入通知名称'
  }, {
    maxLength: 32,
    message: '通知名称长度不能大于32位'
  }],
  bizType: [{
    required: true,
    message: '请输入业务类型'
  }, {
    maxLength: 12,
    message: '业务类型长度不能大于12位'
  }],
  channelType: [{
    required: true,
    message: '请输入渠道类型'
  }, {
    maxLength: 12,
    message: '渠道类型长度不能大于12位'
  }],
  messageTemplate: [{
    required: true,
    message: '请输入消息模板'
  }],
  description: [{
    maxLength: 255,
    message: '描述长度不能大于255位'
  }],
  ...channelConfig,
} as Record<string, FieldRule | FieldRule[]>;
