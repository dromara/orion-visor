import type { MenuQueryResponse } from '@/api/system/menu';
import axios from 'axios';

/**
 * 用户权限响应
 */
export interface UserPermissionResponse {
  user: UserBaseResponse;
  roles: Array<string>;
  permissions: Array<string>;
  systemPreference: Record<string, any>;
  tippedKeys: Array<string>;
}

/**
 * 用户基础信息
 */
export interface UserBaseResponse {
  id: number;
  username: string;
  nickname: string;
  avatar: string;
  passwordUpdateStatus: number;
  passwordUpdateReason: string;
}

/**
 * 获取用户信息
 */
export function getUserPermission() {
  return axios.get<UserPermissionResponse>('/infra/user-permission/user');
}

/**
 * 获取菜单列表
 */
export function getUserMenuList() {
  return axios.get<Array<MenuQueryResponse>>('/infra/user-permission/menu');
}
