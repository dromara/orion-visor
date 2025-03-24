import type { PaginationProps, TableColumnData, TableExpandable, TableRowSelection } from '@arco-design/web-vue';
import type { Ref } from 'vue';
import { reactive, ref } from 'vue';
import { useAppStore } from '@/store';
import { isNumber } from '@/utils/is';
import { TablePageSizeOptions } from '@/types/const';

// 表格显示列 key
const TableColumnsKey = 'table-columns';

/**
 * 表格列数据
 */
export interface TableColumnsData {
  tableColumns: Ref<TableColumnData[]>;
  columnsHook: TableColumnsHook;
}

/**
 * 表格列操作
 */
export interface TableColumnsHook {
  getConfig: () => Array<string>;
  saveConfig: (config: Array<string> | undefined) => void;
}

/**
 * 创建列表分页
 */
export const useTablePagination = (ext?: PaginationProps): PaginationProps => {
  const appStore = useAppStore();
  return reactive({
    total: 0,
    current: 1,
    pageSize: isNumber(appStore.defaultTablePageSize) ? appStore.defaultTablePageSize : TablePageSizeOptions[0],
    showTotal: true,
    showPageSize: true,
    pageSizeOptions: TablePageSizeOptions,
    ...ext
  });
};

/**
 * 创建行选择器
 */
export const useRowSelection = (ext?: TableRowSelection): TableRowSelection => {
  return reactive({
    type: 'checkbox',
    showCheckedAll: true,
    onlyCurrent: true,
    ...ext
  });
};

/**
 * 创建表格展开
 */
export const useExpandable = (ext?: TableExpandable): TableExpandable => {
  return reactive({
    width: 50,
    fixed: true,
    ...ext
  });
};

/**
 * 使用表格列
 */
export const useTableColumns = (table: string, originColumns: Array<TableColumnData>): TableColumnsData => {
  const tableColumns = ref(originColumns);

  // 获取配置
  const getConfig = () => {
    // 查询缓存
    let preferConfig: Record<string, string[]> = {};
    const localConfig = localStorage.getItem(TableColumnsKey);
    if (localConfig) {
      try {
        preferConfig = JSON.parse(localConfig as string);
      } catch (e) {
      }
    }
    // 解析配置
    const preferValue = preferConfig[table];
    if (preferValue?.length) {
      // 配置值
      return preferValue;
    } else {
      // 默认值
      return originColumns.filter(s => s.default).map(s => (s.dataIndex || s.slotName) as string);
    }
  };

  // 保存配置
  const saveConfig = (config: Array<string> | undefined) => {
    // 查询缓存
    let preferConfig: Record<string, string[] | undefined> = {};
    const localConfig = localStorage.getItem(TableColumnsKey);
    if (localConfig) {
      try {
        preferConfig = JSON.parse(localConfig as string);
      } catch (e) {
      }
    }
    // 设置缓存
    preferConfig[table] = config;
    localStorage.setItem(TableColumnsKey, JSON.stringify(preferConfig));
    // 刷新
    refresh();
  };

  // 刷新
  const refresh = () => {
    const preferConfig = getConfig();
    tableColumns.value = originColumns.filter(s => preferConfig.includes((s.dataIndex || s.slotName) as string));
  };

  // 初始化
  refresh();

  return {
    tableColumns,
    columnsHook: {
      getConfig,
      saveConfig,
    }
  };
};
