import type { FieldRule } from '@arco-design/web-vue';

export const type = [{
  required: true,
  message: '请选择主机类型'
}] as FieldRule[];

export const osType = [{
  required: true,
  message: '请选择系统类型'
}] as FieldRule[];

export const name = [{
  required: true,
  message: '请输入主机名称'
}, {
  maxLength: 64,
  message: '主机名称长度不能大于64位'
}] as FieldRule[];

export const code = [{
  required: true,
  message: '请输入主机编码'
}, {
  maxLength: 64,
  message: '主机编码长度不能大于64位'
}] as FieldRule[];

export const address = [{
  required: true,
  message: '请输入主机地址'
}, {
  maxLength: 128,
  message: '主机地址长度不能大于128位'
}] as FieldRule[];

export const port = [{
  required: true,
  message: '请输入主机端口'
}, {
  type: 'number',
  min: 1,
  max: 65535,
  message: '输入的端口不合法'
}] as FieldRule[];

export const tags = [{
  maxLength: 5,
  message: '最多选择5个标签'
}] as FieldRule[];

export const description = [{
  maxLength: 255,
  message: '主机描述长度不能大于255位'
}] as FieldRule[];

export default {
  type,
  osType,
  name,
  code,
  address,
  port,
  tags,
  description,
} as Record<string, FieldRule | FieldRule[]>;
