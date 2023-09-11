import { useClipboard } from '@vueuse/core';
import { Message } from '@arco-design/web-vue';

export default function useCopy() {
  const { isSupported, copy: c, text, copied } = useClipboard();
  const copy = (value: string, tips = `${value} 已复制`) => {
    return c(value)
    .then(() => {
      if (tips) {
        Message.success(tips);
      }
    }).catch(() => {
      Message.error('复制失败');
    });
  };
  return {
    isSupported,
    copy,
    text,
    copied
  };
}


