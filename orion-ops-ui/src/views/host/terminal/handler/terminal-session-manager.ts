import type {
  ITerminalChannel,
  ITerminalSession,
  ITerminalSessionManager,
  ITerminalOutputProcessor,
  OutputPayload,
  TerminalTabItem
} from '../types/terminal.type';
import { sleep } from '@/utils';
import { InputProtocol } from '../types/terminal.protocol';
import TerminalSession from './terminal-session';
import { useDebounceFn } from '@vueuse/core';
import TerminalChannel from '@/views/host/terminal/handler/terminal-channel';
import { addEventListen, removeEventListen } from '@/utils/event';

// 终端会话管理器实现
export default class TerminalSessionManager implements ITerminalSessionManager, ITerminalOutputProcessor {

  private readonly channel: ITerminalChannel;

  private sessions: Record<string, ITerminalSession>;

  private keepAliveTask?: any;

  private readonly dispatchResizeFn: () => {};

  constructor() {
    this.channel = new TerminalChannel(this);
    this.sessions = {};
    this.dispatchResizeFn = useDebounceFn(this.dispatchResize).bind(this);
  }

  // 打开终端会话
  async openSession(tab: TerminalTabItem, dom: HTMLElement) {
    const sessionId = tab.key;
    const hostId = tab.hostId as number;
    // 初始化客户端
    await this.initChannel();
    // 新建会话
    const session = new TerminalSession(
      hostId,
      sessionId,
      this.channel
    );
    // 初始化
    session.init(dom);
    // 等待前端渲染完成
    await sleep(100);
    // 添加会话
    this.sessions[sessionId] = session;
    // 发送会话初始化请求
    this.channel.send(InputProtocol.CHECK, {
      session: sessionId,
      hostId: hostId
    });
  }

  // 初始化 channel
  private async initChannel() {
    // 检查 channel 是否已经初始化
    if (this.channel.isConnected()) {
      return;
    }
    // 初始化 channel
    await this.channel.init();
    // 注册 resize 事件
    addEventListen(window, 'resize', this.dispatchResizeFn);
    // 注册 ping 事件
    this.keepAliveTask = setInterval(() => {
      this.channel.send(InputProtocol.PING, {});
    }, 15000);
  }

  // 获取终端会话
  getSession(sessionId: string): ITerminalSession {
    return this.sessions[sessionId];
  }

  // 关闭终端会话
  closeSession(sessionId: string): void {
    // 发送关闭消息
    this.channel?.send(InputProtocol.CLOSE, { session: sessionId });
    // 关闭 session
    const session = this.sessions[sessionId];
    if (session) {
      session.close();
    }
    // 移除 session
    this.sessions[sessionId] = undefined as unknown as ITerminalSession;
    // session 全部关闭后 关闭 channel
    if (Object.values(this.sessions).filter(Boolean).every(s => !s?.connected)) {
      this.reset();
    }
  }

  // 调度重置大小
  private dispatchResize() {
    // 对所有已连接的会话重置大小
    Object.values(this.sessions)
      .filter(h => h.connected)
      .forEach(h => h.fit());
  }

  // 处理检查消息
  processCheck({ session: sessionId, result, errorMessage }: OutputPayload): void {
    const success = !!Number.parseInt(result);
    const session = this.sessions[sessionId];
    // 未成功展示错误信息
    if (!success) {
      session.write('[91m' + errorMessage + '[0m');
      return;
    }
    // 发送 connect 命令
    this.channel.send(InputProtocol.CONNECT, { session: sessionId, cols: session.inst.cols, rows: session.inst.rows });
  }

  // 处理连接消息
  processConnect({ session: sessionId, result, errorMessage }: OutputPayload): void {
    const success = !!Number.parseInt(result);
    const session = this.sessions[sessionId];
    // 未成功展示错误信息
    if (!success) {
      session.write('[91m' + errorMessage + '[0m');
      return;
    }
    // 设置可写
    session.setCanWrite(true);
    // 执行连接逻辑
    session.connect();
  }

  // 处理 pong 消息
  processPong(payload: OutputPayload): void {
    console.log('pong');
  }

  // 处理输出消息
  processOutput({ session: sessionId, body }: OutputPayload): void {
    const session = this.sessions[sessionId];
    session && session.write(body);
  }

  // 重置
  reset(): void {
    this.sessions = {};
    // 关闭 channel
    this.channel.close();
    // 清除 ping 事件
    if (this.keepAliveTask) {
      clearInterval(this.keepAliveTask);
      this.keepAliveTask = undefined;
    }
    // 移除 resize 事件
    removeEventListen(window, 'resize', this.dispatchResizeFn);
  }

}
