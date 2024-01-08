import {
  ITerminalChannel,
  ITerminalOutputProcessor,
  ITerminalSessionManager,
  OutputPayload
} from '../types/terminal.type';
import { InputProtocol } from '../types/terminal.protocol';
import { TerminalStatus } from '../types/terminal.const';

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
    // 未成功展示错误信息
    if (!success) {
      session.write(`[91m${msg || ''}[0m`);
      session.status = TerminalStatus.CLOSED;
      return;
    }
    // 发送 connect 命令
    this.channel.send(InputProtocol.CONNECT, { sessionId, cols: session.inst.cols, rows: session.inst.rows });
  }

  // 处理连接消息
  processConnect({ sessionId, result, msg }: OutputPayload): void {
    const success = !!Number.parseInt(result);
    const session = this.sessionManager.getSession(sessionId);
    // 未成功展示错误信息
    if (!success) {
      session.write(`[91m${msg || ''}[0m`);
      session.status = TerminalStatus.CLOSED;
      return;
    }
    // 设置可写
    session.setCanWrite(true);
    // 执行连接逻辑
    session.connect();
  }

  // 处理关闭消息
  processClose({ sessionId, msg }: OutputPayload): void {
    const session = this.sessionManager.getSession(sessionId);
    // 关闭 tab 则无需处理
    if (session) {
      // 提示消息
      session.write(`\r\n[91m${msg || ''}[0m`);
      // 设置状态
      session.status = TerminalStatus.CLOSED;
      session.connected = false;
      // 设置不可写
      session.setCanWrite(false);
    }
  }

  // 处理 pong 消息
  processPong(payload: OutputPayload): void {
    console.log('pong');
  }

  // 处理输出消息
  processOutput({ sessionId, body }: OutputPayload): void {
    const session = this.sessionManager.getSession(sessionId);
    session && session.write(body);
  }

}
