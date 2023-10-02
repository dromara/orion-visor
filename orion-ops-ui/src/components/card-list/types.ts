import { ResponsiveValue } from '@arco-design/web-vue';

/**
 * 创建卡片位置
 */
export type Position = 'head' | 'tail' | false

/**
 * 卡片字段
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
