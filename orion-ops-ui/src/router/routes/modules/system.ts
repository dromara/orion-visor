import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const SYSTEM: AppRouteRecordRaw = {
  name: 'systemModule',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'systemMenu',
      path: '/menu',
      component: () => import('@/views/system/menu/index.vue'),
    },
    {
      name: 'dictKey',
      path: '/dict-key',
      component: () => import('@/views/system/dict-key/index.vue'),
    },
    {
      name: 'dictValue',
      path: '/dict-value',
      component: () => import('@/views/system/dict-value/index.vue'),
    },
  ],
};

export default SYSTEM;
