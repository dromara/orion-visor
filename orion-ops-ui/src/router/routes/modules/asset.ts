import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const ASSET: AppRouteRecordRaw = {
  name: 'asset',
  path: '/asset',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'assetHostList',
      path: '/asset/host-list',
      component: () => import('@/views/asset/host-list/index.vue'),
    }, {
      name: 'assetHostKey',
      path: '/asset/host-key',
      component: () => import('@/views/asset/host-key/index.vue'),
    }, {
      name: 'assetHostIdentity',
      path: '/asset/host-identity',
      component: () => import('@/views/asset/host-identity/index.vue'),
    }, {
      name: 'assetGrant',
      path: '/asset/grant',
      component: () => import('@/views/asset/grant/index.vue'),
    },
  ],
};

export default ASSET;
