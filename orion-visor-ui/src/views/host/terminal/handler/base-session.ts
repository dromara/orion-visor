import type { ITerminalSession, TerminalPanelTabItem } from '../types/define';

// 会话基类
export default abstract class BaseSession implements ITerminalSession {

  public type: string;
  public hostId: number;
  public title: string;
  public address: string;
  public sessionId: string;
  public connected: boolean;
  public canReconnect: boolean;
  public canWrite: boolean;

  protected constructor(type: string, tab: TerminalPanelTabItem) {
    this.type = type;
    this.hostId = tab.hostId;
    this.title = tab.title;
    this.address = tab.address;
    this.sessionId = tab.sessionId;
    this.connected = false;
    this.canWrite = false;
    this.canReconnect = false;
  }

  // 设置是否可写
  setCanWrite(canWrite: boolean): void {
    this.canWrite = canWrite;
  }

  // 设置已连接
  setConnected(): void {
    this.connected = true;
  }

  // 连接会话
  connect(): void {
  }

  // 断开连接
  disconnect(): void {
    this.connected = false;
  }

  // 关闭
  close(): void {
    this.connected = false;
  }

}
