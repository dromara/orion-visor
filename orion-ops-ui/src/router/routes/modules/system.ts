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
    {
      name: 'systemDict',
      path: '/system/dict',
      component: () => import('@/views/system/dict/index.vue'),
    },
  ],
};

export default SYSTEM;
