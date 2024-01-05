import type { ITerminalDispatcher, ITerminalHandler, TerminalTabItem } from '@/store/modules/terminal/types';
import type { HostQueryResponse } from '@/api/asset/host';
import type { HostTerminalAccessResponse } from '@/api/asset/host-terminal';
import { getHostTerminalAccessToken } from '@/api/asset/host-terminal';
import { TabType } from '@/views/host/terminal/types/terminal.const';
import { Message } from '@arco-design/web-vue';
import { sleep } from '@/utils';
import { format, InputProtocol, OutputProtocol, parse, Payload } from '../types/terminal.protocol';
import { useDebounceFn } from '@vueuse/core';
import { addEventListen, removeEventListen } from '@/utils/event';
import { useTerminalStore } from '@/store';

export const wsBase = import.meta.env.VITE_WS_BASE_URL;

// 拆分两套逻辑 1. tab处理, 2. terminal处理
// 太多需要优化的地方了
// 拆成 event

/**
 * 终端调度器
 */
export default class TerminalDispatcher implements ITerminalDispatcher {

  private access?: HostTerminalAccessResponse;

  private client?: WebSocket;

  private handlers: Record<string, ITerminalHandler>;

  private pingTask?: any;

  private readonly dispatchResizeFn: () => {};

  constructor() {
    this.handlers = {};
    this.dispatchResizeFn = useDebounceFn(this.dispatchResize).bind(this);
  }


  // 初始化客户端
  async initClient() {
    if (this.client) {
      return;
    }
    // 获取 access
    const { data: accessData } = await getHostTerminalAccessToken();
    this.access = accessData;
    // 打开会话
    this.client = new WebSocket(`${wsBase}/host/terminal/${accessData.accessToken}`);
    this.client.onerror = event => {
      Message.error('无法连接至服务器');
      console.error('error', event);
    };
    this.client.onclose = event => {
      console.warn('close', event);
    };
    this.client.onmessage = this.handlerMessage.bind(this);
    // 注册 ping 事件
    this.pingTask = setInterval(() => {
      this.client?.send(format(InputProtocol.PING, {} as Payload));
    }, 150000);
    // 注册 resize 事件
    addEventListen(window, 'resize', this.dispatchResizeFn);
    // 等待会话连接成功
    for (let i = 0; i < 100; i++) {
      await sleep(50);
      if (this.client.readyState !== WebSocket.CONNECTING) {
        break;
      }
    }
  }

  // 处理消息
  handlerMessage({ data }: MessageEvent) {
    const payload = parse(data as string);
    if (!payload) {
      return;
    }
    // 选取会话
    switch (payload.type) {
      case OutputProtocol.CHECK.type:
        // 检查信息回调
        this.onTerminalCheckCallback(payload.session, payload.result, payload.errorMessage);
        break;
      case OutputProtocol.CONNECT.type:
        // 连接信息回调
        this.onTerminalConnectCallback(payload.session, payload.result, payload.errorMessage);
        break;
      case OutputProtocol.OUTPUT.type:
        // 输出
        this.onTerminalOutputCallback(payload.session, payload.body);
        break;
      default:
        break;
    }
  }

  // 打开终端
  async openTerminal(record: HostQueryResponse) {
    // 初始化客户端
    await this.initClient();
    // uncheck
    if (!this.access) {
      return;
    }
    const session = this.access.sessionInitial = (parseInt(this.access.sessionInitial as string, 32) + 1).toString(32);
    // 打开会话
    useTerminalStore().tabs.openTab({
      type: TabType.TERMINAL,
      key: session,
      title: record.alias || (`${record.name} ${record.address}`),
      hostId: record.id,
      address: record.address
    });
  }

  // 注册终端处理器
  registerTerminalHandler(tab: TerminalTabItem, handler: ITerminalHandler) {
    this.handlers[tab.key] = handler;
    // 发送 check 命令
    this.client?.send(format(InputProtocol.CHECK, { session: tab.key, hostId: tab.hostId }));
  }

  // 调度重置大小
  dispatchResize() {
    Object.values(this.handlers)
      .filter(h => h.connected)
      .forEach(h => h.fit());
  }

  // 终端检查回调
  onTerminalCheckCallback(session: string, result: string, errormessage: string) {
    const success = !!parseInt(result);
    const handler = this.handlers[session];
    // 未成功展示错误信息
    if (!success) {
      handler.write('[91m' + errormessage + '[0m');
      return;
    }
    // 发送 connect 命令
    this.client?.send(format(InputProtocol.CONNECT, { session, cols: handler.inst.cols, rows: handler.inst.rows }));
  }

  // 终端连接回调
  onTerminalConnectCallback(session: string, result: string, errormessage: string) {
    const success = !!parseInt(result);
    const handler = this.handlers[session];
    // 未成功展示错误信息
    if (!success) {
      handler.write('[91m' + errormessage + '[0m');
      return;
    }
    // 设置可写
    handler.setCanWrite(true);
    handler.connect();
  }

  // 发送消息
  onMessage(session: string, value: string): void {
    // 发送命令
    this.client?.send(format(InputProtocol.INPUT, { session, command: value }));
  }

  // 终端输出回调
  onTerminalOutputCallback(session: string, body: string) {
    this.handlers[session].write(body);
  }

  // 关闭终端
  closeTerminal(session: string) {
    // 发送关闭消息
    this.client?.send(format(InputProtocol.CLOSE, { session }));
    // 关闭终端
    this.handlers[session].close();
  }

  // 重置
  reset(): void {
    this.access = undefined;
    this.handlers = {};
    // 关闭 client
    if (this.client) {
      if (this.client.readyState === WebSocket.CONNECTING) {
        this.client.close();
      }
      this.client = undefined;
    }
    // 清除 ping 事件
    if (this.pingTask) {
      clearInterval(this.pingTask);
      this.pingTask = undefined;
    }
    // 移除 resize 事件
    removeEventListen(window, 'resize', this.dispatchResizeFn);
  }

}
