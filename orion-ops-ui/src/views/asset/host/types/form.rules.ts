import { FieldRule } from '@arco-design/web-vue';

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

export default {
  name,
  code,
  address,
} as Record<string, FieldRule | FieldRule[]>;
