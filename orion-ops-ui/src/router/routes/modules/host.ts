import type { AppRouteRecordRaw } from '../types';
import { FULL_LAYOUT } from '../base';

const HOST: AppRouteRecordRaw = {
  name: 'host',
  path: '/host',
  component: FULL_LAYOUT,
  children: [
    {
      name: 'hostSpace',
      path: '/host/space',
      component: () => import('@/views/host/space/index.vue'),
      meta: {
        noAffix: true
      }
    },
  ],
};

export default HOST;
