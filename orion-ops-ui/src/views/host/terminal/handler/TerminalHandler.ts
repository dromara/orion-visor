import { ITerminalHandler } from '@/store/modules/terminal/types';
import { useTerminalStore } from '@/store';
import { fontFamilySuffix } from '@/views/host/terminal/types/terminal.const';
import { ITerminalAddon, Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import { WebglAddon } from 'xterm-addon-webgl';

/**
 * 终端处理器
 */
export default class TerminalHandler implements ITerminalHandler {

  public connected: boolean = false;

  private canWrite: boolean = false;

  private readonly session: string;

  public inst: Terminal;

  private fitAddon?: FitAddon;

  private addons: ITerminalAddon[] = [];

  constructor(session: string, dom: HTMLElement) {
    this.session = session;
    const { preference } = useTerminalStore();
    // 初始化实例
    this.inst = new Terminal({
      ...(preference.displaySetting as any),
      theme: preference.themeSchema,
      fastScrollModifier: 'shift',
      fontFamily: preference.displaySetting.fontFamily + fontFamilySuffix,
    });
    this.init(dom);
  }

  // 初始化
  init(dom: HTMLElement): void {
    // 注册插件
    this.addons.push(
      this.fitAddon = new FitAddon(),
      new WebglAddon()
    );
    const inst = this.inst;
    this.addons.forEach(s => inst.loadAddon(s));
    // 打开终端
    this.inst.open(dom);
    // 自适应
    this.fitAddon.fit();
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
      useTerminalStore().dispatcher.onMessage(this.session, s);
    });
    // 注册 resize 事件
    this.inst.onResize(({ cols, rows }) => {
      // 输入
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
    this.fitAddon?.fit();
  }

  // 关闭
  close(): void {
    try {
      for (let addon of this.addons) {
        addon.dispose();
      }
      this.inst.dispose();
    } catch (e) {
    }
  }

}
