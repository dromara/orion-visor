import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT, FULL_LAYOUT } from '../base';

const EXEC: AppRouteRecordRaw[] = [
  {
    name: 'execModule',
    path: '/exec-module',
    component: DEFAULT_LAYOUT,
    children: [
      {
        name: 'execCommand',
        path: '/exec-command',
        component: () => import('@/views/exec/exec-command/index.vue'),
      },
      {
        name: 'execCommandLog',
        path: '/exec-log',
        component: () => import('@/views/exec/exec-command-log/index.vue'),
      },
      {
        name: 'execTemplate',
        path: '/exec-template',
        component: () => import('@/views/exec/exec-template/index.vue'),
      },
    ],
  },
  {
    name: 'execFullModule',
    path: '/exec-full-module',
    component: FULL_LAYOUT,
    children: [
      {
        name: 'execCommandLogView',
        path: '/exec-log-view',
        component: () => import('@/views/exec/exec-command-log-view/index.vue'),
      },
    ],
  }
];

export default EXEC;
