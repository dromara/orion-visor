import type { Ref } from 'vue';

export interface TerminalState {
  isDarkTheme: Ref<boolean>;
  preference: TerminalPreference;
}

// 终端配置
export interface TerminalPreference {
  darkTheme: string,
  themeSchema: TerminalThemeSchema,
  displaySetting: TerminalDisplaySetting,
}

// 终端主题
export interface TerminalThemeSchema {
  name: string;
  dark: boolean;
  background: string;
  foreground: string;
  cursor: string;
  cursorAccent?: string;
  selectionInactiveBackground?: string;
  selectionBackground?: string;
  selectionForeground?: string;
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
