import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const DASHBOARD: AppRouteRecordRaw = {
  name: 'dashboard',
  path: '/dashboard',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'workplace',
      path: '/dashboard/workplace',
      component: () => import('@/views/dashboard/workplace/index.vue'),
    },
  ],
};

export default DASHBOARD;
