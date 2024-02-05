import type { Terminal } from 'xterm';
import type { FitAddon } from 'xterm-addon-fit';
import type { CanvasAddon } from 'xterm-addon-canvas';
import type { WebglAddon } from 'xterm-addon-webgl';
import type { WebLinksAddon } from 'xterm-addon-web-links';
import type { ISearchOptions, SearchAddon } from 'xterm-addon-search';
import type { ImageAddon } from 'xterm-addon-image';
import type { CSSProperties } from 'vue';
import type { HostQueryResponse } from '@/api/asset/host';

// 终端 tab 元素
export interface TerminalTabItem {
  key: string;
  title: string;
  icon: string;

  [key: string]: unknown;
}

// 终端面板 tab 元素
export interface TerminalPanelTabItem extends TerminalTabItem {
  seq: number;
  hostId: number;
  address: string;
  type: string;
}

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  visible?: boolean;
  disabled?: boolean;
  checked?: boolean;
  iconStyle?: CSSProperties;
  click: () => void;
}

// 组合操作元素
export interface CombinedHandlerItem {
  icon: string,
  title: string;
  tab?: TerminalTabItem;
  host?: HostQueryResponse;
}

// 右键菜单元素
export interface ContextMenuItem {
  item: string;
  icon: string;
  content: string;
}

// 快捷键元素
export interface ShortcutKeyItem {
  item: string;
  content: string;
  type: number;
}

// ssh 额外配置
export interface SshExtraModel {
  authType?: string;
  username?: string;
  keyId?: number;
  identityId?: number;
}

// session tab
export interface PanelSessionTab {
  type: string;
  icon: string;
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

// 终端 dom 元素引用
export interface TerminalDomRef {
  el: HTMLElement;
  searchModal: any;
  editorModal: any;
}

// 终端 tab 管理器定义
export interface ITerminalTabManager<T extends TerminalTabItem = TerminalTabItem> {
  // 当前 tab
  active: string;
  // 全部 tab
  items: Array<T>;

  // 获取当前 tab
  getCurrentTab: () => T | undefined;
  // 点击 tab
  clickTab: (key: string) => void;
  // 删除 tab
  deleteTab: (key: string) => void;
  // 打开 tab
  openTab: (tab: T) => void;
  // 切换到前一个 tab
  changeToPrevTab: () => void;
  // 切换到后一个 tab
  changeToNextTab: () => void;
  // 切换索引 tab
  changeToIndex: (index: number) => void;
  // 清空
  clear: () => void;
}

// 终端面板管理器定义
export interface ITerminalPanelManager<T extends TerminalPanelTabItem = TerminalPanelTabItem> {
  // 当前面板
  active: number;
  // 面板列表
  panels: Array<ITerminalTabManager<T>>;

  // 获取当前面板
  getCurrentPanel: () => ITerminalTabManager<T>;
  // 设置当前面板
  setCurrentPanel: (active: number) => void;
  // 获取面板
  getPanel: (index: number) => ITerminalTabManager<T>;
  // 移除面板
  removePanel: (index: number) => void;
  // 重置
  reset: () => void;
}

// 终端会话管理器定义
export interface ITerminalSessionManager {
  // 打开终端会话
  openSession: (tab: TerminalTabItem, domRef: TerminalDomRef) => Promise<ITerminalSession>;
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

// 终端插件
export interface TerminalAddons {
  fit: FitAddon;
  webgl: WebglAddon;
  canvas: CanvasAddon;
  weblink: WebLinksAddon;
  search: SearchAddon;
  image: ImageAddon;
}

// 终端会话定义
export interface ITerminalSession {
  hostId: number;
  sessionId: string;
  // terminal 实例
  inst: Terminal;
  // 是否已连接
  connected: boolean;
  // 是否可写
  canWrite: boolean;
  // 状态
  status: number;
  // 处理器
  handler: ITerminalSessionHandler;

  // 初始化
  init: (domRef: TerminalDomRef) => void;
  // 连接
  connect: () => void;
  // 设置是否可写
  setCanWrite: (canWrite: boolean) => void;
  // 写入数据
  write: (value: string | Uint8Array) => void;
  // 聚焦
  focus: () => void;
  // 失焦
  blur: () => void;
  // 自适应
  fit: () => void;
  // 查找
  find: (word: string, next: boolean, options: ISearchOptions) => void;
  // 断开连接
  disconnect: () => void;
  // 关闭
  close: () => void;
}

// 终端会话处理器定义
export interface ITerminalSessionHandler {
  // 检测是否忽略默认行为
  checkPreventDefault: (e: KeyboardEvent) => boolean;
  // 启用状态
  enabledStatus: (option: string) => boolean;
  // 调用处理方法
  invokeHandle: (option: string) => void;
  // 获取快捷键
  getShortcutKey: (e: KeyboardEvent) => ShortcutKeyItem | undefined;

  // 复制选中
  copy: () => void;
  // 从剪切板粘贴并且去除尾部空格 (如果配置)
  paste: () => void;
  // 粘贴并且去除尾部空格 (如果配置)
  pasteTrimEnd: (value: string) => void;
  // 粘贴原文
  pasteOrigin: (value: string) => void;
  // 选中全部
  selectAll: () => void;
  // 去顶部
  toTop: () => void;
  // 去底部
  toBottom: () => void;
  // 打开搜索
  search: () => void;
  // 增大字号
  fontSizePlus: () => void;
  // 减小字号
  fontSizeSubtract: () => void;
  // 打开命令编辑器
  commandEditor: () => void;
  // 中断
  interrupt: () => void;
  // 回车
  enter: () => void;
  // 清空
  clear: () => void;
  // 断开连接
  disconnect: () => void;
  // 截图
  screenshot: () => void;
  // 检查追加缺失的部分
  checkAppendMissing: (value: string) => void;
}
