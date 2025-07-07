import type { IVncSession } from '@/views/terminal/interfaces';
import type { OutputPayload } from '@/views/terminal/types/protocol';
import { useTerminalStore } from '@/store';
import { TerminalSessionTypes } from '@/views/terminal/types/const';
import { getTerminalAccessToken, openTerminalAccessChannel } from '@/api/terminal/terminal';
import BaseGuacdChannel from './base-guacd-channel';

// 终端通信会话 VNC 会话实现
export default class VncChannel extends BaseGuacdChannel<IVncSession> {

  // 打开 channel
  protected async openChannel(): Promise<void> {
    const setting = useTerminalStore().preference.vncGraphSetting;
    const { data } = await getTerminalAccessToken({
      hostId: this.session.info.hostId,
      connectType: TerminalSessionTypes.VNC.type,
      extra: {
        colorDepth: setting.colorDepth || 24,
        forceLossless: setting.forceLossless,
        swapRedBlue: setting.swapRedBlue,
        cursor: setting.cursor,
        compressLevel: setting.compressLevel,
        qualityLevel: setting.qualityLevel,
      }
    });
    // 打开 channel
    this.client = await openTerminalAccessChannel(TerminalSessionTypes.VNC.channel, data);
  }

  // 处理修改大小
  processResize({ width, height }: OutputPayload): void {
  }

}
