import type { AppRouteRecordRaw } from '../types';

const DASHBOARD: AppRouteRecordRaw = {
  name: 'hostWorkspace',
  path: '/host-workspace',
  component: () => import('@/layout/host-workspace-layout.vue'),
  children: [
    {
      name: 'hostTerminal',
      path: '/host-workspace/terminal',
      component: () => import('@/views/host-workspace/terminal/index.vue'),
    },
  ],
};

export default DASHBOARD;
