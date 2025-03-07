import type { AppRouteRecordRaw } from '../types';
import { DEFAULT_LAYOUT } from '../base';

const ASSET: AppRouteRecordRaw = {
  name: 'assetModule',
  path: '/asset-module',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'hostList',
      path: '/asset/host',
      component: () => import('@/views/asset/host-list/index.vue'),
    }, {
      name: 'hostKey',
      path: '/asset/host-key',
      component: () => import('@/views/asset/host-key/index.vue'),
    }, {
      name: 'hostIdentity',
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
