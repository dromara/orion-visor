import axios from 'axios';

/**
 * 主机拓展信息查询请求
 */
export interface HostExtraQueryRequest {
  hostId?: number;
  item: string;
  items?: Array<string>;
}

/**
 * 主机拓展信息更新请求
 */
export interface HostExtraUpdateRequest {
  hostId?: number;
  item: string;
  extra: string;
}

/**
 * 获取主机拓展信息
 */
export function getHostExtraItem<T>(params: HostExtraQueryRequest) {
  return axios.get<T>('/asset/host-extra/get', { params });
}

/**
 * 获取多个主机拓展信息
 */
export function getHostExtraItemList(request: HostExtraQueryRequest) {
  return axios.post<Record<string, Record<string, any>>>('/asset/host-extra/list', request);
}

/**
 * 修改主机拓展信息
 */
export function updateHostExtra(request: HostExtraUpdateRequest) {
  return axios.put('/asset/host-extra/update', request);
}
