import { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入名称'
}, {
  maxLength: 64,
  message: '名称长度不能大于64位'
}] as FieldRule[];

export const publicKey = [{
  required: true,
  message: '请输入公钥文本'
}, {
  maxLength: 65535,
  message: '公钥文本长度不能大于65535位'
}] as FieldRule[];

export const privateKey = [{
  required: true,
  message: '请输入私钥文本'
}, {
  maxLength: 65535,
  message: '私钥文本长度不能大于65535位'
}] as FieldRule[];

export const password = [{
  required: true,
  message: '请输入密码'
}, {
  maxLength: 512,
  message: '密码长度不能大于512位'
}] as FieldRule[];

export default {
  name,
  publicKey,
  privateKey,
  password,
} as Record<string, FieldRule | FieldRule[]>;
