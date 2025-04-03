import type { Terminal } from '@xterm/xterm';
import type { ISearchOptions } from '@xterm/addon-search';
import type { CSSProperties, Reactive } from 'vue';
import type { HostQueryResponse } from '@/api/asset/host';
import type { InputPayload, OutputPayload, Protocol } from '@/types/protocol/terminal.protocol';

// 终端 tab 元素
export interface TerminalTabItem {
  key: string;
  title: string;
  icon: string;

  [key: string]: unknown;
}

// 终端面板 tab 元素
export interface TerminalPanelTabItem extends TerminalTabItem {
  type: string;
  sessionId: string;
  seq: number;
  hostId: number;
  address: string;
  color?: string;
}

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  visible?: boolean;
  disabled?: boolean;
  checked?: boolean;
  active?: boolean;
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

// session tab
export interface PanelSessionTabType {
  type: string;
  icon: string;
}

// 终端 tab 管理器定义
export interface ITerminalTabManager<T extends TerminalTabItem = TerminalTabItem> {
  // 当前 tab
  active: string;
  // 全部 tab
  items: Array<T>;

  // 获取当前 tab
  getCurrentTab: () => T | undefined;
  // 获取 tab
  getTab: (key: string) => T;
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
export interface ITerminalPanelManager {
  // 当前面板
  active: number;
  // 面板列表
  panels: Array<ITerminalTabManager<TerminalPanelTabItem>>;

  // 获取当前面板
  getCurrentPanel: () => ITerminalTabManager<TerminalPanelTabItem>;
  // 设置当前面板
  setCurrentPanel: (active: number) => void;
  // 获取面板
  getPanel: (index: number) => ITerminalTabManager<TerminalPanelTabItem>;
  // 移除面板
  removePanel: (index: number) => void;
  // 重置
  reset: () => void;
}

// 终端会话管理器定义
export interface ITerminalSessionManager {
  // 全部会话
  sessions: Record<string, ITerminalSession>;

  // 打开 ssh 会话
  openSsh: (tab: TerminalPanelTabItem, domRef: XtermDomRef) => Promise<ISshSession>;
  // 打开 sftp 会话
  openSftp: (tab: TerminalPanelTabItem, resolver: ISftpSessionResolver) => Promise<ISftpSession>;
  // 重新打开会话
  reOpenSession: (sessionId: string, newSessionId: string) => Promise<void>;
  // 获取终端会话
  getSession: <T extends ITerminalSession>(sessionId: string) => T;
  // 关闭终端会话
  closeSession: (sessionId: string) => void;
  // 重置大小
  dispatchResize: () => void;
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
  // 处理 SSH 输出消息
  processSshOutput: (payload: OutputPayload) => void;
  // 处理 SFTP 文件列表
  processSftpList: (payload: OutputPayload) => void;
  // 处理 SFTP 创建文件夹
  processSftpMkdir: (payload: OutputPayload) => void;
  // 处理 SFTP 创建文件
  processSftpTouch: (payload: OutputPayload) => void;
  // 处理 SFTP 移动文件
  processSftpMove: (payload: OutputPayload) => void;
  // 处理 SFTP 删除文件
  processSftpRemove: (payload: OutputPayload) => void;
  // 处理 SFTP 修改文件权限
  processSftpChmod: (payload: OutputPayload) => void;
  // 处理 SFTP 下载文件夹展开文件
  processDownloadFlatDirectory: (payload: OutputPayload) => void;
  // 处理 SFTP 获取文件内容
  processSftpGetContent: (payload: OutputPayload) => void;
  // 处理 SFTP 修改文件内容
  processSftpSetContent: (payload: OutputPayload) => void;
}

// xterm dom 元素引用
export interface XtermDomRef {
  viewport: HTMLElement;
  searchModal: any;
  editorModal: any;
  uploadModal: any;
}

// 终端状态
export interface TerminalStatus {
  // 连接状态
  connectStatus: number;
  // 是否已连接
  connected: boolean;
  // 是否可写
  canWrite: boolean;
  // 是否可以重新连接
  canReconnect: boolean;
}

// 终端会话定义
export interface ITerminalSession<Status extends TerminalStatus = TerminalStatus> {
  readonly type: string;
  readonly title: string;
  readonly address: string;
  readonly hostId: number;
  // 终端状态
  readonly status: Reactive<Status>;
  sessionId: string;

  // 连接会话
  connect: () => void;
  // 断开连接
  disconnect: () => void;
  // 关闭
  close: () => void;

  // 设置是否可写
  setCanWrite: (canWrite: boolean) => void;
  // 设置已连接
  setConnected: () => void;
  // 设置已关闭
  setClosed: () => void;
}

// ssh 会话定义
export interface ISshSession extends ITerminalSession {
  // terminal 实例
  inst: Terminal;
  // 处理器
  handler: ISshSessionHandler;

  // 初始化
  init: (domRef: XtermDomRef) => void;
  // 写入数据
  write: (value: string) => void;
  // 修改大小
  resize: (cols: number, rows: number) => void;
  // 聚焦
  focus: () => void;
  // 失焦
  blur: () => void;
  // 自适应
  fit: () => void;
  // 查找
  find: (word: string, next: boolean, options: ISearchOptions) => void;
}

// ssh 会话处理器定义
export interface ISshSessionHandler {
  // 检测是否忽略默认行为
  checkPreventDefault: (e: KeyboardEvent) => boolean;
  // 检测是否为内置快捷键
  checkIsBuiltin: (e: KeyboardEvent) => boolean;
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
  // 打开 sftp
  openSftp: () => void;
  // 上传文件
  uploadFile: () => void;
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

// sftp 会话定义
export interface ISftpSession extends ITerminalSession {
  // 接收器
  resolver: ISftpSessionResolver;

  // 初始化
  init: (resolver: ISftpSessionResolver) => void;
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
  setContent: (path: string) => void;
}

// sftp 会话接收器定义
export interface ISftpSessionResolver {
  // 设置加载状态
  setLoading: (loading: boolean) => void;
  // 连接后回调
  connectCallback: () => void;
  // 关闭回调
  onClose: (forceClose: boolean, msg: string) => void;
  // 接受文件列表响应
  resolveList: (path: string, result: string, msg: string, list: Array<SftpFile>) => void;
  // 接收创建文件夹响应
  resolveSftpMkdir: (result: string, msg: string) => void;
  // 接收创建文件响应
  resolveSftpTouch: (result: string, msg: string) => void;
  // 接收移动文件响应
  resolveSftpMove: (result: string, msg: string) => void;
  // 接收删除文件响应
  resolveSftpRemove: (result: string, msg: string) => void;
  // 接收修改文件权限响应
  resolveSftpChmod: (result: string, msg: string) => void;
  // 接收下载文件夹展开文件响应
  resolveDownloadFlatDirectory: (currentPath: string, result: string, msg: string, list: Array<SftpFile>) => void;
  // 接收获取文件内容响应
  resolveSftpGetContent: (result: string, msg: string, token: string) => void;
  // 接收修改文件内容响应
  resolveSftpSetContent: (result: string, msg: string, token: string) => void;
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

// sftp 传输管理器定义
export interface ISftpTransferManager {
  transferList: Array<SftpTransferItem>;
  // 添加上传任务
  addUpload: (hostId: number, parentPath: string, files: Array<File>) => void;
  // 添加下载任务
  addDownload: (hostId: number, currentPath: string, files: Array<SftpFile>) => void;
  // 取消传输
  cancelTransfer: (fileId: string) => void;
  // 取消全部传输
  cancelAllTransfer: () => void;
}

// sftp 传输处理回调定义
export interface ISftpTransferCallback {
  // 下一分片回调
  onNextPart: () => Promise<void>;
  // 开始回调
  onStart: (channelId: string, token: string) => void;
  // 进度回调
  onProgress: (totalSize: number | undefined, currentSize: number | undefined) => void;
  // 失败回调
  onError: (msg: string | undefined) => void;
  // 完成回调
  onFinish: () => void;
  // 中断回调
  onAbort: () => void;
}

// sftp 传输处理器定义
export interface ISftpTransferHandler extends ISftpTransferCallback {
  // 类型
  type: string;
  // 是否完成
  finished: boolean;
  // 是否中断
  aborted: boolean;
  // 开始
  start: () => void;
  // 完成
  finish: () => void;
  // 失败
  error: () => void;
  // 中断
  abort: () => void;
  // 是否有下一个分片
  hasNextPart: () => boolean;
}

// sftp 上传文件项
export interface SftpTransferItem {
  fileId: string;
  type: string;
  hostId: number;
  name: string;
  parentPath: string;
  currentSize: number,
  totalSize: number;
  progress: number | string;
  status: string;
  errorMessage?: string;
  file: File;
}

// 传输操作响应
export interface TransferOperatorResponse {
  channelId?: string;
  type: string;
  hostId?: number;
  currentSize?: number;
  transferToken?: string;
  success: boolean;
  msg?: string;
  totalSize?: number;
}
