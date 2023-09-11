import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const ASSET: AppRouteRecordRaw = {
  name: 'asset',
  path: '/asset',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'assetHost',
      path: '/asset/host',
      component: () => import('@/views/asset/host/index.vue'),
    },
  ],
};

export default ASSET;
