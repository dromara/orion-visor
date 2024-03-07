import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const USER: AppRouteRecordRaw = {
  name: 'userModule',
  path: '/user-module',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'role',
      path: '/role',
      component: () => import('@/views/user/role/index.vue'),
    },
    {
      name: 'user',
      path: '/user',
      component: () => import('@/views/user/user/index.vue'),
    },
    {
      name: 'userInfo',
      path: '/user-info',
      component: () => import('@/views/user/info/index.vue'),
    },
    {
      name: 'operatorLog',
      path: '/operator-log',
      component: () => import('@/views/user/operator-log/index.vue'),
    },
  ],
};

export default USER;
