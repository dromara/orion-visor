import type { Ref } from 'vue';

export interface TerminalState {
  isDarkTheme: Ref<boolean>;
  preference: TerminalPreference;
  dispatcher: ITerminalDispatcher;
}

// 终端配置
export interface TerminalPreference {
  darkTheme: string;
  newConnectionType: string;
  displaySetting: TerminalDisplaySetting;
  themeSchema: TerminalThemeSchema;
}

// 显示设置
export interface TerminalDisplaySetting {
  fontFamily?: string;
  fontSize?: number;
  lineHeight?: number;
  fontWeight?: string | number;
  fontWeightBold?: string | number;
  cursorStyle?: string;
  cursorBlink?: boolean;
}

// 终端主题
export interface TerminalThemeSchema {
  name: string;
  dark: boolean;
  background: string;
  foreground: string;
  cursor: string;
  cursorAccent?: string;
  selectionBackground?: string;
  selectionForeground?: string;
  selectionInactiveBackground?: string;
  black: string;
  red: string;
  green: string;
  yellow: string;
  blue: string;
  magenta: string;
  cyan: string;
  white: string;
  brightBlack: string;
  brightRed: string;
  brightGreen: string;
  brightYellow: string;
  brightBlue: string;
  brightMagenta: string;
  brightCyan: string;
  brightWhite: string;

  [key: string]: unknown;
}

// 终端 tab 元素
export interface TerminalTabItem {
  key: string;
  title: string;
  type: string;

  [key: string]: unknown;
}

// 终端调度器
export interface ITerminalDispatcher {
  // 当前活跃 tab
  active: string;
  // 所有 tab
  items: Array<TerminalTabItem>;

  // 点击 tab
  clickTab: (key: string) => void;
  // 删除 tab
  deleteTab: (key: string) => void;
  // 打开 tab
  openTab: (tab: TerminalTabItem) => void;
  // 打开终端
  openTerminal: (record: any) => void;
  // 注册终端钩子
  registerTerminalHook: (tab: TerminalTabItem) => void;

  // 重置
  reset: () => void;
}
