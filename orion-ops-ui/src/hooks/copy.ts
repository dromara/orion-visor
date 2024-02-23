import { useClipboard } from '@vueuse/core';
import { Message } from '@arco-design/web-vue';

const { copy: c } = useClipboard();

// 复制
export const copy = async (value: string | undefined, tips: string | boolean = `${value} 已复制`) => {
  try {
    if (!value) {
      return;
    }
    await c(value);
    if (tips) {
      Message.success(tips as string);
    }
  } catch (e) {
    Message.error('复制失败');
  }
};

// 获取剪切板内容
export const readText = () => {
  return navigator.clipboard.readText();
};

export default function useCopy() {
  return {
    copy,
    readText,
  };
}
