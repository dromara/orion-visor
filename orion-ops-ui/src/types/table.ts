import { PaginationProps, TableRowSelection } from '@arco-design/web-vue';
import { ColResponsiveValue } from '@/components/card-list/types';
import { reactive } from 'vue';

/**
 * FIXME DELETE
 * 默认分页
 */
export const defaultPagination = (): PaginationProps => {
  return {
    current: 1,
    pageSize: 10,
    showTotal: true,
    showPageSize: true,
    pageSizeOptions: [10, 20, 30, 50, 100]
  };
};

/**
 * FIXME DELETE
 * 默认行选择器
 */
export const defaultRowSelection = (): TableRowSelection => {
  return {
    type: 'checkbox',
    showCheckedAll: true,
    onlyCurrent: true,
  };
};

/**
 * 卡片列表列布局
 */
export const useCardColLayout = (): ColResponsiveValue => {
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
 * 创建列表分页
 */
export const usePagination = (): PaginationProps => {
  return reactive({
    total: 0,
    current: 1,
    pageSize: 10,
    showTotal: true,
    showPageSize: true,
    pageSizeOptions: [10, 20, 30, 50, 100]
  });
};

/**
 * 创建卡片列表分页
 */
export const useCardPagination = (): PaginationProps => {
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
 * 创建行选择器
 */
export const useRowSelection = (type = 'checkbox'): TableRowSelection => {
  return reactive({
    type: type as any,
    showCheckedAll: true,
    onlyCurrent: true,
  });
};
