import { RouteLocationNormalized } from 'vue-router';
import { TagProps } from '@/store/modules/tab-bar/types';

export const REDIRECT_ROUTE_NAME = 'redirect';

export const LOGIN_ROUTE_NAME = 'login';

export const FORBIDDEN_ROUTER_NAME = 'forbidden';

export const NOT_FOUND_ROUTER_NAME = 'notFound';

export const DEFAULT_ROUTE_NAME = 'workplace';

export const DEFAULT_ROUTE_FULL_PATH = '/dashboard/workplace';

/**
 * 路由白名单
 */
export const WHITE_ROUTER_LIST = [
  { name: LOGIN_ROUTE_NAME, children: [] },
  { name: REDIRECT_ROUTE_NAME, children: [] },
];

/**
 * 状态路由
 */
export const STATUS_ROUTER_LIST = [
  { name: NOT_FOUND_ROUTER_NAME, children: [] },
  { name: FORBIDDEN_ROUTER_NAME, children: [] },
];

/**
 * 默认 tab 页面
 */
export const DEFAULT_TAB = {
  title: '工作台',
  name: DEFAULT_ROUTE_NAME,
  fullPath: DEFAULT_ROUTE_FULL_PATH,
};

/**
 * router 转 tag
 */
export const routerToTag = (route: RouteLocationNormalized): TagProps => {
  const { name, meta, fullPath, query } = route;
  return {
    title: meta.locale || '',
    name: String(name),
    fullPath,
    query,
    ignoreCache: meta.ignoreCache,
  };
};
