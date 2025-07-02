import type { Reactive } from 'vue';
import type { Terminal } from '@xterm/xterm';
import type { ISearchOptions } from '@xterm/addon-search';
import type Guacamole from 'guacamole-common-js';
import type {
  IRdpSessionClipboardHandler,
  IRdpSessionDisplayHandler,
  ISftpSessionHandler,
  ISshSessionHandler,
  TerminalSessionTabItem
} from '@/views/terminal/interfaces';

// 终端会话类型
export interface TerminalSessionType {
  type: string;
  icon: string;

  [key: string]: unknown;
}

// ssh 初始化配置
export interface SshInitConfig {
  viewport: HTMLElement;
  searchModal: any;
  uploadModal: any;
  webglAvailable: boolean;
}

// guacd 初始化配置
export interface GuacdInitConfig {
  viewport: HTMLElement;
}

// 会话主机信息
export interface SessionHostInfo {
  title: string;
  name: string;
  logId: number;
  hostId: number;
  address: string;
  port: number;
  username: string;
}

// 会话状态
export interface ReactiveSessionState {
  // 连接状态
  connectStatus: number;
  // 是否已连接
  connected: boolean;
  // 是否可写
  canWrite: boolean;
  // 是否可以重新连接
  canReconnect: boolean;
}

// guacd 会话状态
export interface GuacdReactiveSessionStatus extends ReactiveSessionState {
  // 关闭码
  closeCode: number;
  // 关闭信息
  closeMessage: string;
}

// dom 视口处理器定义
export interface IDomViewportHandler {
  // 聚焦
  focus: () => void;
  // 失焦
  blur: () => void;
  // 自适应
  fit: () => void;
  // 修改大小
  resize: (width: number, height: number) => void;
  // 截屏
  screenshot: () => void;
}

// 终端会话定义
export interface ITerminalSession<State extends ReactiveSessionState = ReactiveSessionState> {
  readonly type: string;

  // 会话主机信息
  readonly info: SessionHostInfo;
  // 面板索引
  readonly panelIndex: number;
  // 前端交互的唯一值 前端的 key
  readonly sessionKey: string;
  // 后端交互的唯一值 后端的 sessionId
  sessionId: string;
  // 会话状态
  readonly state: Reactive<State>;

  // 重新初始化
  reInit: () => Promise<void>;
  // 连接会话
  connect: () => void;
  // 断开连接
  disconnect: () => void;
  // 关闭
  close: () => void;
  // ping
  ping: () => void;

  // 设置是否可写
  setCanWrite: (canWrite: boolean) => void;
  // 设置已连接
  setConnected: () => void;
  // 设置已关闭
  setClosed: () => void;
}

// SSH 会话定义
export interface ISshSession extends ITerminalSession, IDomViewportHandler {
  // terminal 实例
  inst: Terminal;
  // 会话配置
  config: SshInitConfig;
  // 处理器
  handler: ISshSessionHandler;

  // 初始化
  init: (config: SshInitConfig) => Promise<void>;
  // 写入数据
  write: (value: string) => void;
  // 查找
  find: (word: string, next: boolean, options: ISearchOptions) => void;
}

// SFTP 会话定义
export interface ISftpSession extends ITerminalSession {
  // 接收器
  handler: ISftpSessionHandler;

  // 初始化
  init: (handler: ISftpSessionHandler) => Promise<void>;
  // 设置显示隐藏文件
  setShowHiddenFile: (show: boolean) => void;
  // 查询文件列表
  list: (path: string) => void;
  // 创建文件夹
  mkdir: (path: string) => void;
  // 创建文件
  touch: (path: string) => void;
  // 移动文件
  move: (path: string, target: string) => void;
  // 删除文件
  remove: (path: string[]) => void;
  // 修改权限
  chmod: (path: string, mod: number) => void;
  // 下载文件夹展开文件
  downloadFlatDirectory: (currentPath: string, path: string[]) => void;
  // 获取内容
  getContent: (path: string) => void;
  // 修改内容
  setContent: (content: string) => void;
}

// Guacd 会话定义
export interface IGuacdSession extends ITerminalSession<GuacdReactiveSessionStatus>, IDomViewportHandler {
  // guacd 客户端
  client: Guacamole.Client;
  // FIXME VNC 可以再抽象
}

// RDP 会话定义
export interface IRdpSession extends IGuacdSession {
  fileSystemName: string;
  // 会话配置
  config: GuacdInitConfig;
  // 视图处理器
  displayHandler: IRdpSessionDisplayHandler;
  // 剪切板处理器
  clipboardHandler: IRdpSessionClipboardHandler;

  // 初始化
  init: (config: GuacdInitConfig) => Promise<void>;
  // 文件系统事件
  onFileSystemEvent: (event: Record<string, any>) => void;
  // 发送键
  sendKeys: (keys: Array<number>) => void;
  // 粘贴
  paste: (data: string) => void;
  // 是否可写
  isWriteable: () => boolean;
}

// sftp 文件
export interface SftpFile {
  name: string;
  path: string;
  suffix: string;
  size: number;
  attr: string;
  isDir: boolean;
  permission: number;
  uid: number;
  gid: number;
  modifyTime: number;
  canPreview: boolean;
}

// 终端会话管理器定义
export interface ITerminalSessionManager {
  // 全部会话
  sessions: Array<ITerminalSession>;
  // 打开 ssh 会话
  openSsh: (item: TerminalSessionTabItem, config: SshInitConfig) => Promise<void>;
  // 打开 sftp 会话
  openSftp: (item: TerminalSessionTabItem, handler: ISftpSessionHandler) => Promise<void>;
  // 打开 rdp 会话
  openRdp: (item: TerminalSessionTabItem, config: GuacdInitConfig) => Promise<void>;
  // 重新打开会话
  reOpenSession: (sessionKey: string) => Promise<void>;
  // 创建终端会话
  createSession: <T extends ITerminalSession>(item: TerminalSessionTabItem) => T;
  // 获取终端会话
  getSession: <T extends ITerminalSession>(sessionKey: string) => T;
  // 关闭终端会话
  closeSession: (sessionKey: string) => void;
  // 自适应
  dispatchFit: () => void;
  // ping
  dispatchPing: () => void;
  // 重置
  reset: () => void;
}
