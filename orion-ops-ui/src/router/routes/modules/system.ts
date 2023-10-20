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
      name: 'systemDictKey',
      path: '/system/dict-key',
      component: () => import('@/views/system/dict-key/index.vue'),
    },
    {
      name: 'systemDictValue',
      path: '/system/dict-value',
      component: () => import('@/views/system/dict-value/index.vue'),
    },
  ],
};

export default SYSTEM;
