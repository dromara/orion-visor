import type { TerminalInteractSetting, TerminalShortcutKey } from '@/store/modules/terminal/types';
import type { ITerminalSession, ITerminalSessionHandler, ITerminalTabManager, TerminalDomRef } from '../types/terminal.type';
import type { Terminal } from 'xterm';
import useCopy from '@/hooks/copy';
import { useTerminalStore } from '@/store';
import { InnerTabs } from '../types/terminal.const';

const { copy: copyValue, readText } = useCopy();

// 终端会话处理器实现
export default class TerminalSessionHandler implements ITerminalSessionHandler {

  private readonly domRef: TerminalDomRef;

  private readonly inst: Terminal;

  private readonly session: ITerminalSession;

  private readonly interactSetting: TerminalInteractSetting;

  private readonly shortcutKeys: Array<TerminalShortcutKey>;

  private readonly tabManager: ITerminalTabManager;

  constructor(session: ITerminalSession,
              domRef: TerminalDomRef) {
    this.session = session;
    this.inst = session.inst;
    this.domRef = domRef;
    const { preference, tabManager } = useTerminalStore();
    this.interactSetting = preference.interactSetting;
    this.shortcutKeys = preference.shortcutSetting.keys;
    this.tabManager = tabManager;
  }

  // 启用状态
  enabledStatus(option: string): boolean {
    switch (option) {
      case 'paste':
      case 'pasteTrimEnd':
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

  // 触发快捷键
  triggerShortcutKey(e: KeyboardEvent): boolean {
    // 检测触发的快捷键
    const key = this.shortcutKeys.find(key => {
      return key.code === e.code
        && key.altKey === e.altKey
        && key.shiftKey === e.shiftKey
        && key.ctrlKey === e.ctrlKey;
    });
    if (key) {
      // 调用处理方法
      this.invokeHandle.call(this, key.item);
      return false;
    } else {
      return true;
    }
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

  // 粘贴
  paste() {
    readText().then(s => this.pasteTrimEnd(s));
  }

  // 粘贴并且去除尾部空格 (如果配置)
  pasteTrimEnd(value: string) {
    if (this.interactSetting.pasteAutoTrim) {
      // 粘贴前去除尾部空格
      this.inst.paste(value.trimEnd());
    } else {
      this.inst.paste(value);
    }
    this.inst.focus();
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
    this.domRef.editorModal?.open('', '');
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

  // 关闭 tab
  closeTab() {
    this.tabManager.deleteTab(this.session.sessionId);
  }

  // 切换到前一个 tab
  changeToPrevTab() {
    this.tabManager.changeToPrevTab();
  }

  // 切换到后一个 tab
  changeToNextTab() {
    this.tabManager.changeToNextTab();
  }

  // 复制终端 tab
  openCopyTerminalTab() {
    useTerminalStore().openCopyTerminal(this.session.hostId);
  }

  // 打开新建连接 tab
  openNewConnectTab() {
    this.tabManager.openTab(InnerTabs.NEW_CONNECTION);
  }

}
