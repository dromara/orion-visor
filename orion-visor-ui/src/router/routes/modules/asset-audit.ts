import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const ASSET_AUDIT: AppRouteRecordRaw = {
  name: 'assetAuditModule',
  path: '/asset-audit-module',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'connectLog',
      path: '/connect-log',
      component: () => import('@/views/asset-audit/connect-log/index.vue'),
    },
    {
      name: 'connectSession',
      path: '/connect-session',
      component: () => import('@/views/asset-audit/connect-session/index.vue'),
    },
    {
      name: 'sftpLog',
      path: '/sftp-log',
      component: () => import('@/views/asset-audit/sftp-log/index.vue'),
    },
  ],
};

export default ASSET_AUDIT;
