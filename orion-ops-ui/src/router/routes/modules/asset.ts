import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const ASSET: AppRouteRecordRaw = {
  name: 'asset',
  path: '/asset',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'assetHost',
      path: '/asset/host',
      component: () => import('@/views/asset/host/index.vue'),
    }, {
      name: 'assetHostKey',
      path: '/asset/host-key',
      component: () => import('@/views/asset/host-key/index.vue'),
    }, {
      name: 'assetHostIdentity',
      path: '/asset/host-identity',
      component: () => import('@/views/asset/host-identity/index.vue'),
    },
  ],
};

export default ASSET;
