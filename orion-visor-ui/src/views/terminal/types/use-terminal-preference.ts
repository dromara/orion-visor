import { ref, watch } from 'vue';
import { useDebounceFn } from '@vueuse/core';
import { useTerminalStore } from '@/store';
import { isArray, isObject } from '@/utils/is';

const { preference, updateTerminalPreference } = useTerminalStore();

// 更新终端偏好
export default function useTerminalPreference<T>(key: string,
                                                 setLocal = false,
                                                 filler?: (v: T, before: T) => void,
                                                 delay = 500) {
  // 默认配置
  let initialValue = preference[key as keyof typeof preference] as T;
  if (isArray(initialValue)) {
    initialValue = [...initialValue] as T;
  } else if (isObject(initialValue)) {
    initialValue = { ...initialValue } as T;
  }
  const formModel = ref<T>(initialValue);

  // 防抖更新
  const debouncedUpdate = useDebounceFn(async (value: T) => {
    // 更新
    await updateTerminalPreference(key, value, setLocal);
  }, delay);

  // 监听表单
  watch(formModel, async (v, before) => {
    if (!v) {
      return;
    }    // 填充
    filler?.(v, before);
    // 更新
    await debouncedUpdate(v);
  }, { deep: true });

  return {
    formModel,
  };
}
