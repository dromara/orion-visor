import type Guacamole from 'guacamole-common-js';
import type { ShortcutKeyItem } from '@/views/terminal/types/define';
import type { SftpFile } from '@/views/terminal/interfaces';

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
  paste: (focus?: boolean) => void;
  // 粘贴并且去除尾部空格 (如果配置)
  pasteTrimEnd: (value: string, focus?: boolean) => void;
  // 粘贴原文
  pasteOrigin: (value: string, focus?: boolean) => void;
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
  // 检查追加缺失的部分
  checkAppendMissing: (value: string, focus?: boolean) => void;
}

// sftp 会话处理器定义
export interface ISftpSessionHandler {
  // 设置加载状态
  setLoading: (loading: boolean) => void;
  // 连接后回调
  connectCallback: () => void;
  // 关闭回调
  onClose: (code: number, msg: string) => void;
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

// rdp 会话视图处理器定义
export interface IRdpSessionDisplayHandler {
  displayWidth: number;
  displayHeight: number;
  displayDpi: number;
  autoFit: boolean;
  localCursor: boolean;

  // 初始化
  init: () => void;
  // 聚焦
  focus: () => void;
  // 失焦
  blur: () => void;
  // 自适应
  fit: (force?: boolean) => void;
  // 修改大小
  resize: (width: number, height: number) => void;
  // 设置显示大小
  setDisplaySize: (width: number, height: number) => void;
}

// rdp 会话剪切板处理器定义
export interface IRdpSessionClipboardHandler {
  // 发送数据到远程剪切板
  sendDataToRemoteClipboard: (data: string | File | Blob) => void;
  // 接收远程剪切板数据
  receiveRemoteClipboardData: (stream: Guacamole.InputStream, mimetype: string) => void;
}
