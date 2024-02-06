import type { ISftpSession, ISftpSessionResolver, ITerminalChannel, SftpDataRef } from '../types/terminal.type';
import { InputProtocol } from '../types/terminal.protocol';
import SftpSessionResolver from './sftp-session-resolver';

// sftp 会话实现
export default class SftpSession implements ISftpSession {

  public readonly hostId: number;

  public sessionId: string;

  public connected: boolean;

  public resolver: ISftpSessionResolver;

  private dataRef: SftpDataRef;

  private readonly channel: ITerminalChannel;

  constructor(hostId: number,
              sessionId: string,
              channel: ITerminalChannel) {
    this.hostId = hostId;
    this.sessionId = sessionId;
    this.channel = channel;
    this.connected = false;
    this.dataRef = undefined as unknown as SftpDataRef;
    this.resolver = undefined as unknown as ISftpSessionResolver;
  }

  // 初始化
  init(dataRef: SftpDataRef): void {
    this.dataRef = dataRef;
    // 处理器
    this.resolver = new SftpSessionResolver(this, dataRef);
  }

  // 设置已连接
  connect(): void {
    this.connected = true;
    // 加载 home 目录文件数据
    this.list(undefined);
  }

  // 查询文件列表
  list(path: string | undefined) {
    this.dataRef.setLoading(true);
    this.channel.send(InputProtocol.SFTP_LIST, {
      sessionId: this.sessionId,
      path
    });
  };

  // 断开连接
  disconnect(): void {
    // 发送关闭消息
    this.channel.send(InputProtocol.CLOSE, {
      sessionId: this.sessionId
    });
  }

  // 关闭
  close(): void {
  }

}
