import type { ISftpChannel, ISftpSession } from '@/views/terminal/interfaces';
import type { OutputPayload } from '@/views/terminal/types/protocol';
import { TerminalCloseCode, TerminalSessionTypes } from '@/views/terminal/types/const';
import { getTerminalAccessToken, openTerminalAccessChannel } from '@/api/terminal/terminal';
import BaseTerminalChannel from './base-terminal-channel';

// 终端通信会话 SFTP 会话实现
export default class SftpChannel extends BaseTerminalChannel<ISftpSession> implements ISftpChannel {

  // 打开 channel
  protected async openChannel(): Promise<void> {
    const { data } = await getTerminalAccessToken({
      hostId: this.session.info.hostId,
      connectType: TerminalSessionTypes.SFTP.type,
    });
    // 打开 channel
    this.client = await openTerminalAccessChannel(TerminalSessionTypes.SFTP.channel, data);
  }

  // 处理已连接消息
  processConnected(_: OutputPayload): void {
    // 设置可写
    this.session.setCanWrite(true);
    // 设置已连接
    this.session.setConnected();
  }

  // 处理已关闭消息
  processClosed({ code, msg }: OutputPayload): void {
    if (this.triggerClosed) {
      return;
    }
    const codeNumber = Number.parseInt(code);
    this.triggerClosed = true;
    this.session.state.canReconnect = TerminalCloseCode.FORCE !== codeNumber;
    // 设置已关闭
    this.session.setClosed();
    // sftp 设置状态
    this.session.handler?.onClose(codeNumber, msg);
    // 关闭 channel
    this.close();
  }

  // 处理 SFTP 文件列表
  processSftpList({ result, path, msg, body }: OutputPayload): void {
    // 获取会话
    this.session.handler.resolveList(path, result, msg, JSON.parse(body));
  }

  // 处理 SFTP 创建文件夹
  processSftpMkdir({ result, msg }: OutputPayload): void {
    // 获取会话
    this.session.handler.resolveSftpMkdir(result, msg);
  }

  // 处理 SFTP 创建文件
  processSftpTouch({ result, msg }: OutputPayload): void {
    // 获取会话
    this.session.handler.resolveSftpTouch(result, msg);
  }

  // 处理 SFTP 移动文件
  processSftpMove({ result, msg }: OutputPayload): void {
    // 获取会话
    this.session.handler.resolveSftpMove(result, msg);
  }

  // 处理 SFTP 删除文件
  processSftpRemove({ result, msg }: OutputPayload): void {
    // 获取会话
    this.session.handler.resolveSftpRemove(result, msg);
  }

  // 处理 SFTP 修改文件权限
  processSftpChmod({ result, msg }: OutputPayload): void {
    // 获取会话
    this.session.handler.resolveSftpChmod(result, msg);
  }

  // 处理 SFTP 下载文件夹展开文件
  processDownloadFlatDirectory({ currentPath, result, msg, body }: OutputPayload): void {
    // 获取会话
    this.session.handler.resolveDownloadFlatDirectory(currentPath, result, msg, JSON.parse(body));
  }

  // 处理 SFTP 获取文件内容
  processSftpGetContent({ result, msg, token }: OutputPayload): void {
    // 获取会话
    this.session.handler.resolveSftpGetContent(result, msg, token);
  }

  // 处理 SFTP 修改文件内容
  processSftpSetContent({ result, msg, token }: OutputPayload) {
    // 获取会话
    this.session.handler.resolveSftpSetContent(result, msg, token);
  }

}
