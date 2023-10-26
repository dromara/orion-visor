// 配置值类型定义
export const ValueType = {
  STRING: 'STRING',
  INTEGER: 'INTEGER',
  DECIMAL: 'DECIMAL',
  BOOLEAN: 'BOOLEAN',
  COLOR: 'COLOR',
};

// 快捷定义字段
export const definedExtraKeys = [
  {
    name: 'disabled',
    type: ValueType.BOOLEAN
  }, {
    name: 'status',
    type: ValueType.STRING
  }, {
    name: 'type',
    type: ValueType.STRING
  }, {
    name: 'color',
    type: ValueType.COLOR
  }
];

// 内置字段
export const innerKeys = ['value', 'label'];

// 菜单配置值类型 字典项
export const dictValueTypeKey = 'dictValueType';

// 加载的字典值
export const dictKeys = [dictValueTypeKey];

/**
 * 额外参数类型
 */
export interface ExtraParamType {
  name?: string;
  type?: string;
}
