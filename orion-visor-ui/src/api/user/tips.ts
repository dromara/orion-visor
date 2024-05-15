import axios from 'axios';

/**
 * 修改为已提示
 */
export function setTipsTipped(key: string) {
  return axios.put('/infra/tips/tipped', null, { params: { key } });
}
