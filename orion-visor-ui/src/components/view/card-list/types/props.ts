import type { CSSProperties } from 'vue';
import type { PaginationProps, ResponsiveValue } from '@arco-design/web-vue';
import type { CardFieldsHook } from '@/hooks/card';
import type { QueryOrderData } from '@/hooks/query-order';
import type { CardFieldConfig, CardPosition, CardRecord, ColResponsiveValue, HandleVisible } from '@/types/card';

// 卡片属性
export interface CardProps {
  rowKey?: string;
  pagination?: PaginationProps | boolean;
  loading?: boolean;
  fieldConfig?: CardFieldConfig;
  cardHeight?: string;
  cardClass?: string;
  cardBodyStyle?: CSSProperties;
  filterCount?: number;
  searchInputPlaceholder?: string;
  searchInputWidth?: string;
  searchValue?: string;
  createCardDescription?: string;
  createCardPosition?: CardPosition;
  addPermission?: Array<string>;
  fieldsHook?: CardFieldsHook;
  queryOrder?: QueryOrderData;
  cardLayoutGutter?: number | ResponsiveValue | Array<number> | Array<ResponsiveValue>;
  cardLayoutCols?: ColResponsiveValue;
  handleVisible?: HandleVisible;
  list?: Array<CardRecord>;
}
