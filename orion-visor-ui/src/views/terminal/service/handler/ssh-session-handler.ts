import type { ShortcutKey, TerminalShortcutKey, TerminalSshInteractSetting } from '@/store/modules/terminal/types';
import type { ISshSession, ISshSessionHandler } from '@/views/terminal/interfaces';
import type { Terminal } from '@xterm/xterm';
import useCopy from '@/hooks/copy';
import { useTerminalStore } from '@/store';
import { TerminalSessionTypes, TerminalShortcutItems } from '../../types/const';

// 阻止默认行为的快捷键
const preventKeys: Array<ShortcutKey> = [
  {
    ctrlKey: true,
    altKey: false,
    shiftKey: true,
    code: 'KeyC'
  },
];

// 内置快捷键
const builtinKeys: Array<ShortcutKey> = [
  {
    ctrlKey: true,
    altKey: false,
    shiftKey: true,
    code: 'KeyV'
  }, {
    ctrlKey: false,
    altKey: false,
    shiftKey: true,
    code: 'Insert'
  },
];

const { copy: copyValue, readText } = useCopy();

// ssh 会话处理器实现
export default class SshSessionHandler implements ISshSessionHandler {

  private readonly inst: Terminal;

  private readonly session: ISshSession;

  private readonly interactSetting: TerminalSshInteractSetting;

  private readonly shortcutKeys: Array<TerminalShortcutKey>;

  constructor(session: ISshSession) {
    this.session = session;
    this.inst = session.inst;
    const { preference } = useTerminalStore();
    this.interactSetting = preference.sshInteractSetting;
    this.shortcutKeys = preference.shortcutSetting.keys;
  }

  // 检测是否忽略默认行为
  checkPreventDefault(e: KeyboardEvent): boolean {
    return !!preventKeys.find(key => {
      return key.code === e.code
        && key.altKey === e.altKey
        && key.shiftKey === e.shiftKey
        && key.ctrlKey === e.ctrlKey;
    });
  }

  // 检测是否为内置快捷键
  checkIsBuiltin(e: KeyboardEvent): boolean {
    return !!builtinKeys.find(key => {
      return key.code === e.code
        && key.altKey === e.altKey
        && key.shiftKey === e.shiftKey
        && key.ctrlKey === e.ctrlKey;
    });
  }

  // 启用状态
  enabledStatus(option: string): boolean {
    switch (option) {
      case 'paste':
      case 'pasteTrimEnd':
      case 'pasteOrigin':
      case 'interrupt':
      case 'enter':
      case 'openSftp':
      case 'uploadFile':
      case 'checkAppendMissing':
        return this.session.state.canWrite;
      case 'disconnect':
        return this.session.state.connected;
      default:
        return true;
    }
  }

  // 调用处理方法
  invokeHandle(option: string) {
    // 检测启用状态
    if (!this.enabledStatus(option)) {
      return;
    }
    // 调用实际处理方法
    const handler = this[option as keyof this] as () => void;
    handler && handler.call(this);
  }

  // 获取快捷键
  getShortcutKey(e: KeyboardEvent) {
    const key = this.shortcutKeys.find(key => {
      return key.code === e.code
        && key.altKey === e.altKey
        && key.shiftKey === e.shiftKey
        && key.ctrlKey === e.ctrlKey;
    });
    if (!key) {
      return undefined;
    }
    return TerminalShortcutItems.find(s => s.item === key.item);
  }

  // 复制选中
  copy() {
    let selection = this.inst.getSelection();
    if (selection) {
      // 去除尾部空格
      if (this.interactSetting.copyAutoTrim) {
        selection = selection.trimEnd();
      }
      // 复制
      copyValue(selection, false);
    }
    // 聚焦
    this.inst.focus();
  }

  // 从剪切板粘贴并且去除尾部空格 (如果配置)
  paste(focus?: boolean) {
    readText().then(s => this.pasteTrimEnd(s, focus));
  }

  // 粘贴并且去除尾部空格 (如果配置)
  pasteTrimEnd(value: string, focus?: boolean) {
    if (this.interactSetting.pasteAutoTrim) {
      // 粘贴前去除尾部空格
      this.inst.paste(value.trimEnd());
    } else {
      this.inst.paste(value);
    }
    this.inst.focus();
    if (focus !== false) {
      this.inst.focus();
    }
  }

  // 粘贴原文
  pasteOrigin(value: string, focus?: boolean) {
    this.inst.paste(value);
    if (focus !== false) {
      this.inst.focus();
    }
  }

  // 选中全部
  selectAll() {
    this.inst.selectAll();
    this.inst.focus();
  }

  // 去顶部
  toTop(): void {
    this.inst.scrollToTop();
    this.inst.focus();
  }

  // 去底部
  toBottom(): void {
    this.inst.scrollToBottom();
    this.inst.focus();
  }

  // 打开搜索
  search() {
    this.session.config.searchModal?.toggle();
  }

  // 增大字号
  fontSizePlus() {
    this.fontSizeAdd(1);
  }

  // 减小字号
  fontSizeSubtract() {
    this.fontSizeAdd(-1);
  }

  // 字号增加
  private fontSizeAdd(addSize: number) {
    this.inst.options['fontSize'] = this.inst.options['fontSize'] as number + addSize;
    if (this.session.state.connected) {
      this.session.fit();
      this.inst.focus();
    }
  }

  // 打开 sftp
  openSftp() {
    const terminalStore = useTerminalStore();
    const host = terminalStore.hosts.hostList
      .find(s => s.id === this.session.info.hostId);
    if (host) {
      terminalStore.openSession(host, TerminalSessionTypes.SFTP);
    }
  }

  // 上传文件
  uploadFile(): void {
    this.session.config.uploadModal.open('/');
  }

  // ctrl + c
  interrupt() {
    this.inst.paste(String.fromCharCode(3));
  }

  // 回车
  enter() {
    this.inst.paste(String.fromCharCode(13));
  }

  // 清空
  clear() {
    this.inst.clear();
    this.inst.clearSelection();
    this.inst.focus();
  }

  // 断开连接
  disconnect() {
    this.session.disconnect();
  }

  // 检查追加缺失的部分
  checkAppendMissing(value: string, focus?: boolean): void {
    // 获取最后一行数据
    const buffer = this.inst.buffer?.active;
    let lastLine = '';
    if (buffer) {
      for (let i = buffer.viewportY + buffer.cursorY; i >= 0; i--) {
        lastLine = (buffer.getLine(i)?.translateToString() || '').trimEnd() + lastLine;
        if (lastLine.length > value.length) {
          break;
        }
      }
    }
    // 边界检查
    const lastLineLen = lastLine.length;
    const spinPartLen = value.length;
    let checkEnd;
    if (spinPartLen >= lastLineLen) {
      checkEnd = lastLineLen;
    } else {
      checkEnd = spinPartLen;
    }
    // 获取缺失的数据
    let append = undefined;
    for (let i = 0; i < checkEnd; i++) {
      if (lastLine.endsWith(value.substring(0, checkEnd - i))) {
        append = value.substring(checkEnd - i, spinPartLen);
        break;
      }
    }
    // 全部缺失
    if (append == undefined) {
      append = value;
    }
    // 追加
    this.pasteOrigin(append, focus);
  }

}
