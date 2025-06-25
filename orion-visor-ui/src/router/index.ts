import type { RouteLocationNormalized, RouteLocationRaw } from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router';
import type { TagProps } from '@/store/modules/tab-bar/types';
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

// route 转 tag
export const getRouteTag = (route: RouteLocationNormalized): TagProps => {
  const { name, meta, path, fullPath, query } = route;
  return {
    title: getRouteTitle(route),
    name: String(name),
    path,
    fullPath,
    query,
    ignoreCache: meta.ignoreCache,
  };
};

// 获取 router title
export const getRouteTitle = (route: RouteLocationNormalized) => {
  const { meta } = route;
  // 如果 meta.localeTemplate 则根据路由生成
  if (meta.localeTemplate) {
    return meta?.localeTemplate(route) || meta.locale || '';
  }
  return meta.locale || '';
};

export default router;
