import type { FieldRule } from '@arco-design/web-vue';

export const parentId = [{
  required: true,
  message: '请选择父菜单'
}] as FieldRule[];

export const name = [{
  required: true,
  message: '请输入菜单名称'
}, {
  maxLength: 32,
  message: `菜单名称长度不能大于32位`
}] as FieldRule[];

export const type = [{
  required: true,
  message: '请选择菜单类型'
}] as FieldRule[];

export const sort = [{
  required: true,
  message: '请输入菜单排序'
}] as FieldRule[];

export const visible = [{
  required: true,
  message: '请选择是否可见'
}] as FieldRule[];

export const cache = [{
  required: true,
  message: '请选择是否缓存'
}] as FieldRule[];


export default {
  parentId,
  name,
  type,
  sort,
  visible,
  cache,
} as Record<string, FieldRule | FieldRule[]>;
