import { FieldRule } from '@arco-design/web-vue';

export const username = [{
  required: true,
  message: '请输入用户名'
}, {
  maxLength: 32,
  message: '用户名长度不能大于32位'
}] as FieldRule[];

export const password = [{
  required: true,
  message: '请输入密码'
}, {
  maxLength: 64,
  message: '密码长度不能大于64位'
}] as FieldRule[];

export const nickname = [{
  required: true,
  message: '请输入花名'
}, {
  maxLength: 16,
  message: '花名长度不能大于16位'
}] as FieldRule[];

export const avatar = [{
  required: true,
  message: '请输入头像地址'
}, {
  maxLength: 500,
  message: '头像地址长度不能大于500位'
}] as FieldRule[];

export const mobile = [{
  required: true,
  message: '请输入手机号'
}, {
  maxLength: 15,
  message: '手机号长度不能大于15位'
}] as FieldRule[];

export const email = [{
  required: true,
  message: '请输入邮箱'
}, {
  maxLength: 64,
  message: '邮箱长度不能大于64位'
}] as FieldRule[];

export const status = [{
  required: true,
  message: '请输入用户状态 0停用 1启用 2锁定'
}] as FieldRule[];

export const lastLoginTime = [{
  required: true,
  message: '请输入最后登录时间'
}] as FieldRule[];

export default {
  username,
  password,
  nickname,
  avatar,
  mobile,
  email,
  status,
  lastLoginTime,
} as Record<string, FieldRule | FieldRule[]>;
