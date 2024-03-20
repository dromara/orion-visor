import type { IDisposable, ITerminalInitOnlyOptions, ITerminalOptions, Terminal } from 'xterm';
import type { FitAddon } from 'xterm-addon-fit';
import type { SearchAddon } from 'xterm-addon-search';
import type { CanvasAddon } from 'xterm-addon-canvas';

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
  openSearch: () => {};
}

// appender 配置
export interface LogAppenderConf {
  id: number;
  el: HTMLElement;
  openSearch: () => {};
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

  // 设置当前元素
  setCurrent(id: number): void;

  // 打开搜索
  openSearch(): void;

  // 查找关键字
  find(word: string, next: boolean, options: any): void;

  // 聚焦
  focus(): void;

  // 自适应
  fitAll(): void;

  // 去顶部
  toTop(): void;

  // 去底部
  toBottom(): void;

  // 添加字体大小
  addFontSize(addSize: number): void;

  // 复制
  copy(): void;

  // 复制全部
  copyAll(): void;

  // 选中全部
  selectAll(): void;

  // 清空
  clear(): void;

  // 关闭 client
  closeClient(): void;

  // 关闭 view
  closeView(): void;

  // 关闭
  close(): void;
}

/**
 * 批量执行状态
 */
export const execStatus = {
  // 等待中
  WAITING: 'WAITING',
  // 运行中
  RUNNING: 'RUNNING',
  // 执行完成
  COMPLETED: 'COMPLETED',
  // 执行失败
  FAILED: 'FAILED',
};

/**
 * 主机执行状态
 */
export const execHostStatus = {
  // 等待中
  WAITING: 'WAITING',
  // 运行中
  RUNNING: 'RUNNING',
  // 执行完成
  COMPLETED: 'COMPLETED',
  // 执行失败
  FAILED: 'FAILED',
  // 执行超时
  TIMEOUT: 'TIMEOUT',
  // 已中断
  INTERRUPTED: 'INTERRUPTED',
};

// 执行状态 字典项
export const execStatusKey = 'execStatus';

// 执行状态 字典项
export const execHostStatusKey = 'execHostStatus';

// 加载的字典值
export const dictKeys = [execStatusKey, execHostStatusKey];
