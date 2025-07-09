import type {
  GuacdInitConfig,
  GuacdReactiveSessionStatus,
  IGuacdChannel,
  IGuacdSession,
  IGuacdSessionClipboardHandler,
  IGuacdSessionDisplayHandler,
  TerminalSessionTabItem
} from '@/views/terminal/interfaces';
import type { OutputPayload } from '@/views/terminal/types/protocol';
import { InputProtocol } from '@/views/terminal/types/protocol';
import { TerminalCloseCode, TerminalMessages } from '@/views/terminal/types/const';
import { screenshot } from '@/views/terminal/types/utils';
import Guacamole from 'guacamole-common-js';
import BaseSession from './base-session';
import GuacdSessionClipboardHandler from '../handler/guacd-session-clipboard-handler';

export const CONNECT_TIMEOUT = 30000;

// guacd 会话基类
export default abstract class BaseGuacdSession extends BaseSession<GuacdReactiveSessionStatus, IGuacdChannel> implements IGuacdSession {

  public config: GuacdInitConfig;

  public client: Guacamole.Client;

  public displayHandler: IGuacdSessionDisplayHandler;

  public clipboardHandler: IGuacdSessionClipboardHandler;

  protected connectTimeoutId?: number;

  protected constructor(item: TerminalSessionTabItem) {
    super(item, {
      closeCode: 0,
      closeMessage: ''
    });
    this.client = undefined as unknown as Guacamole.Client;
    this.config = {} as unknown as GuacdInitConfig;
    this.displayHandler = undefined as unknown as IGuacdSessionDisplayHandler;
    this.clipboardHandler = undefined as unknown as IGuacdSessionClipboardHandler;
  }

  // 初始化
  async init(config: GuacdInitConfig) {
    this.config = config;
    // 初始化
    await this.reInit();
  }

  // 初始化 channel
  async reInit(): Promise<void> {
    // 初始化 channel
    this.channel = this.createChannel();
    // 创建 client
    this.client = new Guacamole.Client(this.channel);
    // 初始化 display
    this.displayHandler = this.createDisplay();
    // 初始化剪切板
    this.clipboardHandler = this.createClipboard();
    // 初始化 display
    this.displayHandler.init();
    // 初始化 channel
    await this.channel.init();
    // 注册 client 事件
    this.registerClientEvent();
  }

  // 创建 channel
  protected abstract createChannel(): IGuacdChannel;

  // 创建 display
  protected abstract createDisplay(): IGuacdSessionDisplayHandler;

  // 创建 clipboard
  protected createClipboard() {
    // 创建 clipboard handler
    return new GuacdSessionClipboardHandler(this);
  }

  // 注册 client 事件
  protected registerClientEvent() {
    // 错误回调
    this.client.onerror = (state) => {
      // 错误回调触发关闭
      this.channel.closeTunnel(state.code, state.message || TerminalMessages.sessionClosed);
    };
    // 状态回调
    this.client.onstatechange = (state) => {
      if (state === Guacamole.Client.State.CONNECTED) {
        // 触发连接成功回调
        this.onConnected();
      }
    };
    // 剪切板回调
    this.client.onclipboard = this.clipboardHandler.receiveRemoteClipboardData.bind(this);
  }

  // 连接会话
  connect(): void {
    // 清空超时检查任务
    window.clearTimeout(this.connectTimeoutId);
    // 设置连接中
    super.setConnecting();
    // 连接 client 其实就是打开 channel 和 display
    this.client.connect();
    // 发送 connect 命令
    this.channel.send(InputProtocol.CONNECT, {
      body: JSON.stringify({
        width: this.displayHandler?.displayWidth,
        height: this.displayHandler?.displayHeight,
        dpi: this.displayHandler?.displayDpi,
      })
    });
    // 定时检查是否连接成功
    this.connectTimeoutId = window.setTimeout(() => {
      // 未连接上证明连接超时
      if (!this.state.connected) {
        this.channel.closeTunnel(TerminalCloseCode.CONNECT_TIMEOUT, TerminalMessages.connectTimeout);
      }
    }, CONNECT_TIMEOUT);
  }

  // 连接成功回调
  protected onConnected() {
    // 手动触发管道已连接
    this.channel.processConnected({} as unknown as OutputPayload);
  }

  // 发送键
  sendKeys(keys: Array<number>): void {
    if (!this.isWriteable()) {
      return;
    }
    for (let i = 0; i < keys.length; i++) {
      this.client.sendKeyEvent(1, keys[i]);
    }
    for (let i = 0; i < keys.length; i++) {
      this.client.sendKeyEvent(0, keys[i]);
    }
  }

  // 粘贴
  paste(data: string): void {
    if (!this.isWriteable()) {
      return;
    }
    // 发送至远程剪切板
    this.clipboardHandler?.sendDataToRemoteClipboard(data);
    // 发送粘贴命令
    setTimeout(() => {
      this.sendKeys([65507, 118]);
    }, 100);
  }

  // 聚焦
  focus(): void {
    this.displayHandler?.focus?.();
  }

  // 失焦
  blur(): void {
    this.displayHandler?.blur?.();
  }

  // 自适应
  fit(): void {
    this.displayHandler?.fit(false);
  }

  // 修改大小
  resize(width: number, height: number): void {
    if (!this.isWriteable()) {
      return;
    }
    // 发送重置大小
    this.channel.send(InputProtocol.RESIZE, { width, height, });
  }

  // 截屏
  async screenshot() {
    await screenshot(this.client?.getDisplay()?.getElement() as HTMLElement);
  }

  // 断开连接
  disconnect(): void {
    super.disconnect();
    // 关闭 client
    this.client?.disconnect();
    // 关闭超时检查任务
    clearTimeout(this.connectTimeoutId);
  }

}
