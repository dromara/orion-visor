import type { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入名称'
}, {
  maxLength: 64,
  message: '名称长度不能大于64位'
}] as FieldRule[];

export const type = [{
  required: true,
  message: '请选择类型'
}] as FieldRule[];

export const keyId = [{
  required: true,
  message: '请选择密钥'
}] as FieldRule[];

export const username = [{
  required: true,
  message: '请输入用户名'
}, {
  maxLength: 128,
  message: '用户名长度不能大于128位'
}] as FieldRule[];

export const description = [{
  maxLength: 255,
  message: '描述长度不能大于255位'
}] as FieldRule[];

export default {
  name,
  type,
  keyId,
  username,
  description,
} as Record<string, FieldRule | FieldRule[]>;
