import type { Router } from 'vue-router';
import setupUserLoginInfoGuard from './user-login-info';
import setupPermissionGuard from './router-permission';
import setupRouteEmitterGuard from './router-listener-emitter';

/**
 * 创建路由守卫
 */
export default function createRouteGuard(router: Router) {
  // 路由监听守卫
  setupRouteEmitterGuard(router);
  // 登录检查守卫
  setupUserLoginInfoGuard(router);
  // 权限检查守卫
  setupPermissionGuard(router);
}
