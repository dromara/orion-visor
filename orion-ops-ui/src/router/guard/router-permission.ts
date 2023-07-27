import type { Router, RouteRecordNormalized } from 'vue-router';
import NProgress from 'nprogress';
import usePermission from '@/hooks/permission';
import { useAppStore } from '@/store';
import { WHITE_ROUTER_LIST, NOT_FOUND_ROUTER_NAME, FORBIDDEN_ROUTER_NAME } from '../constants';

export default function setupPermissionGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const appStore = useAppStore();
    const permission = usePermission();

    // 未加载菜单 并且 未从白名单中找到 to.name
    if (
      !appStore.appAsyncMenus.length &&
      !WHITE_ROUTER_LIST.find((el) => el.name === to.name)
    ) {
      // 加载菜单
      await appStore.fetchMenuConfig();
    }
    // 检查路由是否存在
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
    // 检查是否有权限
    const permissionsAllow = permission.accessRouter(to);
    if (!exist) {
      // 页面不存在
      next({ name: NOT_FOUND_ROUTER_NAME });
    } else if (!permissionsAllow) {
      // 无权限
      next({ name: FORBIDDEN_ROUTER_NAME });
    } else {
      // 正常跳转
      next();
    }
    NProgress.done();
  });
}
