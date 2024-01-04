import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const HOST_REVIEW: AppRouteRecordRaw =
  {
    name: 'hostReview',
    path: '/host-review',
    component: DEFAULT_LAYOUT,
    children: [
      {
        name: 'hostReviewConnectLog',
        path: '/host-review/connect-log',
        component: () => import('@/views/host-review/connect-log/index.vue'),
      },
    ],
  };

export default HOST_REVIEW;
