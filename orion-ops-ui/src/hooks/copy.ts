import { Message } from '@arco-design/web-vue';

// 复制
export const copy = async (value: string | undefined, tips: string | boolean = `${value} 已复制`) => {
  try {
    if (!value) {
      return;
    }
    await copyToClipboard(value);
    if (tips) {
      Message.success(tips as string);
    }
  } catch (e) {
    Message.error('复制失败');
  }
};

// 获取剪切板内容
export const readText = () => {
  if (navigator.clipboard) {
    return navigator.clipboard.readText();
  } else {
    return new Promise<string>((resolve, reject) => {
      const textarea = document.createElement('textarea');
      textarea.style.position = 'absolute';
      textarea.style.left = '-9999px';
      document.body.appendChild(textarea);
      textarea.select();
      try {
        const success = document.execCommand('paste');
        if (!success) {
          Message.error('当前环境无法读取剪切板内容');
        }
        resolve(textarea.value);
      } catch (error) {
        reject(error);
      } finally {
        document.body.removeChild(textarea);
      }
    });
  }
};

// 复制到剪切板
export const copyToClipboard = async (value: string) => {
  if (navigator.clipboard) {
    await navigator.clipboard.writeText(value);
  } else {
    const textarea = document.createElement('textarea');
    textarea.textContent = value;
    textarea.style.position = 'absolute';
    textarea.style.left = '-9999px';
    document.body.appendChild(textarea);
    textarea.select();
    try {
      const success = document.execCommand('copy');
      if (!success) {
        Message.error('当前环境无法复制到剪切板');
      }
    } catch (error) {
      throw error;
    } finally {
      document.body.removeChild(textarea);
    }
  }
};

export default function useCopy() {
  return {
    copy,
    readText,
  };
}
