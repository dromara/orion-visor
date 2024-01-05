import { ITerminalChannel, ITerminalOutputProcessor, OutputPayload, } from '@/views/host/terminal/types/terminal.type';
import TerminalChannel from '@/views/host/terminal/handler/terminal-channel';

// 终端调度器实现
export default class TerminalOutputProcessor implements ITerminalOutputProcessor {

  private readonly channel: ITerminalChannel;

  constructor() {
    this.channel = new TerminalChannel(this);
  }

  // 处理检查消息
  processCheck(payload: OutputPayload): void {
    // const success = !!Number.parseInt(payload.result);
    // const handler = this.handlers[session];
    // // 未成功展示错误信息
    // if (!success) {
    //   handler.write('[91m' + errormessage + '[0m');
    //   return;
    // }
    // // 发送 connect 命令
    // this.channel.send(InputProtocol.CONNECT, { session, cols: handler.inst.cols, rows: handler.inst.rows });
  }

  // 处理连接消息
  processConnect(payload: OutputPayload): void {
    const success = !!Number.parseInt(payload.result);
    // const handler = this.handlers[session];
    // // 未成功展示错误信息
    // if (!success) {
    //   handler.write('[91m' + errormessage + '[0m');
    //   return;
    // }
    // // 设置可写
    // handler.setCanWrite(true);
    // handler.connect();
  }

  // 处理 pong 消息
  processPong(payload: OutputPayload): void {
  }

  // 处理输出消息
  processOutput(payload: OutputPayload): void {
    // this.handlers[session].write(body);
  }

}
