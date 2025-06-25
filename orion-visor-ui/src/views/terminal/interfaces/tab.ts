// 终端 tab 元素
export interface TerminalTabItem {
  key: string;
  title: string;
  icon: string;
  extra?: Record<string, any>;

  [key: string]: unknown;
}

// 终端会话 tab 元素
export interface TerminalSessionTabItem extends TerminalTabItem {
  name: string;
  type: string;
  seq: number;
  panelIndex: number;
  hostId: number;
  address: string;
  color?: string;
}

// 终端 tab 管理器定义
export interface ITerminalTabManager<T extends TerminalTabItem = TerminalTabItem> {
  // 当前 tab
  active: string;
  // 全部 tab
  items: Array<T>;

  // 获取当前 tab
  getCurrentTab: () => T | undefined;
  // 获取 tab
  getTab: (key: string) => T;
  // 点击 tab
  clickTab: (key: string) => void;
  // 删除 tab
  deleteTab: (key: string) => void;
  // 打开 tab
  openTab: (tab: T) => void;
  // 切换到前一个 tab
  changeToPrevTab: () => void;
  // 切换到后一个 tab
  changeToNextTab: () => void;
  // 切换索引 tab
  changeToIndex: (index: number) => void;
  // 清空
  clear: () => void;
}

// 终端面板管理器定义
export interface ITerminalPanelManager {
  // 当前面板
  active: number;
  // 面板列表
  panels: Array<ITerminalTabManager<TerminalSessionTabItem>>;

  // 获取当前面板
  getCurrentPanel: () => ITerminalTabManager<TerminalSessionTabItem>;
  // 设置当前面板
  setCurrentPanel: (active: number) => void;
  // 获取面板
  getPanel: (index: number) => ITerminalTabManager<TerminalSessionTabItem>;
  // 移除面板
  removePanel: (index: number) => void;
  // 重置
  reset: () => void;
}
