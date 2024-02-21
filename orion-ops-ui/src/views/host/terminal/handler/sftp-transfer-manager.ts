import type { ISftpTransferManager, SftpTransferItem } from '../types/terminal.type';

// sftp 传输管理器实现
export default class SftpTransferManager implements ISftpTransferManager {

  transferList: Array<SftpTransferItem>;

  constructor() {
    this.transferList = [];
  }

  // 添加上传文件
  addUpload(items: Array<SftpTransferItem>): void {
    this.transferList.push(...items);
  }

}
