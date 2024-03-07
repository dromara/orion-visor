import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const EXEC: AppRouteRecordRaw = {
  name: 'execModule',
  path: '/exec-module',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'execTemplate',
      path: '/exec-template',
      component: () => import('@/views/exec/exec-template/index.vue'),
    },
  ],
};

export default EXEC;
