import type { PaginationProps, TableRowSelection } from '@arco-design/web-vue';
import { reactive } from 'vue';
import { useAppStore } from '@/store';
import { isNumber } from '@/utils/is';

/**
 * 创建列表分页
 */
export const usePagination = (): PaginationProps => {
  const appStore = useAppStore();
  return reactive({
    total: 0,
    current: 1,
    pageSize: isNumber(appStore.defaultPageSize) ? appStore.defaultPageSize : 12,
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
