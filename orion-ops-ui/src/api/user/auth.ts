import axios from 'axios';

/**
 * 登陆请求
 */
export interface LoginRequest {
  username: string;
  password: string;
}

/**
 * 登陆响应
 */
export interface LoginResponse {
  token: string;
}

/**
 * 登陆
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
  return axios.get('/infra/permission/user');
}

/**
 * 获取菜单列表
 */
export function getMenuList() {
  return axios.get('/infra/permission/menu');
}
