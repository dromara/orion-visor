import type { RouteRecordRaw } from 'vue-router';
import {
  DEFAULT_ROUTE_FULL_PATH,
  FORBIDDEN_ROUTER_NAME,
  LOGIN_ROUTE_NAME,
  NOT_FOUND_ROUTER_NAME,
  REDIRECT_ROUTE_NAME,
  UPDATE_PASSWORD_ROUTE_NAME,
} from '@/router/constants';

// 默认布局
export const DEFAULT_LAYOUT = () => import('@/layout/default-layout.vue');

// 全屏布局
export const FULL_LAYOUT = () => import('@/layout/full-layout.vue');

// 根页面
export const ROOT_ROUTE: RouteRecordRaw = {
  path: '/',
  redirect: DEFAULT_ROUTE_FULL_PATH,
};

// 登录页面
export const LOGIN_ROUTE: RouteRecordRaw = {
  path: '/login',
  name: LOGIN_ROUTE_NAME,
  meta: {
    locale: '登录',
    noAffix: true,
  },
  component: () => import('@/views/authentication/login/index.vue'),
};

// 重定向页面
export const REDIRECT_ROUTE: RouteRecordRaw = {
  path: '/redirect',
  name: 'redirectWrapper',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '重定向',
    hideInMenu: true,
    noAffix: true
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

// 修改密码
export const UPDATE_PASSWORD_ROUTE: RouteRecordRaw = {
  path: '/update-password',
  name: UPDATE_PASSWORD_ROUTE_NAME,
  component: () => import('@/views/base/update-password/index.vue'),
  meta: {
    locale: '修改密码',
    noAffix: true
  },
};

// 403 页面
export const FORBIDDEN_ROUTE: RouteRecordRaw = {
  path: '/403',
  name: FORBIDDEN_ROUTER_NAME,
  component: () => import('@/views/base/status/forbidden/index.vue'),
  meta: {
    locale: '403',
    noAffix: true
  },
};

// 404 页面
export const NOT_FOUND_ROUTE: RouteRecordRaw = {
  // path: '/:pathMatch(.*)*',
  path: '/404',
  name: NOT_FOUND_ROUTER_NAME,
  component: () => import('@/views/base/status/not-found/index.vue'),
  meta: {
    locale: '404',
    noAffix: true
  },
};

export default [
  ROOT_ROUTE,
  LOGIN_ROUTE,
  REDIRECT_ROUTE,
  UPDATE_PASSWORD_ROUTE,
  NOT_FOUND_ROUTE,
  FORBIDDEN_ROUTE
];
