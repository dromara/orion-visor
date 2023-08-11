import { PaginationProps, TableRowSelection } from '@arco-design/web-vue';

/**
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
 * 默认行选择器
 */
export const defaultRowSelection = (): TableRowSelection => {
  return {
    type: 'checkbox',
    showCheckedAll: true,
    onlyCurrent: true,
  };
};
