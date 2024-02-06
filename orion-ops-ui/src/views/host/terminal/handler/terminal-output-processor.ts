import { ISshSession, ITerminalChannel, ITerminalOutputProcessor, ITerminalSessionManager, OutputPayload } from '../types/terminal.type';
import { InputProtocol } from '../types/terminal.protocol';
import { TerminalStatus } from '../types/terminal.const';
import { useTerminalStore } from '@/store';
import { Message } from '@arco-design/web-vue';
import SshSession from './ssh-session';
import SftpSession from './sftp-session';

// 终端输出消息体处理器实现
export default class TerminalOutputProcessor implements ITerminalOutputProcessor {

  private readonly sessionManager: ITerminalSessionManager;

  private readonly channel: ITerminalChannel;

  constructor(sessionManager: ITerminalSessionManager, channel: ITerminalChannel) {
    this.sessionManager = sessionManager;
    this.channel = channel;
  }

  // 处理检查消息
  processCheck({ sessionId, result, msg }: OutputPayload): void {
    const success = !!Number.parseInt(result);
    const session = this.sessionManager.getSession(sessionId);
    if (session instanceof SshSession) {
      // ssh 会话
      if (success) {
        // 检查成功发送 connect 命令
        const { preference } = useTerminalStore();
        this.channel.send(InputProtocol.CONNECT, {
          sessionId,
          terminalType: preference.sessionSetting.terminalEmulationType || 'xterm',
          cols: session.inst.cols,
          rows: session.inst.rows
        });
      } else {
        // 未成功展示错误信息
        session.write(`[91m${msg || ''}[0m`);
        session.status = TerminalStatus.CLOSED;
      }
    } else if (session instanceof SftpSession) {
      // sftp 会话
      if (success) {
        // 检查成功发送 connect 命令
        // TODO

      } else {
        // 未成功提示错误信息
        Message.error(msg || '建立 SFTP 失败');
      }
    }
  }

  // 处理连接消息
  processConnect({ sessionId, result, msg }: OutputPayload): void {
    const success = !!Number.parseInt(result);
    const session = this.sessionManager.getSession(sessionId);
    if (session instanceof SshSession) {
      // ssh 会话
      if (success) {
        // 设置可写
        session.setCanWrite(true);
        // 执行连接逻辑
        session.connect();
      } else {
        // 未成功展示错误信息
        session.write(`[91m${msg || ''}[0m`);
        session.status = TerminalStatus.CLOSED;
      }
    } else if (session instanceof SftpSession) {
      // sftp 会话
      if (success) {
        // 执行连接逻辑
        session.connect();
      } else {
        // 未成功提示错误信息
        Message.error(msg || '打开 SFTP 失败');
      }
    }
  }

  // 处理关闭消息
  processClose({ sessionId, msg }: OutputPayload): void {
    const session = this.sessionManager.getSession(sessionId);
    // 无需处理 (直接关闭 tab )
    if (!session) {
      return;
    }
    if (session instanceof SshSession) {
      // ssh 拼接关闭消息
      session.write(`\r\n[91m${msg || ''}[0m`);
      // 设置状态
      session.status = TerminalStatus.CLOSED;
      session.connected = false;
      // 设置不可写
      session.setCanWrite(false);
    } else if (session instanceof SftpSession) {
      // sftp 设置状态
      session.connected = false;
    }
  }

  // 处理 pong 消息
  processPong(payload: OutputPayload): void {
    // console.log('pong');
  }

  // 处理输出消息
  processOutput({ sessionId, body }: OutputPayload): void {
    const session = this.sessionManager.getSession<ISshSession>(sessionId);
    session && session.write(body);
  }

}
