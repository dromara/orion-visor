import type { FieldRule } from '@arco-design/web-vue';

export const userId = [{
  required: true,
  message: '请输入执行用户id'
}] as FieldRule[];

export const username = [{
  required: true,
  message: '请输入执行用户名'
}, {
  maxLength: 32,
  message: '执行用户名长度不能大于32位'
}] as FieldRule[];

export const source = [{
  required: true,
  message: '请输入执行来源'
}, {
  maxLength: 12,
  message: '执行来源长度不能大于12位'
}] as FieldRule[];

export const sourceId = [{
  required: true,
  message: '请输入执行来源id'
}] as FieldRule[];

export const description = [{
  required: true,
  message: '请输入执行描述'
}, {
  maxLength: 128,
  message: '执行描述长度不能大于128位'
}] as FieldRule[];

export const command = [{
  required: true,
  message: '请输入执行命令'
}] as FieldRule[];

export const timeout = [{
  required: true,
  message: '请输入超时时间'
}] as FieldRule[];

export const status = [{
  required: true,
  message: '请输入执行状态'
}, {
  maxLength: 12,
  message: '执行状态长度不能大于12位'
}] as FieldRule[];

export const startTime = [{
  required: true,
  message: '请输入执行开始时间'
}] as FieldRule[];

export const finishTime = [{
  required: true,
  message: '请输入执行完成时间'
}] as FieldRule[];

export default {
  userId,
  username,
  source,
  sourceId,
  description,
  command,
  timeout,
  status,
  startTime,
  finishTime,
} as Record<string, FieldRule | FieldRule[]>;
