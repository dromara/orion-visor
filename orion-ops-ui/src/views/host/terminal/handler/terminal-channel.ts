import { OutputProtocol } from '@/views/host/terminal/types/terminal.protocol';
import type { InputPayload, ITerminalChannel, ITerminalOutputProcessor, OutputPayload, Protocol, } from '@/views/host/terminal/types/terminal.type';


export const wsBase = import.meta.env.VITE_WS_BASE_URL;

// 终端通信处理器 实现
export default class TerminalChannel implements ITerminalChannel {

  private readonly processor;

  constructor(processor: ITerminalOutputProcessor) {
    this.processor = processor;
  }

  send(protocol: Protocol, payload: InputPayload): void {

  }

  // 初始化
  async init() {
  }

  // 处理消息
  handlerMessage({ data }: MessageEvent) {
    // 解析消息
    const payload = parse(data as string);
    if (!payload) {
      return;
    }
    // 消息调度
    switch (payload.type) {
      case OutputProtocol.CHECK.type:
        // 检查 回调
        this.processor.processCheck(payload);
        break;
      case OutputProtocol.CONNECT.type:
        // 连接 回调
        this.processor.processConnect(payload);
        break;
      case OutputProtocol.PONG.type:
        // pong 回调
        this.processor.processPong(payload);
        break;
      case OutputProtocol.OUTPUT.type:
        // 输出 回调
        this.processor.processOutput(payload);
        break;
      default:
        break;
    }
  }

  close(): void {
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
