import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const ASSET_AUDIT: AppRouteRecordRaw = {
  name: 'assetAudit',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'assetAuditConnectLog',
      path: '/connect-log',
      component: () => import('@/views/asset-audit/connect-log/index.vue'),
    },
    {
      name: 'assetAuditSftpLog',
      path: '/sftp-log',
      component: () => import('@/views/asset-audit/sftp-log/index.vue'),
    },
  ],
};

export default ASSET_AUDIT;
