import type { ITerminalChannel, ITerminalSession, TerminalAddons } from '../types/terminal.type';
import { useTerminalStore } from '@/store';
import { fontFamilySuffix, TerminalStatus } from '../types/terminal.const';
import { InputProtocol } from '../types/terminal.protocol';
import { ITerminalOptions, Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import { WebglAddon } from 'xterm-addon-webgl';
import { WebLinksAddon } from 'xterm-addon-web-links';
import { SearchAddon } from 'xterm-addon-search';
import { ImageAddon } from 'xterm-addon-image';

// 终端会话实现
export default class TerminalSession implements ITerminalSession {

  public hostId: number;

  public inst: Terminal;

  public connected: boolean;

  public canWrite: boolean;

  public status: number;

  private readonly sessionId: string;

  private readonly channel: ITerminalChannel;

  private readonly addons: TerminalAddons;

  constructor(hostId: number,
              sessionId: string,
              channel: ITerminalChannel) {
    this.hostId = hostId;
    this.sessionId = sessionId;
    this.channel = channel;
    this.connected = false;
    this.canWrite = false;
    this.status = TerminalStatus.CONNECTING;
    this.inst = undefined as unknown as Terminal;
    this.addons = {} as TerminalAddons;
  }

  // 初始化
  init(dom: HTMLElement): void {
    const { preference } = useTerminalStore();
    // 初始化实例
    this.inst = new Terminal({
      ...(preference.displaySetting as any),
      theme: preference.theme.schema,
      fastScrollModifier: 'ctrl',
      fontFamily: preference.displaySetting.fontFamily + fontFamilySuffix,
    });
    // 注册插件
    this.addons.fit = new FitAddon();
    this.addons.webgl = new WebglAddon();
    this.addons.link = new WebLinksAddon();
    this.addons.search = new SearchAddon();
    this.addons.image = new ImageAddon();
    for (const addon of Object.values(this.addons)) {
      this.inst.loadAddon(addon);
    }
    // 打开终端
    this.inst.open(dom);
    // 自适应
    this.addons.fit.fit();
  }

  // 设置已连接
  connect(): void {
    this.status = TerminalStatus.CONNECTED;
    this.connected = true;
    this.inst.focus();
    // 注册输入事件
    this.inst.onData(s => {
      if (!this.canWrite) {
        return;
      }
      // 输入
      this.channel.send(InputProtocol.INPUT, {
        sessionId: this.sessionId,
        command: s
      });
    });
    // 注册 resize 事件
    this.inst.onResize(({ cols, rows }) => {
      this.channel.send(InputProtocol.RESIZE, {
        sessionId: this.sessionId,
        cols,
        rows
      });
    });
  }

  // 设置是否可写
  setCanWrite(canWrite: boolean): void {
    this.canWrite = canWrite;
    if (canWrite) {
      this.inst.options.cursorBlink = useTerminalStore().preference.displaySetting.cursorBlink;
    } else {
      this.inst.options.cursorBlink = false;
    }
  }

  // 写入数据
  write(value: string | Uint8Array): void {
    this.inst.write(value);
  }

  // 自适应
  fit(): void {
    this.addons.fit?.fit();
  }

  // 聚焦
  focus(): void {
    this.inst.focus();
  }

  // 清空
  clear(): void {
    this.inst.clear();
    this.inst.clearSelection();
    this.inst.focus();
  }

  // 粘贴
  paste(value: string): void {
    this.inst.paste(value);
    this.inst.focus();
  }

  // 选中全部
  selectAll(): void {
    this.inst.selectAll();
    this.inst.focus();
  }

  // 获取选中
  getSelection(): string {
    const selection = this.inst.getSelection();
    this.inst.focus();
    return selection;
  }

  // 去顶部
  toTop(): void {
    this.inst.scrollToTop();
    this.inst.focus();
  }

  // 去底部
  toBottom(): void {
    this.inst.scrollToBottom();
    this.inst.focus();
  }

  // 获取配置
  getOption(option: string): any {
    return this.inst.options[option as keyof ITerminalOptions] as any;
  }

  // 设置配置
  setOption(option: string, value: any): void {
    this.inst.options[option as keyof ITerminalOptions] = value;
  }

  // 断开连接
  disconnect(): void {
    // 发送关闭消息
    this.channel.send(InputProtocol.CLOSE, {
      sessionId: this.sessionId
    });
  }

  // 关闭
  close(): void {
    try {
      // 卸载插件
      Object.values(this.addons)
        .filter(Boolean)
        .forEach(s => s.dispose());
      // 卸载实体
      this.inst.dispose();
    } catch (e) {
    }
  }

}
