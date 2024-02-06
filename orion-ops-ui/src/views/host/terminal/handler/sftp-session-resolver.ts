import type { ISftpSession, ISftpSessionResolver, SftpDataRef, SftpFile } from '../types/terminal.type';
import { Message } from '@arco-design/web-vue';

// sftp 会话接收器实现
export default class SftpSessionResolver implements ISftpSessionResolver {

  private readonly dataRef: SftpDataRef;

  private readonly session: ISftpSession;

  constructor(session: ISftpSession,
              dataRef: SftpDataRef) {
    this.session = session;
    this.dataRef = dataRef;
  }

  // 接受文件列表响应
  resolveList(result: string, path: string, list: Array<SftpFile>) {
    const success = !!Number.parseInt(result);
    this.dataRef.setLoading(false);
    if (!success) {
      Message.error('查询失败');
      return;
    }
    this.dataRef.currentPath = path;
    this.dataRef.list = list;
  }

}
