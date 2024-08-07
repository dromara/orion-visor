import type { FieldRule } from '@arco-design/web-vue';

export const keyId = [{
  required: true,
  message: '请输入配置项id'
}] as FieldRule[];

export const keyName = [{
  required: true,
  message: '请输入配置项'
}, {
  maxLength: 32,
  message: '配置项长度不能大于32位'
}] as FieldRule[];

export const value = [{
  required: true,
  message: '请输入配置值'
}, {
  maxLength: 1024,
  message: '配置值长度不能大于1024位'
}] as FieldRule[];

export const label = [{
  required: true,
  message: '请输入配置描述'
}, {
  maxLength: 64,
  message: '配置描述长度不能大于64位'
}] as FieldRule[];

export const extra = [{
  required: true,
  message: '请输入额外参数'
}] as FieldRule[];

export const sort = [{
  required: true,
  message: '请输入排序'
}] as FieldRule[];

export default {
  keyId,
  keyName,
  value,
  label,
  extra,
  sort,
} as Record<string, FieldRule | FieldRule[]>;
