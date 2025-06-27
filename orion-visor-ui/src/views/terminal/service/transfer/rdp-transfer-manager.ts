import type { IRdpTransferManager, IRdpSession } from '@/views/terminal/interfaces';
import type Guacamole from 'guacamole-common-js';
import { TerminalMessages } from '../../types/const';
import { Message } from '@arco-design/web-vue';
import BaseTransferManager from './base-transfer-manager';
import RdpFileDownloadTask from './rdp-file-download-task';
import RdpFileUploadTask from './rdp-file-upload-task';

// RDP 传输管理器实现
export default class RdpTransferManager extends BaseTransferManager implements IRdpTransferManager {

  constructor() {
    super();
  }

  // 添加上传任务
  async addUpload(session: IRdpSession, file: File) {
    Message.info(TerminalMessages.fileUploading);
    // 创建任务
    const task = new RdpFileUploadTask(session, {
      name: file.webkitRelativePath || file.name,
      parentPath: session.fileSystemName,
      size: file.size,
      file,
    });
    this.tasks.push(task);
    // 开始上传
    task.start();
    // 开始计算进度
    this.resetProgressTimer();
  }

  // 添加下载任务
  addDownload(session: IRdpSession, stream: Guacamole.InputStream, mimetype: string, name: string) {
    Message.info(TerminalMessages.fileDownloading);
    // 创建任务
    const task = new RdpFileDownloadTask(session, stream, mimetype, {
      name,
      parentPath: session.fileSystemName,
      size: 0,
      unknownSize: true,
    });
    this.tasks.push(task);
    // 开始下载
    task.start();
    // 开始计算进度
    this.resetProgressTimer();
  }

  // 取消传输
  cancelTransfer(fileId: string): void {
    const index = this.tasks.findIndex(s => s.fileId === fileId);
    if (index === -1) {
      return;
    }
    // 中断
    this.tasks[index].abort();
    // 从列表中移除
    this.tasks.splice(index, 1);
  }

  // 通过 sessionKey 关闭
  closeBySessionKey(sessionKey: string): void {
    this.tasks.filter(s => s.sessionKey === sessionKey)
      .forEach(s => s.onError(TerminalMessages.sessionClosed));
  }

}
