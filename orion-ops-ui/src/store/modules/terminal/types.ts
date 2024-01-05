import type { Ref } from 'vue';
import type { ITerminalTabManager, ITerminalSessionManager } from '@/views/host/terminal/types/terminal.type';

export interface TerminalState {
  isDarkTheme: Ref<boolean>;
  preference: TerminalPreference;
  tabManager: ITerminalTabManager;
  sessionManager: ITerminalSessionManager;
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

