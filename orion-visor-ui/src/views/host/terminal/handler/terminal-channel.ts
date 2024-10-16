import type { ITerminalChannel, ITerminalOutputProcessor, ITerminalSessionManager } from '../types/define';
import type { InputPayload, Protocol } from '@/types/protocol/terminal.protocol';
import { format, OutputProtocol, parse } from '@/types/protocol/terminal.protocol';
import { sessionCloseMsg } from '../types/const';
import { getTerminalAccessToken, openTerminalAccessChannel } from '@/api/asset/terminal';
import { Message } from '@arco-design/web-vue';
import TerminalOutputProcessor from './terminal-output-processor';

// 终端通信处理器 实现
export default class TerminalChannel implements ITerminalChannel {

  private client?: WebSocket;

  private readonly sessionManager: ITerminalSessionManager;

  private readonly processor: ITerminalOutputProcessor;

  constructor(sessionManager: ITerminalSessionManager) {
    this.sessionManager = sessionManager;
    this.processor = new TerminalOutputProcessor(sessionManager, this);
  }

  // 初始化
  async init() {
    // 获取 access
    const { data: accessToken } = await getTerminalAccessToken();
    // 打开会话
    try {
      this.client = await openTerminalAccessChannel(accessToken);
    } catch (e) {
      Message.error('无法连接至服务器');
      console.error('terminal error', e);
      throw e;
    }
    this.client.onclose = event => {
      console.warn('terminal close', event);
      // 关闭回调
      this.closeCallback();
    };
    this.client.onmessage = this.handlerMessage.bind(this);
  }

  // 是否已连接
  isConnected(): boolean {
    return !!this.client && this.client.readyState === WebSocket.OPEN;
  }

  // 发送消息
  send(protocol: Protocol, payload: InputPayload): void {
    // 检查是否连接
    if (!this.isConnected()) {
      return;
    }
    // 发送命令
    this.client?.send(format(protocol, payload));
  }

  // 处理消息
  private handlerMessage({ data }: MessageEvent) {
    // 解析消息
    const payload = parse(data as string);
    if (!payload) {
      return;
    }
    // 获取消息处理方法
    const processMethod = Object.values(OutputProtocol)
      .find(protocol => protocol.type === payload.type)
      ?.processMethod;
    //  处理消息
    if (processMethod) {
      const processMethodFn = this.processor[processMethod as keyof ITerminalOutputProcessor] as Function;
      processMethodFn && processMethodFn.call(this.processor, payload);
    }
  }

  // 关闭回调
  private closeCallback(): void {
    // 关闭时将手动触发 close 消息, 有可能是其他原因关闭的, 没有接收到 close 消息, 导致已断开是终端还是显示已连接
    Object.values(this.sessionManager.sessions).forEach(s => {
      if (!s?.connected) {
        return;
      }
      // close 消息
      const data = format(OutputProtocol.CLOSE, {
        type: OutputProtocol.CLOSE.type,
        sessionId: s.sessionId,
        forceClose: 0,
        msg: sessionCloseMsg,
      });
      // 触发 close 消息
      this.handlerMessage({ data } as MessageEvent);
    });
  }

  // 关闭
  close(): void {
    // 关闭 client
    if (this.client) {
      if (this.client.readyState === WebSocket.OPEN) {
        this.client.close();
      }
      this.client = undefined;
    }
  }

}
