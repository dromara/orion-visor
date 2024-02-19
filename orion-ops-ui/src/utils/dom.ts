import { nextTick } from 'vue';

// 设置 ref 自动聚焦
export const setAutoFocus = (el: HTMLElement) => {
  // 自动聚焦
  nextTick(() => {
    el && el.focus();
  });
};
