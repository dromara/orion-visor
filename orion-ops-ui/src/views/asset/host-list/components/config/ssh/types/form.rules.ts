import type { FieldRule } from '@arco-design/web-vue';

export const port = [{
  required: true,
  message: '请输入SSH端口'
}, {
  type: 'number',
  min: 1,
  max: 65535,
  message: '输入的端口不合法'
}] as FieldRule[];

export const authType = [{
  required: true,
  message: '请选择认证方式'
}] as FieldRule[];

export const keyId = [{
  required: true,
  message: '请选择主机秘钥'
}] as FieldRule[];

export const identityId = [{
  required: true,
  message: '请选择主机身份'
}] as FieldRule[];

export const connectTimeout = [{
  required: true,
  message: '请输入连接超时时间'
}, {
  type: 'number',
  min: 0,
  max: 100000,
  message: '连接超时时间需要在 0 - 100000 之间'
}] as FieldRule[];

export const charset = [{
  required: true,
  message: '请输入SSH输出编码'
}, {
  maxLength: 12,
  message: 'SSH输出编码长度不能超过12位'
}] as FieldRule[];

export const fileNameCharset = [{
  required: true,
  message: '请输入文件名称编码'
}, {
  maxLength: 12,
  message: '文件名称编码长度不能超过12位'
}] as FieldRule[];

export const fileContentCharset = [{
  required: true,
  message: '请输入SSH输出编码'
}, {
  maxLength: 12,
  message: '文件内容编码长度不能超过12位'
}] as FieldRule[];

export default {
  port,
  authType,
  keyId,
  identityId,
  connectTimeout,
  charset,
  fileNameCharset,
  fileContentCharset,
} as Record<string, FieldRule | FieldRule[]>;
