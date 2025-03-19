import type { ResponsiveValue } from '@arco-design/web-vue';
import type { VNodeChild } from 'vue';

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
  default: boolean;
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
