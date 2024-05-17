import type { HostGroupQueryResponse } from '@/api/asset/host-group';
import type { HostQueryResponse } from './host';
import type { HostKeyQueryResponse } from './host-key';
import type { HostIdentityQueryResponse } from './host-identity';
import axios from 'axios';

/**
 * 已授权的主机 查询响应
 */
export interface AuthorizedHostQueryResponse {
  groupTree: Array<HostGroupQueryResponse>;
  hostList: Array<HostQueryResponse>;
  treeNodes: Record<string, Array<number>>;
  latestHosts: Array<number>;
}

/**
 * 查询当前用户已授权的主机
 */
export function getCurrentAuthorizedHost(type: string) {
  return axios.get<AuthorizedHostQueryResponse>('/asset/authorized-data/current-host', { params: { type } });
}

/**
 * 查询当前用户已授权的主机密钥
 */
export function getCurrentAuthorizedHostKey() {
  return axios.get<Array<HostKeyQueryResponse>>('/asset/authorized-data/current-host-key');
}

/**
 * 查询当前用户已授权的主机身份
 */
export function getCurrentAuthorizedHostIdentity() {
  return axios.get<Array<HostIdentityQueryResponse>>('/asset/authorized-data/current-host-identity');
}
