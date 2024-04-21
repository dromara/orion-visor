import type { FieldRule } from '@arco-design/web-vue';

export const description = [{
  maxLength: 128,
  message: '执行描述长度不能大于128位'
}] as FieldRule[];

export const hostIdList = [{
  required: true,
  message: '请选择执行主机'
}] as FieldRule[];

export const command = [{
  required: true,
  message: '请输入执行命令'
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

export default {
  description,
  hostIdList,
  command,
  timeout,
  scriptExec,
} as Record<string, FieldRule | FieldRule[]>;
