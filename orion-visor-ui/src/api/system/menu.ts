import type { TableData } from '@arco-design/web-vue/es/table/interface';
import axios from 'axios';

/**
 * 菜单创建请求
 */
export interface MenuCreateRequest {
  parentId?: number;
  name?: string;
  permission?: string;
  type?: number;
  sort?: number;
  visible?: number;
  cache?: number;
  newWindow?: number;
  icon?: string;
  path?: string;
  component?: string;
}

/**
 * 菜单更新请求
 */
export interface MenuUpdateRequest extends MenuCreateRequest {
  id?: number;
  status?: number;
}

/**
 * 菜单查询请求
 */
export interface MenuQueryRequest {
  name?: string;
  status?: number;
}

/**
 * 菜单查询响应
 */
export interface MenuQueryResponse extends TableData {
  id: number;
  parentId: number;
  name: string;
  permission: string;
  type: number;
  sort: number;
  visible: number;
  status: number;
  cache: number;
  newWindow: number,
  icon: string;
  path: string;
  component: string;
  children: Array<MenuQueryResponse>;
}

/**
 * 查询菜单列表
 */
export function getMenuList(request: MenuQueryRequest) {
  return axios.post<MenuQueryResponse[]>('/infra/system-menu/list', request);
}

/**
 * 创建菜单
 */
export function createMenu(request: MenuCreateRequest) {
  return axios.post('/infra/system-menu/create', request);
}

/**
 * 修改菜单
 */
export function updateMenu(request: MenuUpdateRequest) {
  return axios.put<MenuQueryResponse[]>('/infra/system-menu/update', request);
}

/**
 * 修改菜单状态
 */
export function updateMenuStatus(request: MenuUpdateRequest) {
  return axios.put<MenuQueryResponse[]>('/infra/system-menu/update-status', request);
}

/**
 * 删除菜单
 */
export function deleteMenu(id: number) {
  return axios.delete<MenuQueryResponse[]>('/infra/system-menu/delete', { params: { id } });
}

/**
 * 刷新缓存
 */
export function refreshCache() {
  return axios.put('/infra/user-permission/refresh-cache');
}
