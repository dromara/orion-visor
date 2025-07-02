import type { FileTransferItem, FileTransferReactiveState, IFileTransferTask } from '@/views/terminal/interfaces';
import type { Reactive } from 'vue';
import { reactive } from 'vue';
import { nextId } from '@/utils';
import { TransferStatus } from '@/views/terminal/types/const';

// 文件传输任务基类
export default abstract class BaseFileTransferTask implements IFileTransferTask {
  public type: string;
  public source: string;
  public fileId: string;
  public hostId: number;
  public sessionKey: string;
  // 文件
  public fileItem: FileTransferItem;
  // 状态
  public state: Reactive<FileTransferReactiveState>;

  protected constructor(type: string, source: string,
                        hostId: number, sessionKey: string,
                        fileItem: FileTransferItem,
                        state: Partial<FileTransferReactiveState>) {
    this.type = type;
    this.source = source;
    this.fileId = nextId(10);
    this.hostId = hostId;
    this.sessionKey = sessionKey;
    this.fileItem = fileItem;
    this.state = reactive({
      currentSize: 0,
      totalSize: fileItem.size,
      progress: 0,
      status: TransferStatus.WAITING,
      errorMessage: undefined,
      finished: false,
      aborted: false,
      ...state,
    });
  }

  // 开始
  abstract start(): void;

  // 完成
  abstract finish(): void;

  // 失败
  abstract error(): void;

  // 中断
  abstract abort(): void;

  // 传输完成回调
  abstract onFinish(): void;

  // 传输失败回调
  abstract onError(msg: string | undefined): void;

  // 传输中断回调
  abstract onAbort(): void;

}
