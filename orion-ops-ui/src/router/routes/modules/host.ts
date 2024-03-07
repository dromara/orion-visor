import type { AppRouteRecordRaw } from '../types';
import { FULL_LAYOUT } from '../base';

const HOST: AppRouteRecordRaw = {
  name: 'hostModule',
  path: '/host-module',
  component: FULL_LAYOUT,
  children: [
    {
      name: 'terminal',
      path: '/terminal',
      component: () => import('@/views/host/terminal/index.vue'),
      meta: {
        noAffix: true
      }
    },
  ],
};

export default HOST;
