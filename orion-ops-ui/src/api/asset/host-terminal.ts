import axios from 'axios';

// 终端主题
export interface TerminalTheme {
  name: string;
  dark: boolean;
  schema: TerminalThemeSchema;
}

// 终端主题 schema
export interface TerminalThemeSchema {
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

/**
 * 获取主机终端主题
 */
export function getTerminalThemes() {
  return axios.get<Array<TerminalTheme>>('/asset/host-terminal/themes');
}

/**
 * 获取主机终端 accessToken
 */
export function getTerminalAccessToken() {
  return axios.get<string>('/asset/host-terminal/access');
}
