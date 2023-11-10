import axios from 'axios';

/**
 * 主机分组创建请求
 */
export interface HostGroupCreateRequest {
  parentId?: number;
  name?: string;
}

/**
 * 主机分组更新请求
 */
export interface HostGroupUpdateRequest extends HostGroupCreateRequest {
  id?: number;
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
  id: number;
  parentId: number;
  name: string;
  sort: number;
  children: Array<HostGroupQueryResponse>;
}

/**
 * 创建主机分组
 */
export function createHostGroup(request: HostGroupCreateRequest) {
  return axios.post('/asset/host-group/create', request);
}

/**
 * 更新主机分组
 */
export function updateHostGroup(request: HostGroupUpdateRequest) {
  return axios.put('/asset/host-group/update', request);
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
