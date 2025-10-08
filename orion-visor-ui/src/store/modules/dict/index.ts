import type { RadioOption } from '@arco-design/web-vue/es/radio/interface';
import type { DictState } from './types';
import type { Options } from '@/types/global';
import { defineStore } from 'pinia';
import { getDictValueList } from '@/api/system/dict-value';
import { isArray } from '@/utils/is';

const AllOption: Options = { label: '全部', value: '' };

// 获取拼接的选项
const getPrependOptions = (perpendOption: boolean | Options | Options[] | RadioOption | RadioOption[]): Options[] => {
  if (!perpendOption) {
    return [];
  }
  if (perpendOption === true) {
    // 默认选项
    return [{ ...AllOption }];
  } else if (isArray(perpendOption)) {
    // 数组选项
    return perpendOption.map(s => {
      return { ...s } as Options;
    });
  } else {
    // 单一选项
    return [{ ...perpendOption as Options }];
  }
};

export default defineStore('dict', {
  state: (): DictState => ({}),

  actions: {
    // 加载字典值
    async loadKeys(keys: string[]) {
      // 检查是否存在
      const unloadKeys = keys.filter(key => !this.$state.hasOwnProperty(key));
      if (!unloadKeys.length) {
        return;
      }
      // 加载未加载的数据
      try {
        const { data } = await getDictValueList(unloadKeys);
        this.$patch(data as object);
      } catch (e) {
      } finally {
      }
    },

    // 获取字典选项
    toOptions(key: string, perpendOption: boolean | Options | Options[] = false): Options[] {
      let perpendOptions = getPrependOptions(perpendOption);
      const options = this.$state[key] ?? [];
      return [...perpendOptions, ...options];
    },

    // 转为 unref 的字典选项
    toUnrefOptions(key: string, perpendOption: boolean | Options | Options[] = false): Options[] {
      return this.toOptions(key, perpendOption)
        .map(s => {
          return { ...s };
        });
    },

    // 获取字典选项
    toRadioOptions(key: string, perpendOption: boolean | RadioOption | RadioOption[] = false): RadioOption[] {
      let perpendOptions = getPrependOptions(perpendOption);
      const options = this.$state[key] ?? [];
      return [...perpendOptions, ...options] as RadioOption[];
    },

    // 转为 unref 的字典选项
    toUnrefRadioOptions(key: string, perpendOption: boolean | RadioOption | RadioOption[] = false): RadioOption[] {
      return this.toRadioOptions(key, perpendOption)
        .map(s => {
          return { ...s };
        });
    },

    // 获取字典值
    getDictValue(dict: string,
                 value: any,
                 key = 'label',
                 defaultValue = value) {
      for (let dictValue of this.$state[dict] || []) {
        if (dictValue.value === value) {
          return dictValue[key];
        }
      }
      return defaultValue;
    },

    // 获取字典值对象
    getDict(dict: string, value: any): Options {
      for (let dictValue of this.$state[dict] || []) {
        if (dictValue.value === value) {
          return dictValue;
        }
      }
      return {
        value
      } as Options;
    },

    // 切换字典值
    toggleDictValue(dict: string,
                    value: any,
                    key = 'value',
                    defaultValue = value) {
      for (let dictValue of this.$state[dict] || []) {
        if (dictValue.value !== value) {
          return dictValue[key];
        }
      }
      return defaultValue;
    },

    // 切换字典值对象
    toggleDict(dict: string, value: any): Options {
      for (let dictValue of this.$state[dict] || []) {
        if (dictValue.value !== value) {
          return dictValue;
        }
      }
      return {} as Options;
    }

  },
});
