import type { RouteLocationNormalized } from 'vue-router';
import type { TagProps } from '@/store/modules/tab-bar/types';

export const LOGIN_ROUTE_NAME = 'login';

export const REDIRECT_ROUTE_NAME = 'redirect';

export const UPDATE_PASSWORD_ROUTE_NAME = 'updatePassword';

export const FORBIDDEN_ROUTER_NAME = 'forbidden';

export const NOT_FOUND_ROUTER_NAME = 'notFound';

export const DEFAULT_ROUTE_NAME = 'workplace';

export const DEFAULT_ROUTE_FULL_PATH = '/workplace';

/**
 * 默认路由
 */
export const DEFAULT_ROUTER = { name: DEFAULT_ROUTE_NAME, children: [] };

/**
 * 路由白名单
 */
export const WHITE_ROUTER_LIST = [
  { name: LOGIN_ROUTE_NAME, children: [] },
  { name: REDIRECT_ROUTE_NAME, children: [] },
  { name: UPDATE_PASSWORD_ROUTE_NAME, children: [] },
];

/**
 * 状态路由
 */
export const STATUS_ROUTER_LIST = [
  { name: NOT_FOUND_ROUTER_NAME, children: [] },
  { name: FORBIDDEN_ROUTER_NAME, children: [] },
];

/**
 * router 转 tag
 */
export const routerToTag = (route: RouteLocationNormalized): TagProps => {
  const { name, meta, path, fullPath, query } = route;
  return {
    title: meta.locale || '',
    name: String(name),
    path,
    fullPath,
    query,
    ignoreCache: meta.ignoreCache,
  };
};
