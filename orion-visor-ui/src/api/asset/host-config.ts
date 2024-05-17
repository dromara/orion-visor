import axios from 'axios';

/**
 * 主机配置请求
 */
export interface HostConfigRequest {
  hostId?: number;
  type?: string;
  version?: number;
  status?: number;
  config?: string;
}

/**
 * 主机配置查询响应
 */
export interface HostConfigQueryResponse {
  id: number;
  hostId: number;
  type: string;
  version: number;
  status: number;
  config: Record<string, any>;
}

/**
 * 查询主机配置
 */
export function getHostConfig(params: HostConfigRequest) {
  return axios.get<HostConfigQueryResponse>('/asset/host-config/get', { params });
}

/**
 * 查询全部主机配置
 */
export function getHostConfigList(hostId: number) {
  return axios.get<Array<HostConfigQueryResponse>>('/asset/host-config/list', { params: { hostId } });
}

/**
 * 更新主机配置
 */
export function updateHostConfig(request: HostConfigRequest) {
  return axios.put('/asset/host-config/update', request);
}

/**
 * 更新主机配置状态
 */
export function updateHostConfigStatus(request: HostConfigRequest) {
  return axios.put('/asset/host-config/update-status', request);
}
