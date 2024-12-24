import type { VNodeRef } from 'vue';
import { nextTick } from 'vue';

// 设置 ref 自动聚焦
export const setAutoFocus: VNodeRef = ((el: HTMLElement) => {
  // 自动聚焦
  nextTick(() => {
    el && el.focus();
  });
}) as unknown as VNodeRef;
