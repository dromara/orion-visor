import axios from 'axios';

/**
 * 主机分组创建请求
 */
export interface HostGroupCreateRequest {
  parentId?: number;
  name?: string;
}

/**
 * 主机分组重命名请求
 */
export interface HostGroupRenameRequest {
  id?: number;
  name?: string;
}

/**
 * 主机分组移动请求
 */
export interface HostGroupMoveRequest {
  id?: number;
  targetId?: number;
  position?: number;
}

/**
 * 主机分组查询响应
 */
export interface HostGroupQueryResponse {
  key: number;
  parentId: number;
  title: string;
  children: Array<HostGroupQueryResponse>;
  hosts: Array<number>;
}

/**
 * 分组内主机修改请求
 */
export interface HostGroupRelUpdateRequest {
  groupId?: number;
  hostIdList?: Array<string>;
}

/**
 * 主机分组授权 查询请求对象
 */
export interface HostGroupGrantQueryRequest {
  userId?: number;
  roleId?: number;
}

/**
 * 主机分组 授权请求对象
 */
export interface HostGroupGrantRequest extends HostGroupGrantQueryRequest {
  groupIdList?: Array<number>;
}

/**
 * 创建主机分组
 */
export function createHostGroup(request: HostGroupCreateRequest) {
  return axios.post<number>('/asset/host-group/create', request);
}

/**
 * 更新主机分组名称
 */
export function updateHostGroupName(request: HostGroupRenameRequest) {
  return axios.put('/asset/host-group/rename', request);
}

/**
 * 移动主机分组
 */
export function moveHostGroup(request: HostGroupMoveRequest) {
  return axios.put('/asset/host-group/move', request);
}

/**
 * 查询主机分组树
 */
export function getHostGroupTree() {
  return axios.get<Array<HostGroupQueryResponse>>('/asset/host-group/tree');
}

/**
 * 删除主机分组
 */
export function deleteHostGroup(id: number) {
  return axios.delete('/asset/host-group/delete', { params: { id } });
}

/**
 * 查询分组内主机
 */
export function getHostGroupRelList(groupId: number) {
  return axios.get<Array<number>>('/asset/host-group/rel-list', { params: { groupId } });
}

/**
 * 修改分组内主机
 */
export function updateHostGroupRel(request: HostGroupRelUpdateRequest) {
  return axios.put('/asset/host-group/update-rel', request);
}

/**
 * 获取已授权的分组
 */
export function getAuthorizedHostGroup(params: HostGroupGrantQueryRequest) {
  return axios.get<Array<number>>('/asset/host-group/get-authorized-group', { params });
}

/**
 * 主机分组授权
 */
export function grantHostGroup(request: HostGroupGrantRequest) {
  return axios.put('/asset/host-group/grant', request);
}
