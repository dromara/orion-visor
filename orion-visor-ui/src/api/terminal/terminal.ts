import type { TerminalTheme } from '@/views/terminal/interfaces';
import axios from 'axios';
import { createAppWebSocket } from '@/utils/http';

// 终端访问请求
export interface TerminalAccessRequest {
  hostId?: number;
  connectType?: string;
  extra?: Record<string, any>;
}

/**
 * 获取主机终端主题
 */
export function getTerminalThemes() {
  return axios.get<Array<TerminalTheme>>('/terminal/terminal/themes');
}

/**
 * 获取主机终端 accessToken
 */
export function getTerminalAccessToken(request: TerminalAccessRequest) {
  return axios.post<string>('/terminal/terminal/access', request);
}

/**
 * 获取主机终端 transferToken
 */
export function getTerminalTransferToken() {
  return axios.get<string>('/terminal/terminal/transfer');
}

/**
 * 打开主机终端 websocket
 */
export const openTerminalAccessChannel = (protocol: string, accessToken: string) => {
  return createAppWebSocket(`/terminal/access/${protocol}/${accessToken}`);
};

/**
 * 打开主机传输 websocket
 */
export const openTerminalTransferChannel = (accessToken: string) => {
  return createAppWebSocket(`/terminal/transfer/${accessToken}`);
};

