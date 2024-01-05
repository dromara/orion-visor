import type { ITerminalChannel, ITerminalSession } from '../types/terminal.type';
import type { TerminalTabItem } from '@/store/modules/terminal/types';
import { sleep } from '@/utils';
import TerminalSession from './terminal-session';

// 终端会话管理器定义
export interface ITerminalSessionManager {
  // 打开终端
  openSession: (tab: TerminalTabItem, dom: HTMLElement) => void;
  // 获取终端会话
  getSession: (sessionId: string) => ITerminalSession;
  // 重置
  reset: () => void;
}

// FIXME 去除 TOKEN 起始量

// 终端会话管理器实现
export default class TerminalSessionManager implements ITerminalSessionManager {

  private readonly channel: ITerminalChannel;

  private sessions: Record<string, ITerminalSession>;

  constructor(channel: ITerminalChannel) {
    this.channel = channel;
    this.sessions = {};
  }

  // 打开终端会话
  async openSession(tab: TerminalTabItem, dom: HTMLElement) {
    // 初始化客户端
    await this.channel.init();
    // 新建会话
    const session = new TerminalSession(
      tab.hostId as number,
      tab.key,
      this.channel
    );
    // 初始化
    session.init(dom);
    // 等待前端渲染完成
    await sleep(100);
    // 添加会话
    this.sessions[tab.key] = session;
  }

  // 获取终端会话
  getSession(sessionId: string): ITerminalSession {
    return this.sessions[sessionId];
  }

  // 重置
  reset(): void {
    this.sessions = {};
  }

}
