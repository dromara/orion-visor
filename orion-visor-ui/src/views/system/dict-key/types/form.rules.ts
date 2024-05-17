import type { FieldRule } from '@arco-design/web-vue';

export const keyName = [{
  required: true,
  message: '请输入配置项'
}, {
  match: /^[a-zA-Z0-9_]{2,32}$/,
  message: '配置项需要为 2-32 位的数字,字母或下滑线'
}] as FieldRule[];

export const valueType = [{
  required: true,
  message: '请输入配置值类型'
}, {
  maxLength: 12,
  message: '配置值类型长度不能大于12位'
}] as FieldRule[];

export const description = [{
  required: true,
  message: '请输入配置描述'
}, {
  maxLength: 64,
  message: '配置描述长度不能大于64位'
}] as FieldRule[];

export default {
  keyName,
  valueType,
  description,
} as Record<string, FieldRule | FieldRule[]>;
