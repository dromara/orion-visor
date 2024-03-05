import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const ASSET_AUDIT: AppRouteRecordRaw =
  {
    name: 'assetAudit',
    path: '/asset-audit',
    component: DEFAULT_LAYOUT,
    children: [
      {
        name: 'assetAuditConnectLog',
        path: '/asset-audit/connect-log',
        component: () => import('@/views/asset-audit/connect-log/index.vue'),
      },
      {
        name: 'assetAuditSftpLog',
        path: '/asset-audit/sftp-log',
        component: () => import('@/views/asset-audit/sftp-log/index.vue'),
      },
    ],
  };

export default ASSET_AUDIT;
