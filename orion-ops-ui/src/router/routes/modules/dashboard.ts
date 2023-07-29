import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const DASHBOARD: AppRouteRecordRaw = {
  name: 'dashboard',
  component: DEFAULT_LAYOUT,
  children: [
    {
      path: '/dashboard/workplace',
      name: 'workplace',
      component: () => import('@/views/dashboard/workplace/index.vue'),
    },
  ],
};

export default DASHBOARD;
