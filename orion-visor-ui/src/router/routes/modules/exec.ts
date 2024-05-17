import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const EXEC: AppRouteRecordRaw = {
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
      name: 'batchUpload',
      path: '/batch-upload',
      component: () => import('@/views/exec/batch-upload/index.vue'),
    },
    {
      name: 'uploadTask',
      path: '/upload-task',
      component: () => import('@/views/exec/upload-task/index.vue'),
    },
    {
      name: 'execTemplate',
      path: '/exec-template',
      component: () => import('@/views/exec/exec-template/index.vue'),
    },
  ],
};

export default EXEC;
