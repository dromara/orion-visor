import type { Router } from 'vue-router';
import { setRouteEmitter } from '@/utils/route-listener';

/**
 * 初始化路由监听订阅
 */
export default function setupRouteEmitterGuard(router: Router) {
  router.beforeEach(async (to) => {
    setRouteEmitter(to);
  });
}
