import type { Router, RouteRecordNormalized } from 'vue-router';
import NProgress from 'nprogress';
import { useAppStore } from '@/store';
import { NOT_FOUND_ROUTER_NAME, WHITE_ROUTER_LIST } from '../constants';

export default function setupPermissionGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const appStore = useAppStore();

    // 未加载菜单 并且 不在白名单内 则加载菜单
    if (
      !appStore.menuFetched &&
      !WHITE_ROUTER_LIST.find((el) => el.name === to.name)
    ) {
      // 加载菜单
      await appStore.fetchMenuConfig();
    }
    // 检查路由是否存在于授权路由中
    const menuConfig = [...appStore.appAsyncMenus, ...WHITE_ROUTER_LIST];
    let exist = false;
    while (menuConfig.length && !exist) {
      const element = menuConfig.shift();
      if (element?.name === to.name) exist = true;

      if (element?.children) {
        menuConfig.push(
          ...(element.children as unknown as RouteRecordNormalized[])
        );
      }
    }
    if (!exist) {
      // 页面不存在
      next({ name: NOT_FOUND_ROUTER_NAME });
    } else {
      // 正常跳转
      next();
    }
    NProgress.done();
  });
}
