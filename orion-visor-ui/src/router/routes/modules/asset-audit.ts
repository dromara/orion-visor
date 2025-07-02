import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const ASSET_AUDIT: AppRouteRecordRaw[] = [
  {
    name: 'assetAuditModule',
    path: '/asset-audit-module',
    component: DEFAULT_LAYOUT,
    children: [
      {
        name: 'terminalConnectLog',
        path: '/audit/terminal-connect-log',
        component: () => import('@/views/asset-audit/terminal-connect-log/index.vue'),
      },
      {
        name: 'terminalConnectSession',
        path: '/audit/terminal-connect-session',
        component: () => import('@/views/asset-audit/terminal-connect-session/index.vue'),
      },
      {
        name: 'terminalFileLog',
        path: '/audit/terminal-file-log',
        component: () => import('@/views/asset-audit/terminal-file-log/index.vue'),
      },
    ],
  },
];

export default ASSET_AUDIT;
