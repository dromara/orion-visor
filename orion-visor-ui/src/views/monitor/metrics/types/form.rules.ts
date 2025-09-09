import type { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入指标名称'
}, {
  maxLength: 64,
  message: '指标名称长度不能大于64位'
}] as FieldRule[];

export const measurement = [{
  required: true,
  message: '请输入数据集'
}, {
  maxLength: 64,
  message: '数据集长度不能大于64位'
}] as FieldRule[];

export const value = [{
  required: true,
  message: '请输入指标项'
}, {
  maxLength: 128,
  message: '指标项长度不能大于128位'
}] as FieldRule[];

export const unit = [{
  required: true,
  message: '请选择单位'
}] as FieldRule[];

export const suffix = [{
  required: true,
  message: '请输入后缀文本'
}, {
  maxLength: 32,
  message: '后缀文本长度不能大于32位'
}] as FieldRule[];

export const description = [{
  maxLength: 128,
  message: '指标描述长度不能大于128位'
}] as FieldRule[];

export default {
  name,
  measurement,
  value,
  unit,
  suffix,
  description,
} as Record<string, FieldRule | FieldRule[]>;
