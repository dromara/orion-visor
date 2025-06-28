import type { ISftpChannel, ISftpSession, ISftpSessionHandler, ReactiveSessionState, TerminalSessionTabItem } from '@/views/terminal/interfaces';
import { h } from 'vue';
import { InputProtocol } from '@/views/terminal/types/protocol';
import { Modal } from '@arco-design/web-vue';
import BaseSession from './base-session';
import SftpChannel from '../channel/sftp-channel';

// SFTP 会话实现
export default class SftpSession extends BaseSession<ReactiveSessionState, ISftpChannel> implements ISftpSession {

  public handler: ISftpSessionHandler;

  private showHiddenFile: boolean;

  constructor(item: TerminalSessionTabItem) {
    super(item, {});
    this.showHiddenFile = false;
    this.handler = undefined as unknown as ISftpSessionHandler;
  }

  // 初始化
  async init(handler: ISftpSessionHandler) {
    this.handler = handler;
    // 初始化 channel
    await this.initChannel();
  }

  // 初始化 channel
  async reInit(): Promise<void> {
    // 初始化 channel
    await this.initChannel();
  }

  // 初始化 channel
  async initChannel(): Promise<void> {
    this.channel = new SftpChannel(this);
    // 初始化 channel
    await this.channel.init();
  }

  // 连接会话
  connect(): void {
    // 设置连接中
    super.setConnecting();
    // 发送 connect 命令
    this.channel.send(InputProtocol.CONNECT, {
      body: JSON.stringify({})
    });
  }

  // 设置显示隐藏文件
  setShowHiddenFile(show: boolean): void {
    this.showHiddenFile = show;
  }

  // 查询文件列表
  list(path: string) {
    this.handler.setLoading(true);
    this.channel.send(InputProtocol.SFTP_LIST, {
      showHiddenFile: ~~this.showHiddenFile,
      path
    });
  };

  // 创建文件夹
  mkdir(path: string) {
    this.handler.setLoading(true);
    this.channel.send(InputProtocol.SFTP_MKDIR, {
      path
    });
  };

  // 创建文件
  touch(path: string) {
    this.handler.setLoading(true);
    this.channel.send(InputProtocol.SFTP_TOUCH, {
      path
    });
  };

  // 移动文件
  move(path: string, target: string) {
    this.handler.setLoading(true);
    this.channel.send(InputProtocol.SFTP_MOVE, {
      path,
      target
    });
  };

  // 删除文件
  remove(paths: string[]) {
    // 内容
    const contentNode = h('div', {
        style: {
          display: 'flex',
          flexDirection: 'column',
          maxHeight: '40vh',
          overflowY: 'auto',
        }
      },
      paths.map(s => {
        return h('span', {
          style: {
            marginTop: '4px',
            wordBreak: 'break-all',
          }
        }, s);
      }));
    // 提示
    Modal.confirm({
      title: `确定后将立即删除这 ${paths.length} 个文件且无法恢复!`,
      width: 426,
      modalStyle: { padding: '24px 32px' },
      bodyStyle: { marginTop: '-14px' },
      okButtonProps: { status: 'danger' },
      okText: '删除',
      content: () => contentNode,
      onOk: () => {
        this.handler.setLoading(true);
        this.channel.send(InputProtocol.SFTP_REMOVE, {
          path: paths.join('|')
        });
      }
    });
  };

  // 修改权限
  chmod(path: string, mod: number) {
    this.handler.setLoading(true);
    this.channel.send(InputProtocol.SFTP_CHMOD, {
      path,
      mod
    });
  };

  // 下载文件夹展开文件
  downloadFlatDirectory(currentPath: string, path: string[]) {
    this.handler.setLoading(true);
    this.channel.send(InputProtocol.SFTP_DOWNLOAD_FLAT_DIRECTORY, {
      currentPath,
      path: path.join('|')
    });
  }

  // 获取内容
  getContent(path: string) {
    this.channel.send(InputProtocol.SFTP_GET_CONTENT, {
      path
    });
  };

  // 修改内容
  setContent(path: string) {
    this.channel.send(InputProtocol.SFTP_SET_CONTENT, {
      path
    });
  };

  // 设置已连接
  setConnected(): void {
    super.setConnected();
    // 连接回调
    this.handler.connectCallback();
  }

}
