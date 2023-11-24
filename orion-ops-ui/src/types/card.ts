import type { PaginationProps, ResponsiveValue } from '@arco-design/web-vue';
import type { VNodeChild } from 'vue';
import { reactive } from 'vue';
import { useAppStore } from '@/store';
import { isNumber } from '@/utils/is';

/**
 * 字段对齐方式
 */
export type Align = 'left' | 'center' | 'right';

/**
 * 行对齐方式
 */
export type RowAlign = 'stretch' | 'center' | 'end' | 'start';

/**
 * 创建卡片位置
 */
export type CardPosition = 'head' | 'tail' | false;

/**
 * 卡片字段配置
 */
export interface CardFieldConfig {
  rowGap?: string;
  bodyClass?: string;
  showColon?: boolean;
  labelSpan?: number;
  labelOffset?: number;
  labelAlign?: Align;
  valueAlign?: Align;
  rowAlign?: RowAlign;
  height?: string;
  minHeight?: string;
  labelClass?: string;
  valueClass?: string;

  fields: CardField[];
}

/**
 * 卡片字段
 */
export interface CardField {
  label: string;
  dataIndex: string;
  slotName?: string;
  rowAlign?: RowAlign;
  height?: string;
  minHeight?: string;
  labelClass?: string;
  valueClass?: string;
  ellipsis?: boolean;
  tooltip?: boolean;
  render?: (data: {
    record: CardRecord;
    index: number;
  }) => VNodeChild;
}

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
  const appStore = useAppStore();
  return reactive({
    total: 0,
    current: 1,
    pageSize: isNumber(appStore.defaultCardSize) ? appStore.defaultCardSize : 12,
    showTotal: true,
    showPageSize: true,
    pageSizeOptions: [12, 18, 36, 48, 96]
  });
};
