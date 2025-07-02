import type {
  GuacdInitConfig,
  IDomViewportHandler,
  IRdpSession,
  ISftpSession,
  ISftpSessionHandler,
  ISshSession,
  ITerminalSession,
  ITerminalSessionManager,
  SshInitConfig,
  TerminalSessionTabItem
} from '@/views/terminal/interfaces';
import { sleep } from '@/utils';
import { TerminalSessionTypes } from '../../types/const';
import { useDebounceFn } from '@vueuse/core';
import { addEventListen, removeEventListen } from '@/utils/event';
import SshSession from './ssh-session';
import SftpSession from './sftp-session';
import RdpSession from './rdp-session';

// 终端会话管理器实现
export default class TerminalSessionManager implements ITerminalSessionManager {

  public sessions: Array<ITerminalSession>;

  private readonly keepAliveTaskId: number;

  private readonly dispatchFitFn: () => void;

  constructor() {
    this.sessions = [];
    this.dispatchFitFn = useDebounceFn(this.dispatchFit, 300).bind(this);
    // 注册 resize 事件
    addEventListen(window, 'resize', this.dispatchFitFn);
    // 注册 ping 事件
    this.keepAliveTaskId = window.setInterval(this.dispatchPing.bind(this), 15000);
  }

  // 打开 ssh 会话
  async openSsh(item: TerminalSessionTabItem, config: SshInitConfig) {
    // 获取会话
    const session: ISshSession = this.getSession(item.key);
    try {
      // 初始化 session
      await session.init(config);
      // 等待前端渲染完成
      await sleep(100);
      // 连接会话
      session.connect();
    } catch (ex) {
      // 异常关闭
      session.close();
    }
  }

  // 打开 sftp 会话
  async openSftp(item: TerminalSessionTabItem, handler: ISftpSessionHandler): Promise<void> {
    // 获取会话
    const session: ISftpSession = this.getSession(item.key);
    try {
      // 初始化
      await session.init(handler);
      // 连接会话
      session.connect();
    } catch (ex) {
      // 异常关闭
      session.close();
    }
  }

  // 打开 rdp 会话
  async openRdp(item: TerminalSessionTabItem, config: GuacdInitConfig): Promise<void> {
    // 获取会话
    const session: IRdpSession = this.getSession(item.key);
    try {
      // 初始化 session
      await session.init(config);
      // 等待前端渲染完成
      await sleep(100);
      // 连接会话
      session.connect();
    } catch (ex) {
      console.error(ex);
      // 异常关闭
      session.close();
    }
  }

  // 重新打开会话
  async reOpenSession(sessionKey: string): Promise<void> {
    // 获取会话
    const session = this.getSession(sessionKey);
    if (session) {
      // 重新初始化
      await session.reInit();
      // 重新连接
      session.connect();
    }
  }

  // 创建会话
  createSession<T extends ITerminalSession>(item: TerminalSessionTabItem): T {
    let session;
    if (item.type === TerminalSessionTypes.SSH.type) {
      // SSH 会话
      session = new SshSession(item);
    } else if (item.type === TerminalSessionTypes.SFTP.type) {
      // SFTP 会话
      session = new SftpSession(item);
    } else if (item.type === TerminalSessionTypes.RDP.type) {
      // RDP 会话
      session = new RdpSession(item);
    } else {
      return undefined as unknown as T;
    }
    // 添加会话到上下文
    this.sessions.push(session);
    // 获取会话 这样可以获取到 session 的响应式 proxy 对象
    return this.sessions.find(s => s.sessionKey === item.key) as T;
  }

  // 获取终端会话
  getSession<T extends ITerminalSession>(sessionKey: string) {
    return this.sessions.find(s => s.sessionKey === sessionKey) as T;
  }

  // 关闭终端会话
  closeSession(sessionKey: string): void {
    // 查找会话
    const index = this.sessions.findIndex(s => s.sessionKey === sessionKey);
    if (index === -1) {
      return;
    }
    const session = this.sessions[index];
    // 关闭连接
    session.disconnect();
    // 关闭会话
    session.close();
    // 移除会话
    this.sessions.splice(index, 1);
  }

  // 调度 ping
  dispatchPing() {
    this.sessions.forEach(s => s.ping());
  }

  // 调度自适应
  dispatchFit() {
    this.sessions.forEach(s => (s as unknown as IDomViewportHandler)?.fit?.());
  }

  // 重置
  reset(): void {
    try {
      this.sessions = [];
      // 清除 ping 事件
      clearInterval(this.keepAliveTaskId);
      // 移除 resize 事件
      removeEventListen(window, 'resize', this.dispatchFitFn);
    } catch (e) {
    }
  }

}
