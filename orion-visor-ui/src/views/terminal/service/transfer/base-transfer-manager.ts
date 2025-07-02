import type { ITransferManager, FileTransferTaskType } from '@/views/terminal/interfaces';
import { TransferStatus, TerminalMessages } from '@/views/terminal/types/const';

// 传输管理器基类
export default abstract class BaseTransferManager implements ITransferManager {

  public tasks: Array<FileTransferTaskType>;

  protected progressIntervalId?: number;

  protected constructor() {
    this.tasks = [];
  }

  // 取消传输
  abstract cancelTransfer(fileId: string): void;

  // 取消全部执行中的传输
  cancelAllTransfer(): void {
    // 从列表中移除非传输中的元素
    this.tasks.reduceRight((_, value: FileTransferTaskType, index: number) => {
      if (value.state.status !== TransferStatus.TRANSFERRING) {
        this.tasks.splice(index, 1);
      }
    }, null as any);
  }

  // 计算传输进度
  protected calculateProgress(): void {
    let count = 0;
    this.tasks.forEach(task => {
      const state = task.state;
      if (state.totalSize !== 0
        && (state.status === TransferStatus.WAITING || state.status === TransferStatus.TRANSFERRING)
        && task.fileItem.unknownSize !== true) {
        count++;
        state.progress = (state.currentSize / state.totalSize * 100).toFixed(2);
      }
    });
    // 如果所有任务都已结束则关闭
    if (count === 0) {
      clearInterval(this.progressIntervalId);
    }
  };

  // 重置进度定时器
  protected resetProgressTimer(): void {
    clearInterval(this.progressIntervalId);
    this.progressIntervalId = window.setInterval(this.calculateProgress.bind(this), 500);
  }

  // 关闭
  protected close(): void {
    // 关闭传输进度
    clearInterval(this.progressIntervalId);
    // 进行中和等待中的文件改为失败
    this.tasks.forEach(task => {
      const state = task.state;
      if (state.status === TransferStatus.WAITING ||
        state.status === TransferStatus.TRANSFERRING) {
        state.status = TransferStatus.ERROR;
        state.errorMessage = TerminalMessages.sessionClosed;
      }
    });
  }

}
