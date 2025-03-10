import type { ITerminalSession, TerminalPanelTabItem, TerminalStatus } from '../types/define';
import type { Reactive } from 'vue';
import { reactive } from 'vue';
import { TerminalSessionStatus } from '@/views/host/terminal/types/const';

// 会话基类
export default abstract class BaseSession<Status extends TerminalStatus> implements ITerminalSession<Status> {

  public readonly type: string;
  public readonly hostId: number;
  public readonly title: string;
  public readonly address: string;
  public readonly status: Reactive<Status>;
  public sessionId: string;

  protected constructor(type: string, tab: TerminalPanelTabItem, status: Partial<Status>) {
    this.type = type;
    this.hostId = tab.hostId;
    this.title = tab.title;
    this.address = tab.address;
    this.sessionId = tab.sessionId;
    this.status = reactive({
      connectStatus: TerminalSessionStatus.CONNECTING,
      connected: false,
      canWrite: false,
      canReconnect: false,
      ...status,
    } as Status);
  }

  // 连接会话
  connect(): void {
    this.status.connectStatus = TerminalSessionStatus.CONNECTING;
  }

  // 断开连接
  disconnect(): void {
    // 设置已关闭
    this.setClosed();
  }

  // 关闭
  close(): void {
    // 设置已关闭
    this.setClosed();
  }

  // 设置是否可写
  setCanWrite(canWrite: boolean): void {
    this.status.canWrite = canWrite;
  }

  // 设置已连接
  setConnected(): void {
    this.status.connected = true;
    this.status.connectStatus = TerminalSessionStatus.CONNECTED;
  }

  // 设置已关闭
  setClosed(): void {
    this.status.connected = false;
    this.status.canWrite = false;
    this.status.connectStatus = TerminalSessionStatus.CLOSED;
  }

}
