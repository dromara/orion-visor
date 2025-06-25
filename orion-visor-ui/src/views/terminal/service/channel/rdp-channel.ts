import type { IRdpSession } from '@/views/terminal/interfaces';
import type { OutputPayload } from '@/views/terminal/types/protocol';
import { useTerminalStore } from '@/store';
import { TerminalSessionTypes } from '@/views/terminal/types/const';
import { getTerminalAccessToken, openTerminalAccessChannel } from '@/api/terminal/terminal';
import BaseGuacdChannel from './base-guacd-channel';

// 终端通信会话 Rdp 会话实现
export default class RdpChannel extends BaseGuacdChannel<IRdpSession> {

  // 打开 channel
  protected async openChannel(): Promise<void> {
    const setting = useTerminalStore().preference.rdpGraphSetting;
    const { data } = await getTerminalAccessToken({
      hostId: this.session.info.hostId,
      connectType: TerminalSessionTypes.RDP.type,
      extra: {
        enableAudioInput: setting.enableAudioInput,
        enableAudioOutput: setting.enableAudioOutput,
        colorDepth: setting.colorDepth || 16,
        forceLossless: setting.forceLossless,
        enableWallpaper: setting.enableWallpaper,
        enableTheming: setting.enableTheming,
        enableFontSmoothing: setting.enableFontSmoothing,
        enableFullWindowDrag: setting.enableFullWindowDrag,
        enableDesktopComposition: setting.enableDesktopComposition,
        enableMenuAnimations: setting.enableMenuAnimations,
        disableBitmapCaching: setting.disableBitmapCaching,
        disableOffscreenCaching: setting.disableOffscreenCaching,
        disableGlyphCaching: setting.disableGlyphCaching,
      }
    });
    // 打开 channel
    this.client = await openTerminalAccessChannel(TerminalSessionTypes.RDP.channel, data);
  }

  // 处理修改大小
  processResize({ width, height }: OutputPayload): void {
  }

}
