import axios from 'axios';

/**
 * 主机别名修改请求
 */
export interface HostAliasUpdateRequest {
  id?: number;
  name?: string;
}

/**
 * 修改主机别名
 */
export function updateHostAlias(request: HostAliasUpdateRequest) {
  return axios.put('/asset/host-extra/update-alias', request);
}
