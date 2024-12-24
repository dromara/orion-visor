import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';
import { DEFAULT_ROUTE_FULL_PATH, DEFAULT_ROUTE_NAME } from '@/router/constants';

const DASHBOARD: AppRouteRecordRaw = {
  name: 'dashboard',
  path: '/dashboard',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: DEFAULT_ROUTE_NAME,
      path: DEFAULT_ROUTE_FULL_PATH,
      component: () => import('@/views/dashboard/workplace/index.vue'),
    },
  ],
};

export default DASHBOARD;
