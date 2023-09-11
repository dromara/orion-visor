import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const SYSTEM: AppRouteRecordRaw = {
  name: 'system',
  path: '/system',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'systemMenu',
      path: '/system/menu',
      component: () => import('@/views/system/menu/index.vue'),
    },
  ],
};

export default SYSTEM;
