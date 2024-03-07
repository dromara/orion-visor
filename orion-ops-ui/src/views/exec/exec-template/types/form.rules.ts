import type { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入名称'
}, {
  maxLength: 64,
  message: '名称长度不能大于64位'
}] as FieldRule[];

export const command = [{
  required: true,
  message: '请输入命令'
}] as FieldRule[];

export const timeout = [{
  required: true,
  message: '请输入超时时间秒 0不超时'
}] as FieldRule[];

export const parameter = [{
  required: true,
  message: '请输入参数'
}] as FieldRule[];

export default {
  name,
  command,
  timeout,
  parameter,
} as Record<string, FieldRule | FieldRule[]>;
