import type { Router, LocationQueryRaw } from 'vue-router';
import NProgress from 'nprogress';
import { useUserStore } from '@/store';
import { isLogin } from '@/utils/auth';

/**
 * 初始化用户登录路由守卫
 */
export default function setupUserLoginInfoGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    NProgress.start();
    const userStore = useUserStore();
    if (isLogin()) {
      // 获取用户信息
      if (userStore.id) {
        next();
      } else {
        try {
          // 获取用户信息
          await userStore.info();
          next();
        } catch (error) {
          // 获取失败退出登录
          await userStore.logout();
          next({
            name: 'login',
            query: {
              redirect: to.name,
              ...to.query,
            } as LocationQueryRaw,
          });
        }
      }
    } else {
      // 未登录跳转到登录页
      if (to.name === 'login') {
        next();
        return;
      }
      // 跳转到登录页
      next({
        name: 'login',
        query: {
          redirect: to.name,
          ...to.query,
        } as LocationQueryRaw,
      });
    }
  });
}
