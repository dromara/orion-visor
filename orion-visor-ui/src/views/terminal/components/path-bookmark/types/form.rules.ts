import type { FieldRule } from '@arco-design/web-vue';

export const groupId = [{
  message: '请选择分组'
}] as FieldRule[];

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

export const path = [{
  required: true,
  message: '请输入路径'
}, {
  maxLength: 1000,
  message: '名称长度不能大于1000位'
}] as FieldRule[];

export default {
  groupId,
  name,
  type,
  path,
} as Record<string, FieldRule | FieldRule[]>;
