import type { Router } from 'vue-router';
import setupUserLoginInfoGuard from './user-login-info';
import setupPermissionGuard from './router-permission';
import setupRouteEmitterGuard from './router-listener-emitter';

/**
 * 创建路由守卫
 */
export default function createRouteGuard(router: Router) {
  setupRouteEmitterGuard(router);
  setupUserLoginInfoGuard(router);
  setupPermissionGuard(router);
}
