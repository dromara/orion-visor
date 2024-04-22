import type { ITerminalTabManager, TerminalTabItem } from '../types/terminal.type';

// 终端 tab 管理器实现
export default class TerminalTabManager<T extends TerminalTabItem = TerminalTabItem> implements ITerminalTabManager<T> {

  public active: string;

  public items: Array<T>;

  constructor(def: T | undefined = undefined) {
    if (def) {
      this.active = def.key;
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
    return this.items.find(s => s.key === this.active);
  }

  // 获取 tab
  getTab(key: string): T {
    return this.items.find(s => s.key === key) as T;
  }

  // 点击 tab
  clickTab(key: string): void {
    this.active = key;
  }

  // 删除 tab
  deleteTab(key: string): void {
    // 获取当前 tab
    const tabIndex = this.items.findIndex(s => s.key === key);
    // 删除 tab
    this.items.splice(tabIndex, 1);
    if (key === this.active && this.items.length !== 0) {
      // 切换为前一个 tab
      this.active = this.items[Math.max(tabIndex - 1, 0)].key;
    } else {
      this.active = undefined as unknown as string;
    }
  }

  // 打开 tab
  openTab(tab: T): void {
    // 不存在则创建 tab
    if (!this.items.find(s => s.key === tab.key)) {
      this.items.push(tab);
    }
    this.active = tab.key;
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
    this.active = this.items[index].key;
  }

  // 获取当前索引
  private getCurrentTabIndex(): number {
    return this.items.findIndex(s => s.key === this.active);
  }

  // 清空
  clear() {
    this.active = undefined as unknown as string;
    this.items = [];
  }

}
