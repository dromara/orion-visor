import type { RouteLocationRaw } from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router';
import { appRoutes } from './routes';
import { openWindow } from '@/utils';
import { isStandaloneMode } from '@/utils/env';
import createRouteGuard from './guard';
import baseRouters from './routes/base';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

NProgress.configure({ showSpinner: false });

// 创建路由
const router = createRouter({
  history: createWebHistory(),
  routes: [
    ...baseRouters,
    ...appRoutes,
  ],
  scrollBehavior() {
    return { top: 0 };
  },
});

// 创建路由守卫
createRouteGuard(router);

// 新页面打开路由
export const openNewRoute = (route: RouteLocationRaw) => {
  const { href } = router.resolve(route);
  if (isStandaloneMode) {
    // 单应用 PWA 则跳转
    window.location.href = href;
  } else {
    // 浏览器 则直接打开
    openWindow(href);
  }
};

export default router;
