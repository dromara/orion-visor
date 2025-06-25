import type { ITerminalChannel, ITerminalSession } from '@/views/terminal/interfaces';
import type { InputPayload, OutputPayload, Protocol } from '@/views/terminal/types/protocol';
import { format, InputProtocol, OutputProtocol, parse } from '@/views/terminal/types/protocol';
import { TerminalMessages, TerminalCloseCode } from '../../types/const';
import { Message } from '@arco-design/web-vue';

// 终端通信处理器 实现
export default abstract class BaseTerminalChannel<T extends ITerminalSession> implements ITerminalChannel {

  protected client?: WebSocket;

  protected session: T;

  protected triggerClosed: boolean;

  constructor(session: T) {
    this.session = session;
    this.triggerClosed = false;
  }

  // 打开 channel
  protected abstract openChannel(): Promise<void>;

  // 初始化
  async init() {
    // 打开 channel
    try {
      await this.openChannel();
    } catch (e) {
      Message.error('无法连接至服务器');
      console.error('terminal error', e);
      throw e;
    }
    if (this.client) {
      // 处理关闭事件
      this.client.onclose = this.handleClientClose.bind(this);
      // 处理消息
      this.client.onmessage = this.handleClientMessage.bind(this);
    }
  }

  // 发送消息
  send(protocol: Protocol, payload?: InputPayload): void {
    // 检查是否已开启
    if (!this.isOpened()) {
      return;
    }
    // 发送命令
    if (payload) {
      this.client?.send(format(protocol, payload));
    } else {
      this.client?.send(protocol.type);
    }
  }

  // ping
  ping(): void {
    this.send(InputProtocol.PING);
  }

  // 处理设置id
  processSetId({ sessionId }: OutputPayload): void {
    // 设置 sessionId
    this.session.sessionId = sessionId;
  }

  // 处理设置信息
  processSetInfo({ info }: OutputPayload) {
    const data = JSON.parse(info);
    if (data) {
      this.session.info.address = data.address;
      this.session.info.port = data.port;
      this.session.info.username = data.username;
    }
  };

  // 处理已连接消息
  abstract processConnected(_: OutputPayload): void;

  // 处理已关闭消息
  abstract processClosed(payload: OutputPayload): void;

  // 处理修改大小
  processResize(_: OutputPayload) {
  };

  // 处理 pong 消息
  processPong(_: OutputPayload) {
  }

  // 处理客户端消息
  protected handleClientMessage(event: MessageEvent) {
    // 解析消息
    const payload = parse(event.data as string);
    if (!payload) {
      return;
    }
    // 获取消息处理方法
    const processMethod = Object.values(OutputProtocol)
      .find(protocol => protocol.type === payload.type)
      ?.processMethod;
    //  处理消息
    if (processMethod) {
      const processMethodFn = this[processMethod as keyof ITerminalChannel] as Function;
      processMethodFn && processMethodFn.call(this, payload);
    }
  }

  // 处理客户端关闭
  protected handleClientClose(event: CloseEvent) {
    console.warn('channel closed', event);
    // 关闭后手动触发关闭消息 - 错误兜底
    this.processClosed({
      type: OutputProtocol.CLOSED.type,
      code: TerminalCloseCode.NORMAL + '',
      msg: event.reason || TerminalMessages.sessionClosed,
    });
  }

  // 是否已开启
  isOpened(): boolean {
    return !!this.client && this.client.readyState === WebSocket.OPEN;
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
