import type { PaginationProps } from '@arco-design/web-vue';
import type { ColResponsiveValue } from '@/types/card';
import { reactive } from 'vue';
import { isNumber } from '@/utils/is';
import { useAppStore } from '@/store';
import { CardPageSizeOptions } from '@/types/const';

/**
 * 创建卡片列表列布局
 */
export const useCardColLayout = (): ColResponsiveValue => {
  return reactive({
    xs: 24,
    sm: 12,
    md: 8,
    lg: 8,
    xl: 8,
    xxl: 6,
  });
};

/**
 * 创建创建卡片列表分页
 */
export const useCardPagination = (): PaginationProps => {
  const appStore = useAppStore();
  return reactive({
    total: 0,
    current: 1,
    pageSize: isNumber(appStore.defaultCardPageSize) ? appStore.defaultCardPageSize : CardPageSizeOptions[0],
    showTotal: true,
    showPageSize: true,
    pageSizeOptions: CardPageSizeOptions
  });
};
