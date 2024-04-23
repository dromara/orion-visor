import { ISftpSession, ISshSession, ITerminalChannel, ITerminalOutputProcessor, ITerminalSessionManager, OutputPayload } from '../types/terminal.type';
import { InputProtocol } from '../types/terminal.protocol';
import { TerminalStatus } from '../types/terminal.const';
import { useTerminalStore } from '@/store';
import { Message } from '@arco-design/web-vue';
import SshSession from './ssh-session';
import SftpSession from './sftp-session';

// 终端输出消息体处理器实现
export default class TerminalOutputProcessor implements ITerminalOutputProcessor {

  private readonly sessionManager: ITerminalSessionManager;

  private readonly channel: ITerminalChannel;

  constructor(sessionManager: ITerminalSessionManager, channel: ITerminalChannel) {
    this.sessionManager = sessionManager;
    this.channel = channel;
  }

  // 处理检查消息
  processCheck({ sessionId, result, msg }: OutputPayload): void {
    const success = !!Number.parseInt(result);
    const session = this.sessionManager.getSession(sessionId);
    session.canReconnect = !success;
    if (session instanceof SshSession) {
      // ssh 会话
      if (success) {
        // 检查成功发送 connect 命令
        const { preference } = useTerminalStore();
        this.channel.send(InputProtocol.CONNECT, {
          sessionId,
          terminalType: preference.sessionSetting.terminalEmulationType || 'xterm',
          cols: session.inst.cols,
          rows: session.inst.rows
        });
      } else {
        // 未成功展示错误信息
        session.write(`[91m${msg || ''}\r\n输入回车重新连接...[0m\r\n\r\n`);
        session.status = TerminalStatus.CLOSED;
      }
    } else if (session instanceof SftpSession) {
      // sftp 会话
      if (success) {
        // 检查成功发送 connect 命令
        this.channel.send(InputProtocol.CONNECT, {
          sessionId,
        });
      } else {
        // 未成功提示错误信息
        session.resolver?.onClose(false, msg);
        Message.error(msg || '建立 SFTP 失败');
      }
    }
  }

  // 处理连接消息
  processConnect({ sessionId, result, msg }: OutputPayload): void {
    const success = !!Number.parseInt(result);
    const session = this.sessionManager.getSession(sessionId);
    session.canReconnect = !success;
    if (session instanceof SshSession) {
      // ssh 会话
      if (success) {
        // 设置可写
        session.setCanWrite(true);
        // 执行连接逻辑
        session.connect();
      } else {
        // 未成功展示错误信息
        session.write(`[91m${msg || ''}\r\n输入回车重新连接...[0m\r\n\r\n`);
        session.status = TerminalStatus.CLOSED;
      }
    } else if (session instanceof SftpSession) {
      // sftp 会话
      if (success) {
        // 执行连接逻辑
        session.connect();
      } else {
        // 未成功提示错误信息
        session.resolver?.onClose(false, msg);
        Message.error(msg || '打开 SFTP 失败');
      }
    }
  }

  // 处理关闭消息
  processClose({ sessionId, msg, forceClose }: OutputPayload): void {
    const session = this.sessionManager.getSession(sessionId);
    // 无需处理 (直接关闭 tab)
    if (!session) {
      return;
    }
    const isForceClose = !!Number.parseInt(forceClose);
    session.connected = false;
    session.canReconnect = !isForceClose;
    if (session instanceof SshSession) {
      // ssh 拼接关闭消息
      session.write(`\r\n\r\n[91m${msg || ''}[0m\r\n`);
      if (!isForceClose) {
        session.write('[91m输入回车重新连接...[0m\r\n\r\n');
      }
      // 设置状态
      session.status = TerminalStatus.CLOSED;
      // 设置不可写
      session.setCanWrite(false);
    } else if (session instanceof SftpSession) {
      // sftp 设置状态
      session.resolver?.onClose(isForceClose, msg);
    }
  }

  // 处理 pong 消息
  processPong(payload: OutputPayload): void {
    // console.log('pong');
  }

  // 处理 SSH 输出消息
  processSshOutput({ sessionId, body }: OutputPayload): void {
    const session = this.sessionManager.getSession<ISshSession>(sessionId);
    session && session.write(body);
  }

  // 处理 SFTP 文件列表
  processSftpList({ sessionId, result, path, body }: OutputPayload): void {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveList(result, path, JSON.parse(body));
  }

  // 处理 SFTP 创建文件夹
  processSftpMkdir({ sessionId, result, msg }: OutputPayload): void {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveSftpMkdir(result, msg);
  }

  // 处理 SFTP 创建文件
  processSftpTouch({ sessionId, result, msg }: OutputPayload): void {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveSftpTouch(result, msg);
  }

  // 处理 SFTP 移动文件
  processSftpMove({ sessionId, result, msg }: OutputPayload): void {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveSftpMove(result, msg);
  }

  // 处理 SFTP 删除文件
  processSftpRemove({ sessionId, result, msg }: OutputPayload): void {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveSftpRemove(result, msg);
  }

  // 处理 SFTP 修改文件权限
  processSftpChmod({ sessionId, result, msg }: OutputPayload): void {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveSftpChmod(result, msg);
  }

  // 处理 SFTP 下载文件夹展开文件
  processDownloadFlatDirectory({ sessionId, currentPath, body }: OutputPayload): void {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveDownloadFlatDirectory(currentPath, JSON.parse(body));
  }

  // 处理 SFTP 获取文件内容
  processSftpGetContent({ sessionId, path, result, content }: OutputPayload): void {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveSftpGetContent(path, result, content);
  }

  // 处理 SFTP 修改文件内容
  processSftpSetContent({ sessionId, result, msg }: OutputPayload) {
    // 获取会话
    const session = this.sessionManager.getSession<ISftpSession>(sessionId);
    session && session.resolver.resolveSftpSetContent(result, msg);
  }

}
