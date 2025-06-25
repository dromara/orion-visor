import axios from 'axios';

/**
 * 主机拓展信息查询请求
 */
export interface HostExtraQueryRequest {
  hostId?: number;
  item: string;
}

/**
 * 主机拓展信息更新请求
 */
export interface HostExtraUpdateRequest {
  hostId?: number;
  item: string;
  extra: string;
}

// SSH 额外配置
export interface HostSshExtraSettingModel {
  authType: string;
  username: string;
  keyId: number;
  identityId: number;
}

// RDP 额外配置
export interface HostRdpExtraSettingModel {
  authType: string;
  identityId: number;
  lowBandwidthMode: boolean;
}

// 标签额外配置
export interface HostLabelExtraSettingModel {
  alias: string;
  color: string;
}

// 标签规格模型
export interface HostSpecExtraModel {
  sn: string;
  osName: string;
  cpuCore: number;
  cpuFrequency: number;
  cpuModel: string;
  memorySize: number;
  diskSize: number;
  inBandwidth: number;
  outBandwidth: number;
  publicIpAddress: Array<string>;
  privateIpAddress: Array<string>;
  chargePerson: string;
  createdTime: number;
  expiredTime: number;
  items: Array<{
    label: string;
    value: string;
  }>;
}

/**
 * 获取主机拓展信息
 */
export function getHostExtraItem<T>(params: HostExtraQueryRequest) {
  return axios.get<T>('/asset/host-extra/get', { params });
}

/**
 * 修改主机拓展信息
 */
export function updateHostExtra(request: HostExtraUpdateRequest) {
  return axios.put('/asset/host-extra/update', request);
}
