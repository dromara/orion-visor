import { DescData, PaginationProps, ResponsiveValue } from '@arco-design/web-vue';
import { reactive } from 'vue';

/**
 * 卡片字段
 */
export interface CardField {
  field: string;
  label: string;
  render: (record: CardRecord) => string;
  span?: number;
}

/**
 * 创建卡片位置
 */
export type Position = 'head' | 'tail' | false;

/**
 * 卡片实体
 */
export interface CardRecord {
  disabled?: boolean;

  [name: string]: any;
}

/**
 * col 响应式值
 */
export interface ColResponsiveValue extends ResponsiveValue {
  span?: number;
  offset?: number;
  order?: number;
}

/**
 * 显示的操作
 */
export interface HandleVisible {
  disableAdd?: boolean;
  disableSearchInput?: boolean;
  disableFilter?: boolean;
  disableSearch?: boolean;
  disableReset?: boolean;
}

/**
 * 创建卡片列表列布局
 */
export const useColLayout = (): ColResponsiveValue => {
  return {
    xs: 24,
    sm: 12,
    md: 8,
    lg: 8,
    xl: 6,
    xxl: 4,
  };
};

/**
 * 创建创建卡片列表分页
 */
export const usePagination = (): PaginationProps => {
  return reactive({
    total: 0,
    current: 1,
    pageSize: 18,
    showTotal: true,
    showPageSize: true,
    pageSizeOptions: [12, 18, 36, 48, 96]
  });
};

/**
 * 转为卡片描述对象
 */
export const toCardDesc = (fields: CardField[], record: CardRecord): DescData[] => {
  return fields.map(s => {
    return {
      field: s.field,
      label: s.label,
      value: s.render(record),
      span: s.span || 3
    };
  });
};
