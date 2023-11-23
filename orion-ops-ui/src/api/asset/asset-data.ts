import type { HostGroupQueryResponse } from '@/api/asset/host-group';
import axios from 'axios';

/**
 * 查询已授权的主机分组
 */
export function getAuthorizedHostGroup() {
  return axios.get<Array<HostGroupQueryResponse>>('/asset/authorized-data/host-group');
}
