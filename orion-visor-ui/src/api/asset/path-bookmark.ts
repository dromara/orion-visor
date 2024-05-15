import { PathBookmarkGroupQueryResponse } from '@/api/asset/path-bookmark-group';
import axios from 'axios';

/**
 * 路径标签创建请求
 */
export interface PathBookmarkCreateRequest {
  groupId?: number;
  name?: string;
  type?: string;
  path?: string;
}

/**
 * 路径标签更新请求
 */
export interface PathBookmarkUpdateRequest extends PathBookmarkCreateRequest {
  id?: number;
}

/**
 * 路径标签查询响应
 */
export interface PathBookmarkQueryResponse extends PathBookmarkQueryResponseExtra {
  id: number;
  groupId: number;
  name: string;
  type: string;
  path: string;
}

export interface PathBookmarkQueryResponseExtra {
  visible: boolean;
  expand?: boolean;
}

/**
 * 路径标签查询响应
 */
export interface PathBookmarkWrapperResponse {
  groups: Array<PathBookmarkGroupQueryResponse>;
  ungroupedItems: Array<PathBookmarkQueryResponse>;
}

/**
 * 创建路径标签
 */
export function createPathBookmark(request: PathBookmarkCreateRequest) {
  return axios.post('/asset/path-bookmark/create', request);
}

/**
 * 更新路径标签
 */
export function updatePathBookmark(request: PathBookmarkUpdateRequest) {
  return axios.put('/asset/path-bookmark/update', request);
}

/**
 * 分页查询路径标签
 */
export function getPathBookmarkList() {
  return axios.get<PathBookmarkWrapperResponse>('/asset/path-bookmark/list');
}

/**
 * 删除路径标签
 */
export function deletePathBookmark(id: number) {
  return axios.delete('/asset/path-bookmark/delete', { params: { id } });
}
