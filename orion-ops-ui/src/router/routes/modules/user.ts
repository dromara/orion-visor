import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const USER: AppRouteRecordRaw = {
  name: 'user',
  path: '/user',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'userRole',
      path: '/user/role',
      component: () => import('@/views/user/role/index.vue'),
    },
    {
      name: 'userUser',
      path: '/user/user',
      component: () => import('@/views/user/user/index.vue'),
    },
    {
      name: 'userMine',
      path: '/user/mine',
      component: () => import('@/views/user/mine/index.vue'),
    },
  ],
};

export default USER;
