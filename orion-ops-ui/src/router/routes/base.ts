import type { RouteRecordRaw } from 'vue-router';
import { DEFAULT_ROUTE_FULL_PATH, FORBIDDEN_ROUTER_NAME, LOGIN_ROUTE_NAME, NOT_FOUND_ROUTER_NAME, REDIRECT_ROUTE_NAME, } from '@/router/constants';

// 默认布局
export const DEFAULT_LAYOUT = () => import('@/layout/default-layout.vue');

// 全屏布局
export const FULL_LAYOUT = () => import('@/layout/full-layout.vue');

// 根页面
export const ROOT_ROUTER: RouteRecordRaw = {
  path: '/',
  redirect: DEFAULT_ROUTE_FULL_PATH,
};

// 登录页面
export const LOGIN_ROUTER: RouteRecordRaw = {
  path: '/login',
  name: LOGIN_ROUTE_NAME,
  meta: {
    locale: '登录'
  },
  component: () => import('@/views/authentication/login/index.vue'),
};

// 重定向页面
export const REDIRECT_ROUTER: RouteRecordRaw = {
  path: '/redirect',
  name: 'redirectWrapper',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '重定向',
    hideInMenu: true,
  },
  children: [
    {
      path: '/redirect/:path',
      name: REDIRECT_ROUTE_NAME,
      component: () => import('@/views/base/redirect/index.vue'),
      meta: {
        locale: '重定向',
        hideInMenu: true,
        noAffix: true
      },
    },
  ],
};

// 403 页面
export const FORBIDDEN_ROUTE: RouteRecordRaw = {
  path: '/403',
  name: FORBIDDEN_ROUTER_NAME,
  meta: {
    locale: '403'
  },
  component: () => import('@/views/base/status/forbidden/index.vue'),
};

// 404 页面
export const NOT_FOUND_ROUTE: RouteRecordRaw = {
  // path: '/:pathMatch(.*)*',
  path: '/404',
  name: NOT_FOUND_ROUTER_NAME,
  meta: {
    locale: '404'
  },
  component: () => import('@/views/base/status/not-found/index.vue'),
};

export default [
  ROOT_ROUTER,
  LOGIN_ROUTER,
  REDIRECT_ROUTER,
  NOT_FOUND_ROUTE,
  FORBIDDEN_ROUTE
];
