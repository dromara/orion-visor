import type {
  InputPayload,
  ITerminalChannel,
  ITerminalOutputProcessor,
  OutputPayload,
  Protocol,
} from '../types/terminal.type';
import { OutputProtocol } from '../types/terminal.protocol';
import { getHostTerminalAccessToken } from '@/api/asset/host-terminal';
import { Message } from '@arco-design/web-vue';
import { sleep } from '@/utils';

export const wsBase = import.meta.env.VITE_WS_BASE_URL;

// 终端通信处理器 实现
export default class TerminalChannel implements ITerminalChannel {

  private client?: WebSocket;

  private readonly processor;

  constructor(processor: ITerminalOutputProcessor) {
    this.processor = processor;
  }

  // 初始化
  async init() {
    // 获取 access
    const { data: accessToken } = await getHostTerminalAccessToken();
    // 打开会话
    this.client = new WebSocket(`${wsBase}/host/terminal/${accessToken}`);
    this.client.onerror = event => {
      Message.error('无法连接至服务器');
      console.error('error', event);
    };
    this.client.onclose = event => {
      console.warn('close', event);
    };
    this.client.onmessage = this.handlerMessage.bind(this);
    // 等待会话连接
    for (let i = 0; i < 100; i++) {
      await sleep(50);
      if (this.client.readyState !== WebSocket.CONNECTING) {
        break;
      }
    }
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
    // 消息调度
    switch (payload.type) {
      case OutputProtocol.CHECK.type:
        // 检查 回调
        this.processor.processCheck.call(this.processor, payload);
        break;
      case OutputProtocol.CONNECT.type:
        // 连接 回调
        this.processor.processConnect.call(this.processor, payload);
        break;
      case OutputProtocol.PONG.type:
        // pong 回调
        this.processor.processPong.call(this.processor, payload);
        break;
      case OutputProtocol.OUTPUT.type:
        // 输出 回调
        this.processor.processOutput.call(this.processor, payload);
        break;
      default:
        break;
    }
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

// 分隔符
export const SEPARATOR = '|';

// 解析参数
export const parse = (payload: string) => {
  const protocols = Object.values(OutputProtocol);
  const useProtocol = protocols.find(p => payload.startsWith(p.type + SEPARATOR) || p.type === payload);
  if (!useProtocol) {
    return undefined;
  }
  const template = useProtocol.template;
  const res = {} as OutputPayload;
  let curr = 0;
  let len = payload.length;
  for (let i = 0, pl = template.length; i < pl; i++) {
    if (i == pl - 1) {
      // 最后一次
      res[template[i]] = payload.substring(curr, len);
    } else {
      // 非最后一次
      let tmp = '';
      for (; curr < len; curr++) {
        const c = payload.charAt(curr);
        if (c == SEPARATOR) {
          res[template[i]] = tmp;
          curr++;
          break;
        } else {
          tmp += c;
        }
      }
    }
  }
  return res;
};

// 格式化参数
export const format = (protocol: Protocol, payload: InputPayload) => {
  payload.type = protocol.type;
  return protocol.template
    .map(i => payload[i] || '')
    .join(SEPARATOR);
};
