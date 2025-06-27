import type { ISshChannel, ISshSession } from '@/views/terminal/interfaces';
import type { OutputPayload } from '@/views/terminal/types/protocol';
import { TerminalCloseCode, TerminalMessages, TerminalSessionTypes } from '@/views/terminal/types/const';
import { ansi } from '@/utils';
import { useTerminalStore } from '@/store';
import { getTerminalAccessToken, openTerminalAccessChannel } from '@/api/terminal/terminal';
import BaseTerminalChannel from './base-terminal-channel';

// 终端通信会话 SSH 会话实现
export default class SshChannel extends BaseTerminalChannel<ISshSession> implements ISshChannel {

  // 打开 channel
  protected async openChannel(): Promise<void> {
    const { preference } = useTerminalStore();
    const { data } = await getTerminalAccessToken({
      hostId: this.session.info.hostId,
      connectType: TerminalSessionTypes.SSH.type,
      extra: {
        terminalType: preference.sessionSetting.terminalEmulationType ?? 'xterm',
      }
    });
    // 打开 channel
    this.client = await openTerminalAccessChannel(TerminalSessionTypes.SSH.channel, data);
  }

  // 处理已连接消息
  processConnected(_: OutputPayload): void {
    // 设置可写
    this.session.setCanWrite(true);
    // 设置已连接
    this.session.setConnected();
  }

  // 处理已关闭消息
  processClosed({ code, msg }: OutputPayload): void {
    if (this.triggerClosed) {
      return;
    }
    const beforeConnected = this.session.status.connected;
    this.triggerClosed = true;
    // 设置重连状态
    this.session.status.canReconnect = TerminalCloseCode.FORCE !== Number.parseInt(code);
    // 拼接关闭消息
    this.session.write((beforeConnected ? '\r\n\r\n' : '') + ansi(91, msg || ''));
    if (this.session.status.canReconnect) {
      this.session.write('\r\n' + ansi(91, TerminalMessages.waitingReconnect) + '\r\n');
    }
    // 设置已关闭
    this.session.setClosed();
    // 关闭 channel
    this.close();
  }

  // 处理修改大小
  processResize({ width, height }: OutputPayload): void {
    // this.session.resize(Number.parseInt(width), Number.parseInt(height));
  }

  // 处理 SSH 输出消息
  processSshOutput({ body }: OutputPayload): void {
    this.session.write(body);
  }

}
