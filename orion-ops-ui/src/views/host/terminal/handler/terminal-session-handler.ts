import type { TerminalPreference } from '@/store/modules/terminal/types';
import type { ITerminalSession, ITerminalSessionHandler, ITerminalTabManager, TerminalDomRef } from '../types/terminal.type';
import type { Terminal } from 'xterm';
import { useTerminalStore } from '@/store';
import useCopy from '@/hooks/copy';

const { copy: copyValue, readText } = useCopy();

// 终端会话处理器实现
export default class TerminalSessionHandler implements ITerminalSessionHandler {

  private readonly domRef: TerminalDomRef;

  private readonly inst: Terminal;

  private readonly session: ITerminalSession;

  private readonly preference: TerminalPreference;

  private readonly tabManager: ITerminalTabManager;

  constructor(session: ITerminalSession,
              domRef: TerminalDomRef) {
    this.session = session;
    this.inst = session.inst;
    this.domRef = domRef;
    const { preference, tabManager } = useTerminalStore();
    this.preference = preference;
    this.tabManager = tabManager;
  }

  // 启用状态
  enabledStatus(option: string): boolean {
    switch (option) {
      case 'paste':
      case 'interrupt':
      case 'enter':
      case 'commandEditor':
        return this.session.canWrite;
      case 'disconnect':
        return this.session.connected;
      default:
        return true;
    }
  }

  // 复制选中
  copy() {
    let selection = this.inst.getSelection();
    if (selection) {
      // 去除尾部空格
      if (this.preference.interactSetting.copyAutoTrim) {
        selection = selection.trimEnd();
      }
      // 复制
      copyValue(selection, false);
    }
    // 聚焦
    this.inst.focus();
  }

  // 粘贴
  paste() {
    if (this.enabledStatus('paste')) {
      readText().then(s => this.pasteTrimEnd(s));
    }
  }

  // 粘贴并且去除尾部空格 (如果配置)
  pasteTrimEnd(value: string) {
    if (this.enabledStatus('paste')) {
      if (this.preference.interactSetting.pasteAutoTrim) {
        // 粘贴前去除尾部空格
        this.inst.paste(value.trimEnd());
      } else {
        this.inst.paste(value);
      }
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
    this.domRef.searchModal?.toggle();
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
    if (this.session.connected) {
      this.session.fit();
      this.inst.focus();
    }
  }

  // 打开命令编辑器
  commandEditor() {
    if (this.enabledStatus('commandEditor')) {
      this.domRef.editorModal?.open('', '');
    }
  }

  // ctrl + c
  interrupt() {
    if (this.enabledStatus('interrupt')) {
      this.inst.paste(String.fromCharCode(3));
    }
  }

  // 回车
  enter() {
    if (this.enabledStatus('enter')) {
      this.inst.paste(String.fromCharCode(13));
    }
  }

  // 清空
  clear() {
    this.inst.clear();
  }

  // 断开连接
  disconnect() {
    if (this.enabledStatus('disconnect')) {
      this.session.disconnect();
    }
  }

  // 关闭
  close() {
    this.tabManager.deleteTab(this.session.sessionId);
  }

  // 聚焦
  focus() {
    this.inst.focus();
  }

}
