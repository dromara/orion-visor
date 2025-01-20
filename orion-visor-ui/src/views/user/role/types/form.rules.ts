import type { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入角色名称'
}, {
  maxLength: 32,
  message: '角色名称长度不能大于32位'
}] as FieldRule[];

export const code = [{
  required: true,
  message: '请输入角色编码'
}, {
  maxLength: 32,
  message: '角色编码长度不能大于32位'
}] as FieldRule[];

export const description = [{
  maxLength: 255,
  message: '角色描述长度不能大于255位'
}] as FieldRule[];

export default {
  name,
  code,
  description,
} as Record<string, FieldRule | FieldRule[]>;
