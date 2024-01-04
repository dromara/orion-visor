import type { ITerminalDispatcher, TerminalTabItem } from '@/store/modules/terminal/types';
import type { HostQueryResponse } from '@/api/asset/host';
import type { HostTerminalAccessResponse } from '@/api/asset/host-terminal';
import { getHostTerminalAccessToken } from '@/api/asset/host-terminal';
import { InnerTabs, TabType } from '@/views/host/terminal/types/terminal.const';
import { Message } from '@arco-design/web-vue';
import { sleep } from '@/utils';
import { InputProtocol, format } from '../types/terminal.protocol';

export const wsBase = import.meta.env.VITE_WS_BASE_URL;

/**
 * 终端调度器
 */
export default class TerminalDispatcher implements ITerminalDispatcher {

  private access?: HostTerminalAccessResponse;

  private client?: WebSocket;

  public active: string;

  public items: Array<TerminalTabItem>;

  constructor() {
    this.active = InnerTabs.NEW_CONNECTION.key;
    this.items = [InnerTabs.NEW_CONNECTION];
  }

  // 点击 tab
  clickTab(key: string): void {
    this.active = key;
  }

  // 删除 tab
  deleteTab(key: string): void {
    // 获取当前 tab
    const tabIndex = this.items.findIndex(s => s.key === key);
    if (this.items[tabIndex]?.type === TabType.TERMINAL) {
      // 如果是 terminal 则需要关闭
      this.closeTerminal(key);
    }
    // 删除 tab
    this.items.splice(tabIndex, 1);
    if (key === this.active && this.items.length !== 0) {
      // 切换为前一个 tab
      this.active = this.items[Math.max(tabIndex - 1, 0)].key;
    }
    // fixme 关闭 socket
  }

  // 打开 tab
  openTab(tab: TerminalTabItem): void {
    // 不存在则创建 tab
    if (!this.items.find(s => s.key === tab.key)) {
      this.items.push(tab);
    }
    this.active = tab.key;
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
    this.client.onmessage = this.handlerMessage;
    // 等待会话等待完成
    for (let i = 0; i < 100; i++) {
      await sleep(50);
      if (this.client.readyState === WebSocket.OPEN) {
        break;
      }
    }
  }

  // 处理消息
  handlerMessage({ data }: MessageEvent) {
    console.log(data);
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
    this.openTab({
      type: TabType.TERMINAL,
      key: session,
      title: record.alias || (`${record.name} ${record.address}`),
      hostId: record.id,
      address: record.address,
      checked: false,
      connected: false
    });
  }

  // 注册终端钩子
  registerTerminalHook(tab: TerminalTabItem) {
    if (!this.client) {
      return;
    }
    // 发送 check 命令
    this.client.send(format(InputProtocol.CHECK, { session: tab.key, hostId: tab.hostId }));
  }

  // 关闭终端
  closeTerminal(key: string) {
  }

  // 重置
  reset(): void {
    this.active = undefined as unknown as string;
    this.items = [];
    this.access = undefined;
    this.client = undefined;
  }

}

