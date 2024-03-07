import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const SYSTEM: AppRouteRecordRaw = {
  name: 'system',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'systemMenu',
      path: '/menu',
      component: () => import('@/views/system/menu/index.vue'),
    },
    {
      name: 'systemDictKey',
      path: '/dict-key',
      component: () => import('@/views/system/dict-key/index.vue'),
    },
    {
      name: 'systemDictValue',
      path: '/dict-value',
      component: () => import('@/views/system/dict-value/index.vue'),
    },
  ],
};

export default SYSTEM;
