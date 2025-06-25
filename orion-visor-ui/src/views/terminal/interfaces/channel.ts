import type { InputPayload, OutputPayload, Protocol } from '@/views/terminal/types/protocol';
import type Guacamole from 'guacamole-common-js';

// 终端通信会话 定义
export interface ITerminalChannel {
  // 初始化
  init: () => Promise<void>;
  // 发送消息
  send: (protocol: Protocol, payload?: InputPayload) => void;
  // ping
  ping: () => void;
  // 处理设置id
  processSetId: (payload: OutputPayload) => void;
  // 处理设置信息
  processSetInfo: (payload: OutputPayload) => void;
  // 处理已连接消息
  processConnected: (payload: OutputPayload) => void;
  // 处理已关闭消息
  processClosed: (payload: OutputPayload) => void;
  // 处理修改大小
  processResize: (payload: OutputPayload) => void;
  // 处理 pong 消息
  processPong: (payload: OutputPayload) => void;
  // 是否已开启
  isOpened: () => boolean;
  // 关闭
  close: () => void;
}

// 终端通信会话 SSH
export interface ISshChannel extends ITerminalChannel {
  // 处理 SSH 输出消息
  processSshOutput: (payload: OutputPayload) => void;
}

// 终端通信会话 SFTP
export interface ISftpChannel extends ITerminalChannel {
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

// 终端通信会话 guacd RDP/VNC
export interface IGuacdChannel extends ITerminalChannel, Guacamole.Tunnel {
  // 发送指令
  sendInstruction: (...messages: any[]) => void;
  // 处理指令
  processInstruction: (payload: OutputPayload) => void;
  // 关闭 tunnel
  closeTunnel: (code: number, msg?: string) => void;
}
