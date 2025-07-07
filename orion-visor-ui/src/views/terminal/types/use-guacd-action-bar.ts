import { ref } from 'vue';
import type { IGuacdSession } from '../interfaces';
import { fitDisplayValue } from './const';
import { getDisplaySize } from './utils';

// guacd 工具栏配置
export interface UseGuacdActionBarOptions {
  session: IGuacdSession;
  setVisible: (visible: boolean) => void;
}

// 使用主机配置表单
export default function useGuacdActionBar(options: UseGuacdActionBarOptions) {
  const { session, setVisible } = options;

  const displaySize = ref(fitDisplayValue);
  const clipboardData = ref('');

  // 临时自适应
  const fitOnce = () => {
    session.displayHandler?.fit(true);
    setVisible(false);
  };

  // 设置显示大小
  const setDisplaySize = () => {
    const displayHandler = session.displayHandler;
    if (!displayHandler) {
      return;
    }
    if (displaySize.value === fitDisplayValue) {
      // 设置自适应
      displayHandler.autoFit = true;
      displayHandler.fit(true);
    } else {
      try {
        // 获取大小
        const [width, height] = getDisplaySize(displaySize.value, true);
        // 取消自适应
        displayHandler.autoFit = false;
        // 设置大小
        displayHandler.resize(width, height);
      } catch (e) {
        return;
      }
    }
    setVisible(false);
  };

  // 触发组合键
  const triggerCombinationKey = (keys: Array<number>) => {
    session.sendKeys(keys);
    setVisible(false);
  };

  // 发送剪切板数据
  const sendClipboardData = () => {
    // 粘贴
    session.paste(clipboardData.value);
    setVisible(false);
  };

  // 清空剪切板数据
  const clearClipboardData = () => {
    clipboardData.value = '';
  };

  // 关闭会话
  const disconnect = () => {
    session.disconnect();
    setVisible(false);
  };

  return {
    displaySize,
    clipboardData,
    fitOnce,
    setDisplaySize,
    triggerCombinationKey,
    sendClipboardData,
    clearClipboardData,
    disconnect,
  };
}
