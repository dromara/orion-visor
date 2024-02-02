import type { ITerminalPanelManager, TerminalPanelTabItem } from '../types/terminal.type';
import TerminalTabManager from '../handler/terminal-tab-manager';

// 终端面板管理器实现
export default class TerminalPanelManager<T extends TerminalPanelTabItem = TerminalPanelTabItem> implements ITerminalPanelManager<T> {

  // 当前面板
  active: number;
  // 面板列表
  panels: Array<TerminalTabManager<T>>;

  constructor() {
    this.active = 0;
    this.panels = [new TerminalTabManager()];
  }

  // 获取当前面板
  getCurrentPanel(): TerminalTabManager<T> {
    return this.panels[this.active];
  }

  // 设置当前面板
  setCurrentPanel(active: number): void {
    this.active = active;
  };

  // 获取面板
  getPanel(index: number): TerminalTabManager<T> {
    return this.panels[index];
  };

  // 移除面板
  removePanel(index: number) {
    this.panels.splice(index, 1);
    this.active = index >= this.panels.length ? this.panels.length - 1 : index;
  };

  // 重置
  reset() {
    for (let panel of this.panels) {
      panel.clear();
    }
    this.active = 0;
    this.panels = [new TerminalTabManager()];
  };

}
