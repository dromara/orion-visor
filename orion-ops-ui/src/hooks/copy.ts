import { useClipboard } from '@vueuse/core';
import { Message } from '@arco-design/web-vue';

export default function useCopy() {
  const { copy: c } = useClipboard();
  // 复制
  const copy = async (value: string | undefined, tips = `${value} 已复制`) => {
    try {
      if (!value) {
        return;
      }
      await c(value);
      if (tips) {
        Message.success(tips);
      }
    } catch (e) {
      Message.error('复制失败');
    }
  };
  // 获取剪切板内容
  const readText = () => {
    return navigator.clipboard.readText();
  };
  return {
    copy,
    readText,
  };
}
