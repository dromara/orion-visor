import type { Ref } from 'vue';
import { reactive, ref } from 'vue';
import type { PaginationProps } from '@arco-design/web-vue';
import type { ColResponsiveValue, CardFieldConfig, CardField } from '@/types/card';
import { isNumber } from '@/utils/is';
import { useAppStore } from '@/store';
import { CardPageSizeOptions } from '@/types/const';

// 卡片显示字段 key
const CardFieldsKey = 'card-fields';

/**
 * 卡片字段配置数据
 */
export interface CardFieldConfigData {
  cardFieldConfig: Ref<CardFieldConfig>;
  fieldsHook: CardFieldsHook;
}

/**
 * 卡片字段配置操作
 */
export interface CardFieldsHook {
  originFields: Array<CardField>;
  getConfig: () => Array<string>;
  saveConfig: (config: Array<string> | undefined) => void;
}

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

/**
 * 使用卡片字段配置
 */
export const useCardFieldConfig = (table: string, originFieldConfig: CardFieldConfig): CardFieldConfigData => {
  const cardFieldConfig = ref(originFieldConfig);

  // 获取配置
  const getConfig = () => {
    // 查询缓存
    let preferConfig: Record<string, string[]> = {};
    const localConfig = localStorage.getItem(CardFieldsKey);
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
      return originFieldConfig.fields.filter(s => s.default).map(s => (s.dataIndex || s.slotName) as string);
    }
  };

  // 保存配置
  const saveConfig = (config: Array<string> | undefined) => {
    // 查询缓存
    let preferConfig: Record<string, string[] | undefined> = {};
    const localConfig = localStorage.getItem(CardFieldsKey);
    if (localConfig) {
      try {
        preferConfig = JSON.parse(localConfig as string);
      } catch (e) {
      }
    }
    // 设置缓存
    preferConfig[table] = config;
    localStorage.setItem(CardFieldsKey, JSON.stringify(preferConfig));
    // 刷新
    refresh();
  };

  // 刷新
  const refresh = () => {
    const preferConfig = getConfig();
    cardFieldConfig.value = {
      ...originFieldConfig,
      fields: originFieldConfig.fields.filter(s => preferConfig.includes((s.dataIndex || s.slotName) as string)),
    };
  };

  // 初始化
  refresh();

  return {
    cardFieldConfig,
    fieldsHook: {
      originFields: originFieldConfig.fields,
      getConfig,
      saveConfig,
    }
  };
};
