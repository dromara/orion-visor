import type { AppRouteRecordRaw } from '../types';
import { FULL_LAYOUT } from '../base';

const HOST_OPS: AppRouteRecordRaw = {
  name: 'hostOps',
  path: '/host',
  component: FULL_LAYOUT,
  children: [
    {
      name: 'hostTerminal',
      path: '/host/terminal',
      component: () => import('@/views/host-ops/terminal/index.vue'),
      meta: {
        noAffix: true
      }
    },
  ],
};

export default HOST_OPS;
