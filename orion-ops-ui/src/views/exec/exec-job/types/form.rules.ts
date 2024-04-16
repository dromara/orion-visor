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
}, {
  type: 'number',
  min: 0,
  max: 100000,
  message: '超时时间需要在 0 - 100000 之间'
}] as FieldRule[];

export const scriptExec = [{
  required: true,
  message: '请选择是否使用脚本执行'
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
  scriptExec,
  command,
} as Record<string, FieldRule | FieldRule[]>;
