import type { PaginationProps, TableRowSelection } from '@arco-design/web-vue';
import { reactive } from 'vue';
import { useAppStore } from '@/store';
import { isNumber } from '@/utils/is';
import { TablePageSizeOptions } from '@/types/const';

/**
 * 创建列表分页
 */
export const usePagination = (): PaginationProps => {
  const appStore = useAppStore();
  return reactive({
    total: 0,
    current: 1,
    pageSize: isNumber(appStore.defaultTablePageSize) ? appStore.defaultTablePageSize : TablePageSizeOptions[0],
    showTotal: true,
    showPageSize: true,
    pageSizeOptions: TablePageSizeOptions
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
