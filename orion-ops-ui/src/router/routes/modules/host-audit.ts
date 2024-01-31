import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const HOST_AUDIT: AppRouteRecordRaw =
  {
    name: 'hostAudit',
    path: '/host-audit',
    component: DEFAULT_LAYOUT,
    children: [
      {
        name: 'hostAuditConnectLog',
        path: '/host-audit/connect-log',
        component: () => import('@/views/host-audit/connect-log/index.vue'),
      },
    ],
  };

export default HOST_AUDIT;
