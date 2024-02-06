import type { ISftpSession, ITerminalChannel } from '../types/terminal.type';
import { InputProtocol } from '../types/terminal.protocol';

// sftp 会话实现
export default class SftpSession implements ISftpSession {

  public readonly hostId: number;

  public sessionId: string;

  public connected: boolean;

  private readonly channel: ITerminalChannel;

  constructor(hostId: number,
              sessionId: string,
              channel: ITerminalChannel) {
    this.hostId = hostId;
    this.sessionId = sessionId;
    this.channel = channel;
    this.connected = false;
  }

  // 设置已连接
  connect(): void {
    this.connected = true;
  }

  // 断开连接
  disconnect(): void {
    // 发送关闭消息
    this.channel.send(InputProtocol.CLOSE, {
      sessionId: this.sessionId
    });
  }

  // 关闭
  close(): void {
  }

}
