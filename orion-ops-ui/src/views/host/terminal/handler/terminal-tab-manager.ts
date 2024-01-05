import type { ITerminalTabManager, TerminalTabItem } from '@/store/modules/terminal/types';
import { InnerTabs } from '@/views/host/terminal/types/terminal.const';

// 终端 tab 管理器实现
export default class TerminalTabManager implements ITerminalTabManager {

  public active: string;

  public items: Array<TerminalTabItem>;

  constructor() {
    this.active = InnerTabs.NEW_CONNECTION.key;
    this.items = [InnerTabs.NEW_CONNECTION];
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
    }
    // fixme 关闭 ws
  }

  // 打开 tab
  openTab(tab: TerminalTabItem): void {
    // 不存在则创建 tab
    if (!this.items.find(s => s.key === tab.key)) {
      this.items.push(tab);
    }
    this.active = tab.key;
  }

  // 清空
  clear() {
    this.active = undefined as unknown as string;
    this.items = [];
  }

}
