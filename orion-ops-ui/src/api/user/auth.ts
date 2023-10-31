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
 * 修改密码请求
 */
export interface UserUpdatePasswordRequest {
  beforePassword?: string;
  password?: string;
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
 * 修改密码
 */
export function updatePassword(request: UserUpdatePasswordRequest) {
  return axios.put('/infra/auth/update-password', request);
}

/**
 * 获取用户信息
 */
export function getUserPermission() {
  return axios.get('/infra/permission/user');
}

/**
 * 获取菜单列表
 */
export function getMenuList() {
  return axios.get('/infra/permission/menu');
}
