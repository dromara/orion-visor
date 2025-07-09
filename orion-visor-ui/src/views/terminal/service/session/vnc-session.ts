import type { IVncSession, TerminalSessionTabItem, IGuacdSessionDisplayHandler, IGuacdChannel } from '@/views/terminal/interfaces';
import { fitDisplayValue } from '@/views/terminal/types/const';
import { useTerminalStore } from '@/store';
import BaseGuacdSession from './base-guacd-session';
import VncChannel from '../channel/vnc-channel';
import GuacdSessionDisplayHandler from '../handler/guacd-session-display-handler';

// VNC 会话实现
export default class VncSession extends BaseGuacdSession implements IVncSession {

  constructor(item: TerminalSessionTabItem) {
    super(item);
  }

  // 创建 channel
  protected createChannel(): IGuacdChannel {
    return new VncChannel(this);
  };

  // 创建 display
  protected createDisplay(): IGuacdSessionDisplayHandler {
    const vncGraphSetting = useTerminalStore().preference.vncGraphSetting;
    // 创建 display
    const displayHandler = new GuacdSessionDisplayHandler(this);
    // 设置自适应
    const autoFit = vncGraphSetting?.displaySize === fitDisplayValue;
    displayHandler.autoFit = autoFit;
    // 非自适应设置分辨率
    if (!autoFit) {
      displayHandler.setDisplaySize(vncGraphSetting?.displayWidth || 1024, vncGraphSetting?.displayHeight || 768);
    }
    return displayHandler;
  };

}
