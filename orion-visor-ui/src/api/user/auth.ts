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
 * 用户登录
 */
export function userLogin(data: LoginRequest) {
  return axios.post<LoginResponse>('/infra/auth/login', data);
}

/**
 * 用户登出
 */
export function userLogout() {
  return axios.get('/infra/auth/logout');
}
