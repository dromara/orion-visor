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
 * 主机分组查询请求
 */
export interface HostGroupQueryRequest {
  searchValue?: string;
  id?: number;
  parentId?: number;
  name?: string;
  type?: string;
  sort?: number;
}

/**
 * 主机分组查询响应
 */
export interface HostGroupQueryResponse {
  key: number;
  title: string;
  children: Array<HostGroupQueryResponse>;
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
 * 查询全部主机分组树
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
