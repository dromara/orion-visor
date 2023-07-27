import type { RouteRecordRaw } from 'vue-router';
import {
  DEFAULT_ROUTE_FULL_PATH,
  REDIRECT_ROUTE_NAME,
  LOGIN_ROUTE_NAME,
  FORBIDDEN_ROUTER_NAME,
  NOT_FOUND_ROUTER_NAME,
} from '@/router/constants';

export const DEFAULT_LAYOUT = () => import('@/layout/default-layout.vue');

/**
 * 根页面
 */
export const ROOT_ROUTER: RouteRecordRaw = {
  path: '/',
  redirect: DEFAULT_ROUTE_FULL_PATH,
};

/**
 * 登录页面
 */
export const LOGIN_ROUTER: RouteRecordRaw = {
  path: '/login',
  name: LOGIN_ROUTE_NAME,
  component: () => import('@/views/login/index.vue'),
  meta: {
    requiresAuth: false,
  },
};

/**
 * 重定向页面
 */
export const REDIRECT_ROUTER: RouteRecordRaw = {
  path: '/redirect',
  name: 'redirectWrapper',
  component: DEFAULT_LAYOUT,
  meta: {
    requiresAuth: true,
    hideInMenu: true,
  },
  children: [
    {
      path: '/redirect/:path',
      name: REDIRECT_ROUTE_NAME,
      component: () => import('@/views/redirect/index.vue'),
      meta: {
        requiresAuth: true,
        hideInMenu: true,
      },
    },
  ],
};

/**
 * 403 页面
 */
export const FORBIDDEN_ROUTE: RouteRecordRaw = {
  path: '/:pathMatch(.*)*',
  name: FORBIDDEN_ROUTER_NAME,
  component: () => import('@/views/base/forbidden/index.vue'),
};

/**
 * 404 页面
 */
export const NOT_FOUND_ROUTE: RouteRecordRaw = {
  path: '/:pathMatch(.*)*',
  name: NOT_FOUND_ROUTER_NAME,
  component: () => import('@/views/base/not-found/index.vue'),
};

export default [
  ROOT_ROUTER,
  LOGIN_ROUTER,
  REDIRECT_ROUTER,
  NOT_FOUND_ROUTE,
  FORBIDDEN_ROUTE
];
