import type { Ref } from 'vue';
import { ref } from 'vue';
import type { OrderDirection } from '@/types/global';

// 查询排序 key
const QueryOrderKey = 'query-order';

// 排序
export const DESC = 0;
export const ASC = 1;

/**
 * 查询排序数据
 */
export interface QueryOrderData {
  order: Ref<number>;
  markOrderly: <T extends OrderDirection>(request: T) => T;
  saveConfig: (sort: number) => void;
}

/**
 * 使用查询排序
 */
export const useQueryOrder = (table: string, defaultSort: number = ASC): QueryOrderData => {
  const order = ref(defaultSort);

  // 刷新配置
  const refresh = () => {
    // 查询缓存
    let preferConfig: Record<string, string> = {};
    const localConfig = localStorage.getItem(QueryOrderKey);
    if (localConfig) {
      try {
        preferConfig = JSON.parse(localConfig as string);
      } catch (e) {
      }
    }
    // 解析配置
    const preferValue = Number.parseInt(preferConfig[table]);
    if (preferValue) {
      order.value = preferValue;
    }
  };

  const markOrderly = <T extends OrderDirection>(request: T) => {
    request.order = order.value;
    return request;
  };

  // 保存配置
  const saveConfig = (s: number) => {
    order.value = s;
    // 查询缓存
    let preferConfig: Record<string, any> = {};
    const localConfig = localStorage.getItem(QueryOrderKey);
    if (localConfig) {
      try {
        preferConfig = JSON.parse(localConfig as string);
      } catch (e) {
      }
    }
    // 设置缓存
    preferConfig[table] = s;
    localStorage.setItem(QueryOrderKey, JSON.stringify(preferConfig));
  };

  // 初始化
  refresh();

  return {
    order,
    markOrderly,
    saveConfig,
  };
};
