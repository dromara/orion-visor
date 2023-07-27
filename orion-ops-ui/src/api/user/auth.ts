import axios from 'axios';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export function login(data: LoginRequest) {
  return axios.post<LoginResponse>('/infra/auth/login', data);
}

export function logout() {
  return axios.get('/infra/auth/logout');
}
