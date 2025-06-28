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
    const preference = useTerminalStore().preference;
    const graphSetting = preference.rdpGraphSetting;
    const sessionSetting = preference.rdpSessionSetting;
    const { data } = await getTerminalAccessToken({
      hostId: this.session.info.hostId,
      connectType: TerminalSessionTypes.RDP.type,
      extra: {
        enableAudioInput: sessionSetting.enableAudioInput,
        enableAudioOutput: sessionSetting.enableAudioOutput,
        driveMountMode: sessionSetting.driveMountMode,
        colorDepth: graphSetting.colorDepth || 16,
        forceLossless: graphSetting.forceLossless,
        enableWallpaper: graphSetting.enableWallpaper,
        enableTheming: graphSetting.enableTheming,
        enableFontSmoothing: graphSetting.enableFontSmoothing,
        enableFullWindowDrag: graphSetting.enableFullWindowDrag,
        enableDesktopComposition: graphSetting.enableDesktopComposition,
        enableMenuAnimations: graphSetting.enableMenuAnimations,
        disableBitmapCaching: graphSetting.disableBitmapCaching,
        disableOffscreenCaching: graphSetting.disableOffscreenCaching,
        disableGlyphCaching: graphSetting.disableGlyphCaching,
        disableGfx: graphSetting.disableGfx,
      }
    });
    // 打开 channel
    this.client = await openTerminalAccessChannel(TerminalSessionTypes.RDP.channel, data);
  }

  // 处理修改大小
  processResize({ width, height }: OutputPayload): void {
  }

}
