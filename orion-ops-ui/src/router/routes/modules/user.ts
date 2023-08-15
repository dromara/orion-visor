import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

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
  ],
};

export default USER;
