import type { IDisposable, ITerminalOptions, ITerminalInitOnlyOptions } from 'xterm';
import { Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import { SearchAddon } from 'xterm-addon-search';
import { CanvasAddon } from 'xterm-addon-canvas';

// appender 配置
export const AppenderOptions: ITerminalOptions & ITerminalInitOnlyOptions = {
  theme: {
    foreground: '#FFFFFF',
    background: '#202020',
    selectionBackground: '#B5D5FF',
  },
  cols: 30,
  rows: 8,
  rightClickSelectsWord: true,
  disableStdin: true,
  cursorStyle: 'bar',
  cursorBlink: false,
  fastScrollModifier: 'alt',
  fontSize: 14,
  lineHeight: 1.08,
  convertEol: true,
};

// dom 引用
export interface LogDomRef {
  id: number;
  el: HTMLElement;
}

// appender 配置
export interface LogAppenderConf {
  id: number;
  el: HTMLElement;
  terminal: Terminal;
  addons: LogAddons;
}

// appender 插件
export interface LogAddons extends Record<string, IDisposable> {
  fit: FitAddon;
  canvas: CanvasAddon;
  search: SearchAddon;
}

// 执行日志 appender 定义
export interface ILogAppender {
  // 初始化
  init(refs: Array<LogDomRef>): Promise<void>;

  // 自适应
  fit(): void;

  // 关闭 client
  closeClient(): void;

  // 关闭 view
  closeView(): void;

  // 关闭
  close(): void;
}
