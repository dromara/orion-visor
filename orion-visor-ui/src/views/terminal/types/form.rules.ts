import type { FieldRule } from '@arco-design/web-vue';

// 代码片段规则
export const commandSnippetFormRules = {
  groupId: [{
    message: '请选择分组'
  }],
  name: [{
    required: true,
    message: '请输入名称'
  }, {
    maxLength: 64,
    message: '名称长度不能大于64位'
  }],
  command: [{
    required: true,
    message: '请输入代码片段'
  }],
} as Record<string, FieldRule | FieldRule[]>;

// 书签路径规则
export const bookmarkFormRules = {
  groupId: [{
    message: '请选择分组'
  }],
  name: [{
    required: true,
    message: '请输入名称'
  }, {
    maxLength: 64,
    message: '名称长度不能大于64位'
  }],
  type: [{
    required: true,
    message: '请选择类型'
  }],
  path: [{
    required: true,
    message: '请输入路径'
  }, {
    maxLength: 1000,
    message: '路径长度不能大于1000位'
  }],
} as Record<string, FieldRule | FieldRule[]>;
