import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const USER: AppRouteRecordRaw = {
  name: 'user',
  path: '/user',
  component: DEFAULT_LAYOUT,
  children: [
    {
      path: '/user/userChild1',
      name: 'userChild1',
      component: () => import('@/views/user/child1/index.vue'),
    },
    {
      path: '/user/userChild2',
      name: 'userChild2',
      component: () => import('@/views/user/child2/index.vue'),
      meta: {
        noAffix: true
      }
    },
  ],
};

export default USER;
