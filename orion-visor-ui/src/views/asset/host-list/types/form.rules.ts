import type { FieldRule } from '@arco-design/web-vue';

// 主机表单规则
export const hostFormRules = {
  types: [{
    required: true,
    message: '请选择主机协议'
  }],
  osType: [{
    required: true,
    message: '请选择系统类型'
  }],
  archType: [{
    required: true,
    message: '请选择系统架构'
  }],
  name: [{
    required: true,
    message: '请输入主机名称'
  }, {
    maxLength: 64,
    message: '主机名称长度不能大于64位'
  }],
  code: [{
    required: true,
    message: '请输入主机编码'
  }, {
    maxLength: 64,
    message: '主机编码长度不能大于64位'
  }],
  address: [{
    required: true,
    message: '请输入主机地址'
  }, {
    maxLength: 128,
    message: '主机地址长度不能大于128位'
  }],
  port: [{
    required: true,
    message: '请输入主机端口'
  }, {
    type: 'number',
    min: 1,
    max: 65535,
    message: '输入的端口不合法'
  }],
  groupIdList: [{
    required: true,
    message: '请选择主机分组'
  }],
  tags: [{
    maxLength: 5,
    message: '最多选择5个标签'
  }],
  description: [{
    maxLength: 255,
    message: '主机描述长度不能大于255位'
  }],
} as Record<string, FieldRule | FieldRule[]>;

// ssh 表单规则
export const sshFormRules = {
  port: [{
    required: true,
    message: '请输入 SSH 端口'
  }, {
    min: 1,
    max: 65535,
    message: 'SSH 端口不合法'
  }],
  authType: [{
    required: true,
    message: '请选择认证方式'
  }],
  keyId: [{
    required: true,
    message: '请选择主机密钥'
  }],
  identityId: [{
    required: true,
    message: '请选择主机身份'
  }],
  connectTimeout: [{
    required: true,
    message: '请输入连接超时时间'
  }, {
    type: 'number',
    min: 0,
    max: 100000,
    message: '连接超时时间需要在 0 - 100000 之间'
  }],
  charset: [{
    required: true,
    message: '请输入SSH输出编码'
  }, {
    maxLength: 12,
    message: 'SSH输出编码长度不能超过12位'
  }],
  fileNameCharset: [{
    required: true,
    message: '请输入文件名称编码'
  }, {
    maxLength: 12,
    message: '文件名称编码长度不能超过12位'
  }],
  fileContentCharset: [{
    required: true,
    message: '请输入SSH输出编码'
  }, {
    maxLength: 12,
    message: '文件内容编码长度不能超过12位'
  }],
} as Record<string, FieldRule | FieldRule[]>;

export default null;
