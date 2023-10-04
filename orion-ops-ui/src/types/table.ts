import { PaginationProps, TableRowSelection } from '@arco-design/web-vue';
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
 * 创建行选择器
 */
export const useRowSelection = (type = 'checkbox'): TableRowSelection => {
  return reactive({
    type: type as any,
    showCheckedAll: true,
    onlyCurrent: true,
  });
};
