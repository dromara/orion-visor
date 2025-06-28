import type { Reactive } from 'vue';
import { reactive } from 'vue';
import type { ITerminalChannel, ITerminalSession, ReactiveSessionState, SessionHostInfo, TerminalSessionTabItem } from '@/views/terminal/interfaces';
import { TerminalStatus } from '@/views/terminal/types/const';

// 会话基类
export default abstract class BaseSession<State extends ReactiveSessionState, Channel extends ITerminalChannel>
  implements ITerminalSession<State> {

  public readonly type: string;
  public readonly info: SessionHostInfo;
  public readonly panelIndex: number;
  public readonly state: Reactive<State>;
  public readonly sessionKey: string;
  public sessionId: string;
  protected channel: Channel;

  protected constructor(item: TerminalSessionTabItem, state: Partial<State>) {
    this.type = item.type;
    this.info = {
      hostId: item.hostId,
      title: item.title,
      name: item.name,
      address: item.address,
    } as SessionHostInfo;
    this.panelIndex = item.panelIndex;
    this.sessionKey = item.key;
    this.sessionId = item.key;
    this.state = reactive({
      connectStatus: TerminalStatus.CONNECTING,
      connected: false,
      canWrite: false,
      canReconnect: false,
      ...state,
    } as State);
    this.channel = undefined as unknown as Channel;
  }

  // 重新初始化
  abstract reInit(): Promise<void> ;

  // 连接会话
  abstract connect(): void;

  // 断开连接
  disconnect(): void {
    // 设置已关闭
    this.setClosed();
    // 关闭 channel
    this.channel?.close();
  }

  // 关闭
  close(): void {
    this.disconnect();
  }

  // ping
  ping(): void {
    this.channel?.ping();
  }

  // 设置是否可写
  setCanWrite(canWrite: boolean): void {
    this.state.canWrite = canWrite;
  }

  // 设置连接中
  setConnecting(): void {
    this.state.connected = false;
    this.state.canWrite = false;
    this.state.connectStatus = TerminalStatus.CONNECTING;
  }

  // 设置已连接
  setConnected(): void {
    // 设置状态
    this.state.connected = true;
    this.state.connectStatus = TerminalStatus.CONNECTED;
  }

  // 设置已关闭
  setClosed(): void {
    this.state.connected = false;
    this.state.canWrite = false;
    this.state.connectStatus = TerminalStatus.CLOSED;
  }

}
