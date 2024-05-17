import type { MenuQueryResponse } from '@/api/system/menu';
import axios from 'axios';

/**
 * 登录请求
 */
export interface LoginRequest {
  username?: string;
  password?: string;
}

/**
 * 登录响应
 */
export interface LoginResponse {
  token: string;
}

/**
 * 用户权限响应
 */
export interface UserPermissionResponse {
  user: {
    id: number;
    username: string;
    nickname: string;
    avatar: string;
    systemPreference: Record<string, any>;
    tippedKeys: Array<string>;
  };
  roles: Array<string>;
  permissions: Array<string>;
}

/**
 * 登录
 */
export function login(data: LoginRequest) {
  return axios.post<LoginResponse>('/infra/auth/login', data);
}

/**
 * 登出
 */
export function logout() {
  return axios.get('/infra/auth/logout');
}

/**
 * 获取用户信息
 */
export function getUserPermission() {
  return axios.get<UserPermissionResponse>('/infra/permission/user');
}

/**
 * 获取菜单列表
 */
export function getMenuList() {
  return axios.get<Array<MenuQueryResponse>>('/infra/permission/menu');
}
