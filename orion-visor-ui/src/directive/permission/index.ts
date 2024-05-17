import type { DirectiveBinding } from 'vue';
import usePermission from '@/hooks/permission';

function checkPermission(el: HTMLElement, binding: DirectiveBinding) {
  const { value } = binding;
  const permission = usePermission();
  if (Array.isArray(value)) {
    if (value.length > 0) {
      if (!permission.hasAnyPermission(value) && el.parentNode) {
        el.parentNode.removeChild(el);
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
