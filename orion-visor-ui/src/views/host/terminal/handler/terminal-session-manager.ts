import type {
  ISftpSession,
  ISftpSessionResolver,
  ITerminalChannel,
  ITerminalSession,
  ITerminalSessionManager,
  TerminalPanelTabItem,
  XtermDomRef
} from '../types/terminal.type';
import { sleep } from '@/utils';
import { InputProtocol } from '../types/terminal.protocol';
import { PanelSessionType } from '../types/terminal.const';
import { useDebounceFn } from '@vueuse/core';
import { addEventListen, removeEventListen } from '@/utils/event';
import TerminalChannel from './terminal-channel';
import SshSession from './ssh-session';
import SftpSession from './sftp-session';

// 终端会话管理器实现
export default class TerminalSessionManager implements ITerminalSessionManager {

  private readonly channel: ITerminalChannel;

  private sessions: Record<string, ITerminalSession>;

  private keepAliveTaskId?: any;

  private readonly dispatchResizeFn: () => {};

  constructor() {
    this.channel = new TerminalChannel(this);
    this.sessions = {};
    this.dispatchResizeFn = useDebounceFn(this.dispatchResize).bind(this);
  }

  // 打开 ssh 会话
  async openSsh(tab: TerminalPanelTabItem, domRef: XtermDomRef) {
    const sessionId = tab.sessionId;
    const hostId = tab.hostId as number;
    // 初始化客户端
    await this.initChannel();
    // 新建会话
    const session = new SshSession(
      hostId,
      sessionId,
      this.channel
    );
    // 初始化
    session.init(domRef);
    // 等待前端渲染完成
    await sleep(100);
    // 添加会话
    this.sessions[sessionId] = session;
    // 发送会话初始化请求
    this.channel.send(InputProtocol.CHECK, {
      sessionId,
      hostId,
      connectType: PanelSessionType.SSH.type
    });
    return session;
  }

  // 打开 sftp 会话
  async openSftp(tab: TerminalPanelTabItem, resolver: ISftpSessionResolver): Promise<ISftpSession> {
    const sessionId = tab.sessionId;
    const hostId = tab.hostId as number;
    // 初始化客户端
    await this.initChannel();
    // 新建会话
    const session = new SftpSession(
      hostId,
      sessionId,
      this.channel
    );
    // 初始化
    session.init(resolver);
    // 添加会话
    this.sessions[sessionId] = session;
    // 发送会话初始化请求
    this.channel.send(InputProtocol.CHECK, {
      sessionId,
      hostId,
      connectType: PanelSessionType.SFTP.type
    });
    return session;
  }

  // 重新打开会话
  async reOpenSession(type: string, sessionId: string, newSessionId: string): Promise<void> {
    // 初始化客户端
    await this.initChannel();
    // 获取会话并且重新设置 sessionId
    const session = this.sessions[sessionId] as ITerminalSession;
    session.sessionId = newSessionId;
    this.sessions[sessionId] = undefined as unknown as ITerminalSession;
    this.sessions[newSessionId] = session;
    // 发送会话初始化请求
    this.channel.send(InputProtocol.CHECK, {
      sessionId: newSessionId,
      hostId: session.hostId,
      connectType: type,
    });
  }

  // 获取终端会话
  getSession<T>(sessionId: string): T {
    return this.sessions[sessionId] as T;
  }

  // 关闭终端会话
  closeSession(sessionId: string): void {
    const session = this.sessions[sessionId];
    if (!session) {
      return;
    }
    // 关闭连接
    session.disconnect();
    // 关闭会话
    session.close();
    // 移除 session
    this.sessions[sessionId] = undefined as unknown as ITerminalSession;
    // session 全部关闭后 关闭 channel
    if (Object.values(this.sessions).filter(Boolean).every(s => !s?.connected)) {
      this.reset();
    }
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
    this.keepAliveTaskId = setInterval(() => {
      this.channel.send(InputProtocol.PING, {});
    }, 15000);
  }

  // 调度重置大小
  private dispatchResize() {
    // 对所有已连接的会话重置大小
    Object.values(this.sessions)
      .filter(s => s instanceof SshSession)
      .map(s => s as SshSession)
      .filter(h => h.connected)
      .forEach(h => h.fit());
  }

  // 重置
  reset(): void {
    try {
      this.sessions = {};
      // 关闭 channel
      this.channel.close();
      // 清除 ping 事件
      clearInterval(this.keepAliveTaskId);
      // 移除 resize 事件
      removeEventListen(window, 'resize', this.dispatchResizeFn);
    } catch (e) {
    }
  }

}
