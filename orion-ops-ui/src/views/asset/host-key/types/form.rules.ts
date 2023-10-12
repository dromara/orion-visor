import { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入名称'
}, {
  maxLength: 64,
  message: '名称长度不能大于64位'
}] as FieldRule[];

export const privateKey = [{
  required: true,
  message: '请输入私钥文本'
}] as FieldRule[];

export default {
  name,
  privateKey,
} as Record<string, FieldRule | FieldRule[]>;
