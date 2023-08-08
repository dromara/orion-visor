import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const MENU: AppRouteRecordRaw = {
  name: 'menu',
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

export default MENU;
