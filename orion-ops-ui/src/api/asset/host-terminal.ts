import axios from 'axios';

/**
 * 主机终端访问响应
 */
export interface HostTerminalAccessResponse {
  accessToken: string;
  sessionInitial: string;
}

/**
 * 获取主机终端 accessToken
 */
export function getHostTerminalAccessToken() {
  return axios.get<HostTerminalAccessResponse>('/asset/host-terminal/access');
}
