import type { AppRouteRecordRaw } from '../types';
import { FULL_LAYOUT } from '../base';

const TERMINAL: AppRouteRecordRaw = {
  name: 'terminalModule',
  path: '/terminal-module',
  component: FULL_LAYOUT,
  children: [
    {
      name: 'terminal',
      path: '/terminal',
      component: () => import('@/views/terminal/index.vue'),
      meta: {
        noAffix: true
      }
    },
  ],
};

export default TERMINAL;
