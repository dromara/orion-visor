import type { PathBookmarkQueryResponse } from './path-bookmark';
import axios from 'axios';

/**
 * 路径书签分组创建请求
 */
export interface PathBookmarkGroupCreateRequest {
  name?: string;
}

/**
 * 路径书签分组更新请求
 */
export interface PathBookmarkGroupUpdateRequest extends PathBookmarkGroupCreateRequest {
  id?: number;
}

/**
 * 路径书签分组查询响应
 */
export interface PathBookmarkGroupQueryResponse {
  id: number;
  name: string;
  items: Array<PathBookmarkQueryResponse>;
}

/**
 * 创建路径书签分组
 */
export function createPathBookmarkGroup(request: PathBookmarkGroupCreateRequest) {
  return axios.post('/asset/path-bookmark-group/create', request);
}

/**
 * 更新路径书签分组
 */
export function updatePathBookmarkGroup(request: PathBookmarkGroupUpdateRequest) {
  return axios.put('/asset/path-bookmark-group/update', request);
}

/**
 * 查询全部路径书签分组
 */
export function getPathBookmarkGroupList() {
  return axios.get<Array<PathBookmarkGroupQueryResponse>>('/asset/path-bookmark-group/list');
}

/**
 * 删除路径书签分组
 */
export function deletePathBookmarkGroup(id: number) {
  return axios.delete('/asset/path-bookmark-group/delete', { params: { id } });
}

