import type {
  GuacdInitConfig,
  GuacdReactiveSessionStatus,
  IGuacdChannel,
  IRdpSession,
  IRdpSessionClipboardHandler,
  IRdpSessionDisplayHandler,
  TerminalSessionTabItem
} from '@/views/terminal/interfaces';
import type { OutputPayload } from '@/views/terminal/types/protocol';
import { InputProtocol } from '@/views/terminal/types/protocol';
import { fitDisplayValue, TerminalCloseCode, TerminalMessages } from '@/views/terminal/types/const';
import { screenshot } from '@/views/terminal/types/utils';
import { useTerminalStore } from '@/store';
import Guacamole from 'guacamole-common-js';
import BaseSession from './base-session';
import RdpChannel from '../channel/rdp-channel';
import RdpSessionDisplayHandler from '../handler/rdp-session-display-handler';
import RdpSessionClipboardHandler from '../handler/rdp-session-clipboard-handler';

export const AUDIO_INPUT_MIMETYPE = 'audio/L16;rate=44100,channels=2';

export const CONNECT_TIMEOUT = 10000;

// RDP 会话实现
export default class RdpSession extends BaseSession<GuacdReactiveSessionStatus, IGuacdChannel> implements IRdpSession {

  public config: GuacdInitConfig;

  public client: Guacamole.Client;

  public displayHandler: IRdpSessionDisplayHandler;

  public clipboardHandler: IRdpSessionClipboardHandler;

  private connectTimeoutId?: number;

  constructor(item: TerminalSessionTabItem) {
    super(item, {
      closeCode: 0,
      closeMessage: ''
    });
    this.client = undefined as unknown as Guacamole.Client;
    this.config = {} as unknown as GuacdInitConfig;
    this.displayHandler = undefined as unknown as IRdpSessionDisplayHandler;
    this.clipboardHandler = undefined as unknown as IRdpSessionClipboardHandler;
  }

  // 初始化
  async init(config: GuacdInitConfig) {
    this.config = config;
    // 初始化
    await this.reInit();
  }

  // 初始化 channel
  async reInit(): Promise<void> {
    const rdpGraphSetting = useTerminalStore().preference.rdpGraphSetting;
    // 创建 channel
    this.channel = new RdpChannel(this);
    // 创建 client
    this.client = new Guacamole.Client(this.channel);
    // 创建 display handler
    this.displayHandler = new RdpSessionDisplayHandler(this);
    // 创建 clipboard handler
    this.clipboardHandler = new RdpSessionClipboardHandler(this);
    // 设置 display autoFit
    const autoFit = rdpGraphSetting?.displaySize === fitDisplayValue;
    this.displayHandler.autoFit = autoFit;
    if (!autoFit) {
      this.displayHandler.setDisplaySize(rdpGraphSetting?.displayWidth || 1024, rdpGraphSetting?.displayHeight || 768);
    }
    // 初始化 display
    this.displayHandler.init();
    // 初始化 channel
    await this.channel.init();
    // 注册 client 事件
    this.registerClientEvent();
  }

  // 注册 client 事件
  private registerClientEvent() {
    // 错误回调
    this.client.onerror = (state) => {
      // 错误回调触发关闭
      this.channel.closeTunnel(state.code, state.message || TerminalMessages.sessionClosed);
    };
    // 状态回调
    this.client.onstatechange = (state) => {
      if (state === Guacamole.Client.State.CONNECTED) {
        // 触发已连接
        this.onConnected();
      }
    };
    // 剪切板回调
    this.client.onclipboard = this.clipboardHandler.receiveRemoteClipboardData.bind(this);

    // TODO 下载文件
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
      if (!this.status.connected) {
        this.channel.closeTunnel(TerminalCloseCode.CONNECT_TIMEOUT, TerminalMessages.rdpConnectTimeout);
      }
    }, CONNECT_TIMEOUT);
  }

  // 连接成功
  private onConnected() {
    // 手动触发管道已连接
    this.channel.processConnected({} as unknown as OutputPayload);
    // 监听音频输入
    if (useTerminalStore().preference.rdpGraphSetting?.enableAudioInput) {
      const requestAudioStream = (client: Guacamole.Client) => {
        const stream = client.createAudioStream(AUDIO_INPUT_MIMETYPE);
        let recorder;
        try {
          recorder = Guacamole.AudioRecorder.getInstance(stream, AUDIO_INPUT_MIMETYPE);
        } catch (e) {
          recorder = null;
        }
        if (!recorder) {
          // 创建失败
          stream.sendEnd();
        } else {
          // 完成重新回调
          recorder.onclose = requestAudioStream.bind(this, client);
        }
      };
      requestAudioStream(this.client);
    }
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

  // 是否可写
  isWriteable(): boolean {
    return this.status.connected && this.status.canWrite;
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
