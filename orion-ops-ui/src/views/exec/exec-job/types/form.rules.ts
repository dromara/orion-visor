import type { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入任务名称'
}, {
  maxLength: 64,
  message: '任务名称长度不能大于64位'
}] as FieldRule[];

export const hostIdList = [{
  required: true,
  message: '请选择执行主机'
}] as FieldRule[];

export const expression = [{
  required: true,
  message: '请输入 cron 表达式'
}, {
  maxLength: 512,
  message: 'cron 表达式长度不能大于512位'
}] as FieldRule[];

export const timeout = [{
  required: true,
  message: '请输入超时时间'
}] as FieldRule[];

export const command = [{
  required: true,
  message: '请输入执行命令'
}] as FieldRule[];

export default {
  name,
  hostIdList,
  expression,
  timeout,
  command,
} as Record<string, FieldRule | FieldRule[]>;
