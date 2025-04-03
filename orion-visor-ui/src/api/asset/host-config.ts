import axios from 'axios';

/**
 * 主机更新查询请求
 */
export interface HostUpdateQueryRequest {
  hostId: number;
  type: string;
}

/**
 * 主机更新配置请求
 */
export interface HostUpdateConfigRequest {
  hostId: number;
  type: string;
  config: string;
}

export interface HostBaseConfig {
  username?: string;
  port?: number;
  password?: string;
  authType?: string;
  useNewPassword?: boolean;
  hasPassword?: boolean;
}

// 主机 SSH 配置
export interface HostSshConfig extends HostBaseConfig {
  keyId?: number;
  identityId?: number;
  connectTimeout?: number;
  charset?: string;
  fileNameCharset?: string;
  fileContentCharset?: string;
}

/**
 * 更新主机配置
 */
export function updateHostConfig(request: HostUpdateConfigRequest) {
  return axios.put('/asset/host-config/update', request);
}

/**
 * 查询主机配置
 */
export function getHostConfig<T>(request: HostUpdateQueryRequest) {
  return axios.post<T>('/asset/host-config/get', request);
}
