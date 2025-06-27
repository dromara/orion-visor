import type { ITerminalTransferManager, IRdpTransferManager, ISftpTransferManager } from '@/views/terminal/interfaces';
import SftpTransferManager from './sftp-transfer-manager';
import RdpTransferManager from './rdp-transfer-manager';

// 传输管理器基类
export default class TerminalTransferManager implements ITerminalTransferManager {

  public sftp: ISftpTransferManager;

  public rdp: IRdpTransferManager;

  constructor() {
    this.sftp = new SftpTransferManager();
    this.rdp = new RdpTransferManager();
  }

}
