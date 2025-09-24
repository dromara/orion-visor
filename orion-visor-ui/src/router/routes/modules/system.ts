import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const SYSTEM: AppRouteRecordRaw = {
  name: 'systemModule',
  path: '/system-module',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'systemMenu',
      path: '/system/menu',
      component: () => import('@/views/system/menu/index.vue'),
    },
    {
      name: 'dictKey',
      path: '/system/dict-key',
      component: () => import('@/views/system/dict-key/index.vue'),
    },
    {
      name: 'dictValue',
      path: '/system/dict-value',
      component: () => import('@/views/system/dict-value/index.vue'),
    },
    {
      name: 'notifyTemplate',
      path: '/system/notify-template',
      component: () => import('@/views/system/notify-template/index.vue'),
    },
    {
      name: 'systemSetting',
      path: '/system/setting',
      component: () => import('@/views/system/setting/index.vue'),
    },
  ],
};

export default SYSTEM;
