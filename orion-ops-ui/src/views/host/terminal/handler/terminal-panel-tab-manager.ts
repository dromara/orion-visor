import type { ITerminalTabManager, TerminalPanelTabItem } from '../types/terminal.type';

// 终端面板 tab 管理器实现
export default class TerminalPanelTabManager implements ITerminalTabManager<TerminalPanelTabItem> {

  public active: string;

  public items: Array<TerminalPanelTabItem>;

  constructor(def: TerminalPanelTabItem | undefined = undefined) {
    if (def) {
      this.active = def.sessionId;
      this.items = [def];
    } else {
      this.active = undefined as unknown as string;
      this.items = [];
    }
  }

  // 获取当前 tab
  getCurrentTab() {
    if (!this.active) {
      return undefined;
    }
    return this.items.find(s => s.sessionId === this.active);
  }

  // 获取 tab
  getTab(sessionId: string): TerminalPanelTabItem {
    return this.items.find(s => s.sessionId === sessionId) as TerminalPanelTabItem;
  }

  // 点击 tab
  clickTab(sessionId: string): void {
    this.active = sessionId;
  }

  // 删除 tab
  deleteTab(sessionId: string): void {
    // 获取当前 tab
    const tabIndex = this.items.findIndex(s => s.sessionId === sessionId);
    // 删除 tab
    this.items.splice(tabIndex, 1);
    if (sessionId === this.active && this.items.length !== 0) {
      // 切换为前一个 tab
      this.active = this.items[Math.max(tabIndex - 1, 0)].sessionId;
    } else {
      this.active = undefined as unknown as string;
    }
  }

  // 打开 tab
  openTab(tab: TerminalPanelTabItem): void {
    // 不存在则创建 tab
    if (!this.items.find(s => s.sessionId === tab.sessionId)) {
      this.items.push(tab);
    }
    this.active = tab.sessionId;
  }

  // 切换到前一个 tab
  changeToPrevTab() {
    this.changeToIndex(this.getCurrentTabIndex() - 1);
  }

  // 切换到后一个 tab
  changeToNextTab() {
    this.changeToIndex(this.getCurrentTabIndex() + 1);
  }

  // 切换索引 tab
  changeToIndex(index: number) {
    if (index < 0 || index >= this.items.length) {
      return;
    }
    // 切换 tab
    this.active = this.items[index].sessionId;
  }

  // 获取当前索引
  private getCurrentTabIndex(): number {
    return this.items.findIndex(s => s.sessionId === this.active);
  }

  // 清空
  clear() {
    this.active = undefined as unknown as string;
    this.items = [];
  }

}
