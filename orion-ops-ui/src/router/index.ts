import { createRouter, createWebHistory } from 'vue-router';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { appRoutes } from './routes';
import BASE_ROUTERS from './routes/base';
import createRouteGuard from './guard';

NProgress.configure({ showSpinner: false });

// 创建路由
const router = createRouter({
  history: createWebHistory(),
  routes: [
    ...BASE_ROUTERS,
    ...appRoutes,
  ],
  scrollBehavior() {
    return { top: 0 };
  },
});

// 创建路由守卫
createRouteGuard(router);

export default router;
