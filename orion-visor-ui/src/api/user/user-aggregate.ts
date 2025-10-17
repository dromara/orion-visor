import type { MenuQueryResponse } from '@/api/system/menu';
import type { HttpResponse } from '@/types/global';
import axios from 'axios';

/**
 * 用户聚合信息
 */
export interface UserAggregateResponse {
  user: UserBaseResponse;
  roles: Array<string>;
  updatePassword?: UserUpdatePasswordResponse;
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
}

/**
 * 用户更新密码响应
 */
export interface UserUpdatePasswordResponse {
  updatePasswordStatus: number;
  updatePasswordReason: string;
}

/**
 * 获取用户聚合信息
 */
export function getUserAggregateInfo() {
  return axios.get<HttpResponse<UserAggregateResponse>>('/infra/user-aggregate/user', {
    unwrap: true
  });
}

/**
 * 获取用户菜单列表
 */
export function getUserMenuList() {
  return axios.get<Array<MenuQueryResponse>>('/infra/user-aggregate/menu');
}
