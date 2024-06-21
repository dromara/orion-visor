import type { ISftpSession, ISftpSessionResolver, ITerminalChannel, TerminalPanelTabItem } from '../types/terminal.type';
import { InputProtocol } from '../types/terminal.protocol';
import { PanelSessionType } from '../types/terminal.const';
import { Modal } from '@arco-design/web-vue';
import BaseSession from './base-session';

// sftp 会话实现
export default class SftpSession extends BaseSession implements ISftpSession {

  public resolver: ISftpSessionResolver;

  private showHiddenFile: boolean;

  private readonly channel: ITerminalChannel;

  constructor(tab: TerminalPanelTabItem,
              channel: ITerminalChannel) {
    super(PanelSessionType.SFTP.type, tab);
    this.channel = channel;
    this.showHiddenFile = false;
    this.resolver = undefined as unknown as ISftpSessionResolver;
  }

  // 初始化
  init(resolver: ISftpSessionResolver): void {
    this.resolver = resolver;
  }

  // 设置已连接
  connect(): void {
    super.connect();
    // 连接回调
    this.resolver.connectCallback();
  }

  // 设置显示隐藏文件
  setShowHiddenFile(show: boolean): void {
    this.showHiddenFile = show;
  }

  // 查询文件列表
  list(path: string) {
    this.resolver.setLoading(true);
    this.channel.send(InputProtocol.SFTP_LIST, {
      sessionId: this.sessionId,
      showHiddenFile: ~~this.showHiddenFile,
      path
    });
  };

  // 创建文件夹
  mkdir(path: string) {
    this.resolver.setLoading(true);
    this.channel.send(InputProtocol.SFTP_MKDIR, {
      sessionId: this.sessionId,
      path
    });
  };

  // 创建文件
  touch(path: string) {
    this.resolver.setLoading(true);
    this.channel.send(InputProtocol.SFTP_TOUCH, {
      sessionId: this.sessionId,
      path
    });
  };

  // 移动文件
  move(path: string, target: string) {
    this.resolver.setLoading(true);
    this.channel.send(InputProtocol.SFTP_MOVE, {
      sessionId: this.sessionId,
      path,
      target
    });
  };

  // 删除文件
  remove(path: string[]) {
    Modal.confirm({
      title: '删除确认',
      content: `确定要删除 ${path.join(',')} 吗? 确定后将立即删除且无法恢复!`,
      onOk: () => {
        this.resolver.setLoading(true);
        this.channel.send(InputProtocol.SFTP_REMOVE, {
          sessionId: this.sessionId,
          path: path.join('|')
        });
      }
    });
  };

  // 修改权限
  chmod(path: string, mod: number) {
    this.resolver.setLoading(true);
    this.channel.send(InputProtocol.SFTP_CHMOD, {
      sessionId: this.sessionId,
      path,
      mod
    });
  };

  // 下载文件夹展开文件
  downloadFlatDirectory(currentPath: string, path: string[]) {
    this.resolver.setLoading(true);
    this.channel.send(InputProtocol.SFTP_DOWNLOAD_FLAT_DIRECTORY, {
      sessionId: this.sessionId,
      currentPath,
      path: path.join('|')
    });
  }

  // 获取内容
  getContent(path: string) {
    this.channel.send(InputProtocol.SFTP_GET_CONTENT, {
      sessionId: this.sessionId,
      path
    });
  };

  // 修改内容
  setContent(path: string, content: string) {
    this.channel.send(InputProtocol.SFTP_SET_CONTENT, {
      sessionId: this.sessionId,
      path,
      content
    });
  };

  // 断开连接
  disconnect(): void {
    super.disconnect();
    // 发送关闭消息
    this.channel.send(InputProtocol.CLOSE, {
      sessionId: this.sessionId
    });
  }

}
