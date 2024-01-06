import type { Terminal } from 'xterm';

// 终端 tab 元素
export interface TerminalTabItem {
  key: string;
  title: string;
  type: string;

  [key: string]: unknown;
}

// 终端协议
export interface Protocol {
  type: string;
  template: string[];

  [key: string]: unknown;
}

// 终端输入消息内容
export interface InputPayload {
  type?: string;
  sessionId?: string;

  [key: string]: unknown;
}

// 终端输出消息内容
export interface OutputPayload {
  type: string;
  sessionId: string;

  [key: string]: string;
}

// 终端 tab 管理器定义
export interface ITerminalTabManager {
  // 当前 tab
  active: string;
  // 全部 tab
  items: Array<TerminalTabItem>;

  // 点击 tab
  clickTab: (key: string) => void;
  // 删除 tab
  deleteTab: (key: string) => void;
  // 打开 tab
  openTab: (tab: TerminalTabItem) => void;
  // 清空
  clear: () => void;
}

// 终端会话管理器定义
export interface ITerminalSessionManager {
  // 打开终端会话
  openSession: (tab: TerminalTabItem, dom: HTMLElement) => void;
  // 获取终端会话
  getSession: (sessionId: string) => ITerminalSession;
  // 关闭终端会话
  closeSession: (sessionId: string) => void;
  // 重置
  reset: () => void;
}

// 终端通信处理器 定义
export interface ITerminalChannel {
  // 初始化
  init: () => Promise<void>;
  // 是否已连接
  isConnected: () => boolean;
  // 发送消息
  send: (protocol: Protocol, payload: InputPayload) => void;
  // 关闭
  close: () => void;
}

// 终端输出消息体处理器定义
export interface ITerminalOutputProcessor {
  // 处理检查消息
  processCheck: (payload: OutputPayload) => void;
  // 处理连接消息
  processConnect: (payload: OutputPayload) => void;
  // 处理关闭消息
  processClose: (payload: OutputPayload) => void;
  // 处理 pong 消息
  processPong: (payload: OutputPayload) => void;
  // 处理输出消息
  processOutput: (payload: OutputPayload) => void;
}

// 终端会话定义
export interface ITerminalSession {
  hostId: number;
  // terminal 实例
  inst: Terminal;
  // 是否已连接
  connected: boolean;

  // 初始化
  init: (dom: HTMLElement) => void;
  // 连接
  connect: () => void;
  // 设置是否可写
  setCanWrite: (canWrite: boolean) => void;
  // 写入数据
  write: (value: string) => void;
  // 自适应
  fit: () => void;
  // 关闭
  close: () => void;
}
