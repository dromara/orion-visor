import type { ITerminalChannel, ITerminalSession } from '../types/terminal.type';
import { useTerminalStore } from '@/store';
import { fontFamilySuffix } from '@/views/host/terminal/types/terminal.const';
import { InputProtocol } from '@/views/host/terminal/types/terminal.protocol';
import { Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import { WebglAddon } from 'xterm-addon-webgl';

// 终端插件
export interface TerminalAddons {
  fit: FitAddon;
  webgl: WebglAddon;
}

// 终端会话实现
export default class TerminalSession implements ITerminalSession {

  public hostId: number;

  public inst: Terminal;

  public connected: boolean;

  private canWrite: boolean;

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
    this.inst = undefined as unknown as Terminal;
    this.addons = {} as TerminalAddons;
  }

  // 初始化
  init(dom: HTMLElement): void {
    const { preference } = useTerminalStore();
    // 初始化实例
    this.inst = new Terminal({
      ...(preference.displaySetting as any),
      theme: preference.themeSchema,
      fastScrollModifier: 'shift',
      fontFamily: preference.displaySetting.fontFamily + fontFamilySuffix,
    });
    // 注册插件
    this.addons.fit = new FitAddon();
    this.addons.webgl = new WebglAddon();
    // TODO check
    const inst = this.inst;
    Object.values(this.addons).forEach(s => inst.loadAddon(s));
    // 打开终端
    this.inst.open(dom);
    // 自适应
    this.addons.fit.fit();
    // TODO sendCheck
  }

  // 设置已连接
  connect(): void {
    this.connected = true;
    // 注册输入事件
    this.inst.onData(s => {
      if (!this.canWrite) {
        return;
      }
      // 输入
      this.channel.send(InputProtocol.INPUT, {
        session: this.sessionId,
        command: s
      });
    });
    // 注册 resize 事件
    this.inst.onResize(({ cols, rows }) => {
      this.channel.send(InputProtocol.RESIZE, {
        session: this.sessionId,
        cols,
        rows
      });
    });
  }

  // 设置是否可写
  setCanWrite(canWrite: boolean): void {
    this.canWrite = canWrite;
  }

  // 写入数据
  write(value: string): void {
    this.inst.write(value);
  }

  // 自适应
  fit(): void {
    this.addons.fit?.fit();
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
