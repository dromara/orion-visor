import type { CSSProperties } from 'vue';
import type { PaginationProps, ResponsiveValue } from '@arco-design/web-vue';
import type { CardFieldConfig, CardPosition, CardRecord, ColResponsiveValue, HandleVisible } from '@/types/card';

// 卡片属性
export interface CardProps {
  key?: string;
  pagination?: PaginationProps | boolean;
  loading?: boolean;
  fieldConfig?: CardFieldConfig;
  cardHeight: string;
  cardClass?: string;
  cardBodyStyle?: CSSProperties;
  contextMenu?: boolean;
  filterCount?: number;
  searchInputPlaceholder?: string;
  searchInputWidth?: string;
  searchValue?: string;
  createCardDescription?: string;
  createCardPosition?: CardPosition | boolean;
  addPermission?: Array<string>;
  cardLayoutGutter?: Number | ResponsiveValue | Array<Number> | Array<ResponsiveValue>;
  cardLayoutCols?: ColResponsiveValue;
  handleVisible?: HandleVisible;
  list?: Array<CardRecord>;
}

// 定义默认 props
export const cardDefaultProps = () => {
  return {
    key: 'id',
    pagination: false,
    loading: false,
    cardHeight: '100%',
    contextMenu: true,
    filterCount: 0,
    searchInputWidth: '200px',
    searchValue: '',
    createCardDescription: '点击此处进行创建',
    createCardPosition: false,
    addPermission: () => [],
    cardLayoutGutter: () => [16, 16],
    cardLayoutCols: () => {
      return {
        span: 6
      };
    },
    handleVisible: () => {
      return {};
    },
    list: () => []
  };
};

