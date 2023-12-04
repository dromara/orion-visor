import axios from 'axios';

/**
 * 数据授权 请求对象
 */
export interface AssetDataGrantRequest {
  userId?: number;
  roleId?: number;
  idList?: Array<number>;
}

/**
 * 授权数据 查询请求对象
 */
export interface AssetAuthorizedDataQueryRequest {
  userId?: number;
  roleId?: number;
}

/**
 * 主机分组授权
 */
export function grantHostGroup(request: AssetDataGrantRequest) {
  return axios.put('/asset/data-grant/grant-host-group', request);
}

/**
 * 获取已授权的主机分组
 */
export function getAuthorizedHostGroup(params: AssetAuthorizedDataQueryRequest) {
  return axios.get<Array<number>>('/asset/data-grant/get-host-group', { params });
}

/**
 * 主机秘钥授权
 */
export function grantHostKey(request: AssetDataGrantRequest) {
  return axios.put('/asset/data-grant/grant-host-key', request);
}

/**
 * 获取已授权的主机秘钥
 */
export function getAuthorizedHostKey(params: AssetAuthorizedDataQueryRequest) {
  return axios.get<Array<number>>('/asset/data-grant/get-host-key', { params });
}

/**
 * 主机身份授权
 */
export function grantHostIdentity(request: AssetDataGrantRequest) {
  return axios.put('/asset/data-grant/grant-host-identity', request);
}

/**
 * 获取已授权的主机身份
 */
export function getAuthorizedHostIdentity(params: AssetAuthorizedDataQueryRequest) {
  return axios.get<Array<number>>('/asset/data-grant/get-host-identity', { params });
}
