import type { HostGroupQueryResponse } from '@/api/asset/host-group';
import type { HostQueryResponse } from './host';
import type { HostKeyQueryResponse } from './host-key';
import type { HostIdentityQueryResponse } from './host-identity';
import axios from 'axios';

/**
 * 已授权的主机分组 查询响应
 */
export interface AuthorizedHostGroupQueryResponse {
  groupTree: Array<HostGroupQueryResponse>;
  hostList: Array<HostQueryResponse>;
}

/**
 * 查询当前用户已授权的主机分组
 */
export function getCurrentAuthorizedHostGroup() {
  return axios.get<AuthorizedHostGroupQueryResponse>('/asset/authorized-data/current-host-group');
}

/**
 * 查询当前用户已授权的主机秘钥
 */
export function getCurrentAuthorizedHostKey() {
  return axios.get<HostKeyQueryResponse>('/asset/authorized-data/current-host-key');
}

/**
 * 查询当前用户已授权的主机身份
 */
export function getCurrentAuthorizedHostIdentity() {
  return axios.get<HostIdentityQueryResponse>('/asset/authorized-data/current-host-identity');
}
