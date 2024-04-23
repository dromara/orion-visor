import type { ITerminalPanelManager } from '../types/terminal.type';
import TerminalPanelTabManager from '../handler/terminal-panel-tab-manager';

// 终端面板管理器实现
export default class TerminalPanelManager implements ITerminalPanelManager {

  // 当前面板
  active: number;
  // 面板列表
  panels: Array<TerminalPanelTabManager>;

  constructor() {
    this.active = 0;
    this.panels = [new TerminalPanelTabManager()];
  }

  // 获取当前面板
  getCurrentPanel(): TerminalPanelTabManager {
    return this.panels[this.active];
  }

  // 设置当前面板
  setCurrentPanel(active: number): void {
    this.active = active;
  };

  // 获取面板
  getPanel(index: number): TerminalPanelTabManager {
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
    this.panels = [new TerminalPanelTabManager()];
  };

}
