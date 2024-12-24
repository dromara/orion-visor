import type { DirectiveBinding } from 'vue';
import usePermission from '@/hooks/permission';

function checkPermission(el: HTMLElement, binding: DirectiveBinding) {
  const { value } = binding;
  const permission = usePermission();
  if (Array.isArray(value)) {
    if (value.length > 0) {
      const parentNode = el.parentNode as Element;
      if (!permission.hasAnyPermission(value) && parentNode) {
        if (parentNode.classList.contains('arco-space-item')) {
          // 如果是 arco-space-item 则移除父节点
          parentNode.parentNode?.removeChild(parentNode);
        } else {
          // 只移除当前元素
          parentNode.removeChild(el);
        }
      }
    }
  } else {
    throw new Error(`need permission! Like v-permission="['permission']"`);
  }
}

export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding);
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding);
  },
};
