import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT, FULL_LAYOUT } from '../base';

const JOB: AppRouteRecordRaw[] = [
  {
    name: 'jobModule',
    path: '/job-module',
    component: DEFAULT_LAYOUT,
    children: [
      {
        name: 'execJob',
        path: '/exec-job',
        component: () => import('@/views/exec/exec-job/index.vue'),
      },
      {
        name: 'execJobLog',
        path: '/exec-job-log',
        component: () => import('@/views/exec/exec-job-log/index.vue'),
      },
    ],
  },
  {
    name: 'jobFullModule',
    path: '/job-full-module',
    component: FULL_LAYOUT,
    children: [
      {
        name: 'execJobLogView',
        path: '/job-log-view',
        component: () => import('@/views/exec/exec-job-log-view/index.vue'),
      },
    ],
  }
];

export default JOB;
