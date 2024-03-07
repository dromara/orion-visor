import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const DASHBOARD: AppRouteRecordRaw = {
  name: 'dashboard',
  path: '/dashboard',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'workplace',
      path: '/workplace',
      component: () => import('@/views/dashboard/workplace/index.vue'),
    },
  ],
};

export default DASHBOARD;
