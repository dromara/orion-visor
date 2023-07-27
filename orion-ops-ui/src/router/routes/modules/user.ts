import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const USER: AppRouteRecordRaw = {
  path: '/user',
  name: 'user',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'USER',
    requiresAuth: true,
    icon: 'icon-dashboard',
    order: 0,
  },
  children: [
    {
      path: 'userChild',
      name: 'userChild',
      component: () => import('@/views/user/child/index.vue'),
      meta: {
        locale: '用户子页面',
        requiresAuth: true,
      },
    },
  ],
};

export default USER;
