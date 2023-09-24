import { useClipboard } from '@vueuse/core';
import { Message } from '@arco-design/web-vue';

export default function useCopy() {
  const { isSupported, copy: c, text, copied } = useClipboard();
  const copy = async (value: string, tips = `${ value } 已复制`) => {
    try {
      await c(value);
      if (tips) {
        Message.success(tips);
      }
    } catch {
      Message.error('复制失败');
    }
  };
  return {
    isSupported,
    copy,
    text,
    copied
  };
}


