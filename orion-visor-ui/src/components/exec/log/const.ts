import type { XtermAddons } from '@/types/xterm';
import { defaultFontFamily, defaultTheme } from '@/types/xterm';
import type { ITerminalInitOnlyOptions, ITerminalOptions, Terminal } from '@xterm/xterm';

// 执行类型
export type ExecType = 'BATCH' | 'JOB';

// 批量执行状态
export const ExecStatus = {
  // 等待中
  WAITING: 'WAITING',
  // 运行中
  RUNNING: 'RUNNING',
  // 执行完成
  COMPLETED: 'COMPLETED',
  // 执行失败
  FAILED: 'FAILED',
};

// 主机执行状态
export const ExecHostStatus = {
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

// 执行模式
export const ExecMode = {
  MANUAL: 'MANUAL',
  JOB: 'JOB'
};

// 执行状态 字典项
export const execStatusKey = 'execStatus';

// 执行状态 字典项
export const execHostStatusKey = 'execHostStatus';

// 加载的字典值
export const dictKeys = [execStatusKey, execHostStatusKey];

// appender 配置
export const LogAppenderOptions: ITerminalOptions & ITerminalInitOnlyOptions = {
  theme: defaultTheme,
  cols: 30,
  rows: 8,
  rightClickSelectsWord: true,
  disableStdin: true,
  cursorStyle: 'bar',
  cursorBlink: false,
  fastScrollModifier: 'alt',
  fontSize: 13,
  lineHeight: 1.12,
  convertEol: true,
  allowProposedApi: true,
  fontFamily: defaultFontFamily,
};

// append 配置
export interface LogAppenderConfig {
  id: number;
  type: ExecType;
  scrollLines: number;
}

// appender 视口
export interface LogAppenderView {
  id: number;
  viewport: HTMLElement;
  opened: boolean;
  openSearch: () => {};
  terminal: Terminal;
  addons: XtermAddons;
}

// 执行日志 appender 定义
export interface ILogAppender {
  // 初始化
  init(refs: Array<LogAppenderView>): Promise<void>;

  // 打开日志
  openLog(id: number): void;

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
