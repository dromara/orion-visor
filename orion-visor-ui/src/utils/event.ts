// 添加事件监听器
import type { Ref } from 'vue';

export function addEventListen(
  target: Window | HTMLElement,
  event: string,
  handler: EventListenerOrEventListenerObject,
  capture = false
) {
  if (
    target.addEventListener &&
    typeof target.addEventListener === 'function'
  ) {
    target.addEventListener(event, handler, capture);
  }
}

// 移除事件监听器
export function removeEventListen(
  target: Window | HTMLElement,
  event: string,
  handler: EventListenerOrEventListenerObject,
  capture = false
) {
  if (
    target.removeEventListener &&
    typeof target.removeEventListener === 'function'
  ) {
    target.removeEventListener(event, handler, capture);
  }
}

// 触发鼠标事件
export const triggerMouseEvent = (ref: Ref, e = 'click') => {
  const event = new MouseEvent(e, {
    view: window,
    bubbles: true,
    cancelable: true,
  });
  ref.value.dispatchEvent(event);
};
