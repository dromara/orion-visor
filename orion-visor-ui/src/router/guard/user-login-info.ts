import type { RouteLocationRaw, Router } from 'vue-router';
import NProgress from 'nprogress';
import { useUserStore } from '@/store';
import { isLogin } from '@/utils/auth';
import { Message } from '@arco-design/web-vue';
import { LOGIN_ROUTE_NAME, UPDATE_PASSWORD_ROUTE_NAME } from '@/router/constants';

/**
 * 初始化用户登录路由守卫
 */
export default function setupUserLoginInfoGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    NProgress.start();
    const userStore = useUserStore();
    if (isLogin()) {
      if (userStore.id) {
        // 跳转
        next();
      } else {
        try {
          // 获取用户信息
          const info = await userStore.getUserInfo();
          if (info.updatePassword?.updatePasswordStatus === 1) {
            // 跳转到修改密码页面
            next({
              name: UPDATE_PASSWORD_ROUTE_NAME,
              query: { reason: info.updatePassword?.updatePasswordReason },
            } as RouteLocationRaw);
          } else {
            // 跳转
            next();
          }
        } catch (error) {
          // 登录过期
          if ((error as any)?.data?.data?.code !== 401) {
            Message.error('获取用户信息失败');
          }
          // 获取失败退出登录
          await userStore.logout();
          next({
            name: LOGIN_ROUTE_NAME,
            query: {
              redirect: to.name,
              ...to.query,
            },
          } as RouteLocationRaw);
        }
      }
    } else {
      if (to.name === LOGIN_ROUTE_NAME) {
        // 未登录跳转到登录页
        next();
      } else {
        // 跳转到登录页
        next({
          name: LOGIN_ROUTE_NAME,
          query: {
            redirect: to.name,
            ...to.query,
          },
        } as RouteLocationRaw);
      }
    }
  });
}
