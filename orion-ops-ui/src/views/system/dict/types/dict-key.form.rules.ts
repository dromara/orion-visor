import { FieldRule } from '@arco-design/web-vue';

export const keyName = [{
  required: true,
  message: '请输入配置项'
}, {
  maxLength: 32,
  message: '配置项长度不能大于32位'
}] as FieldRule[];

export const valueType = [{
  required: true,
  message: '请输入配置值定义'
}, {
  maxLength: 12,
  message: '配置值定义长度不能大于12位'
}] as FieldRule[];

export const extraSchema = [{
  required: true,
  message: '请输入额外配置定义'
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
  extraSchema,
  description,
} as Record<string, FieldRule | FieldRule[]>;
