import axios from 'axios';

/**
 * 获取主机终端 accessToken
 */
export function getHostTerminalAccessToken() {
  return axios.get<string>('/asset/host-terminal/access');
}
