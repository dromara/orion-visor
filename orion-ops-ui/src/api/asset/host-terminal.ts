import axios from 'axios';
import { createAppWebSocket } from '@/utils/http';

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

/**
 * 打开主机终端 websocket
 */
export const openHostTerminalChannel = (accessToken: string) => {
  return createAppWebSocket(`/host/terminal/${accessToken}`);
};

/**
 * 打开主机传输 websocket
 */
export const openHostTransferChannel = (accessToken: string) => {
  return createAppWebSocket(`/host/transfer/${accessToken}`);
};
