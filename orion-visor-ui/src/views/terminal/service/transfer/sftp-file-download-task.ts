import type { FileTransferItem, IFileDownloadTask } from '@/views/terminal/interfaces';
import { TransferStatus, TerminalMessages } from '../../types/const';
import { getFileName, openDownloadFile } from '@/utils/file';
import { saveAs } from 'file-saver';
import { getDownloadTransferUrl } from '@/api/terminal/terminal-sftp';
import SftpBaseTransferTask from './sftp-base-transfer-task';

// sftp 下载任务实现
export default class SftpFileDownloadTask extends SftpBaseTransferTask implements IFileDownloadTask {

  constructor(type: string, hostId: number, fileItem: FileTransferItem) {
    super(type, hostId, fileItem);
  }

  // 开始回调
  onStart(channelId: string, token: string) {
    // 获取下载 url
    const url = getDownloadTransferUrl(channelId, token);
    // 打开
    openDownloadFile(url);
  }

  // 进度回调
  onProgress(totalSize: number | undefined, currentSize: number | undefined) {
    if (totalSize) {
      this.state.totalSize = totalSize;
    }
    if (currentSize) {
      this.state.currentSize = currentSize;
    }
  };

  // 完成回调
  onFinish() {
    super.onFinish();
    if (this.state.aborted) {
      // 中断则不触发下载
      return;
    }
    if (this.state.totalSize === 0) {
      // 空文件直接触发下载
      try {
        // 触发下载
        saveAs(new Blob([], {
          type: 'application/octet-stream'
        }), getFileName(this.fileItem.name));
        this.state.status = TransferStatus.SUCCESS;
      } catch (e) {
        this.state.status = TransferStatus.ERROR;
        this.state.errorMessage = TerminalMessages.fileSaveError;
      }
    } else {
      this.state.status = TransferStatus.SUCCESS;
    }
  }

}
