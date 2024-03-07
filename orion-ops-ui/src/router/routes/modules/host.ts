import type { AppRouteRecordRaw } from '../types';
import { FULL_LAYOUT } from '../base';

const HOST: AppRouteRecordRaw = {
  name: 'host',
  component: FULL_LAYOUT,
  children: [
    {
      name: 'hostTerminal',
      path: '/terminal',
      component: () => import('@/views/host/terminal/index.vue'),
      meta: {
        noAffix: true
      }
    },
  ],
};

export default HOST;
