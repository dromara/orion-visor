import type { FieldRule } from '@arco-design/web-vue';

export const logId = [{
  required: true,
  message: '请输入执行日志id'
}] as FieldRule[];

export const hostId = [{
  required: true,
  message: '请输入主机id'
}] as FieldRule[];

export const hostName = [{
  required: true,
  message: '请输入主机名称'
}, {
  maxLength: 128,
  message: '主机名称长度不能大于128位'
}] as FieldRule[];

export const status = [{
  required: true,
  message: '请输入执行状态'
}, {
  maxLength: 12,
  message: '执行状态长度不能大于12位'
}] as FieldRule[];

export const command = [{
  required: true,
  message: '请输入执行命令'
}] as FieldRule[];

export const parameter = [{
  required: true,
  message: '请输入执行参数'
}] as FieldRule[];

export const exitStatus = [{
  required: true,
  message: '请输入退出码'
}] as FieldRule[];

export const logPath = [{
  required: true,
  message: '请输入日志路径'
}, {
  maxLength: 512,
  message: '日志路径长度不能大于512位'
}] as FieldRule[];

export const errorMessage = [{
  required: true,
  message: '请输入错误信息'
}, {
  maxLength: 255,
  message: '错误信息长度不能大于255位'
}] as FieldRule[];

export const startTime = [{
  required: true,
  message: '请输入执行开始时间'
}] as FieldRule[];

export const finishTime = [{
  required: true,
  message: '请输入执行结束时间'
}] as FieldRule[];

export default {
  logId,
  hostId,
  hostName,
  status,
  command,
  parameter,
  exitStatus,
  logPath,
  errorMessage,
  startTime,
  finishTime,
} as Record<string, FieldRule | FieldRule[]>;
