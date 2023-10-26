import type { FieldRule } from '@arco-design/web-vue';

export const username = [{
  required: true,
  message: '请输入用户名'
}, {
  maxLength: 32,
  message: '用户名长度不能大于32位'
}, {
  match: /^[a-zA-Z0-9_]{4,32}$/,
  message: '用户名需要为 4-32 位的数字,字母或下滑线'
}] as FieldRule[];

export const password = [{
  required: true,
  message: '请输入密码'
}, {
  minLength: 8,
  maxLength: 32,
  message: '密码长度需要在 8-32 位之间'
}] as FieldRule[];

export const nickname = [{
  required: true,
  message: '请输入花名'
}, {
  maxLength: 16,
  message: '花名长度不能大于16位'
}] as FieldRule[];

export const mobile = [{
  maxLength: 15,
  message: '手机号长度不能大于15位'
}] as FieldRule[];

export const email = [{
  maxLength: 64,
  message: '邮箱长度不能大于64位'
}] as FieldRule[];

export default {
  username,
  password,
  nickname,
  mobile,
  email,
} as Record<string, FieldRule | FieldRule[]>;
