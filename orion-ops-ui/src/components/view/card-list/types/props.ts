import type { CSSProperties } from 'vue';
import type { PaginationProps, ResponsiveValue } from '@arco-design/web-vue';
import type { CardFieldConfig, CardPosition, CardRecord, ColResponsiveValue, HandleVisible } from '@/types/card';

// 卡片属性
export interface CardProps {
  key?: string;
  pagination?: PaginationProps | boolean;
  loading?: boolean;
  fieldConfig?: CardFieldConfig;
  cardHeight?: string;
  cardClass?: string;
  cardBodyStyle?: CSSProperties;
  contextMenu?: boolean;
  filterCount?: number;
  searchInputPlaceholder?: string;
  searchInputWidth?: string;
  searchValue?: string;
  createCardDescription?: string;
  createCardPosition?: CardPosition;
  addPermission?: Array<string>;
  cardLayoutGutter?: Number | ResponsiveValue | Array<Number> | Array<ResponsiveValue>;
  cardLayoutCols?: ColResponsiveValue;
  handleVisible?: HandleVisible;
  list?: Array<CardRecord>;
}
