import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const USER: AppRouteRecordRaw = {
  name: 'user',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'userRole',
      path: '/role',
      component: () => import('@/views/user/role/index.vue'),
    },
    {
      name: 'userUser',
      path: '/user',
      component: () => import('@/views/user/user/index.vue'),
    },
    {
      name: 'userInfo',
      path: '/user-info',
      component: () => import('@/views/user/info/index.vue'),
    },
    {
      name: 'userOperatorLog',
      path: '/operator-log',
      component: () => import('@/views/user/operator-log/index.vue'),
    },
  ],
};

export default USER;
