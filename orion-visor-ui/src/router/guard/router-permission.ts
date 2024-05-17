import type { Router } from 'vue-router';
import NProgress from 'nprogress';
import { useMenuStore } from '@/store';
import { NOT_FOUND_ROUTER_NAME, WHITE_ROUTER_LIST } from '../constants';
import usePermission from '@/hooks/permission';

export default function setupPermissionGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const menuStore = useMenuStore();
    // 未加载菜单 并且 不在白名单内 则加载菜单
    if (
      !menuStore.menuFetched &&
      !WHITE_ROUTER_LIST.find((el) => el.name === to.name)
    ) {
      // 加载菜单
      await menuStore.fetchMenu();
    }
    // 检测是否可以访问
    const permission = usePermission();
    const access = permission.accessRouter(to);
    // 刚进入页面时 重定向的 meta 是空的
    if (access && to.meta.locale === undefined && menuStore.menuFetched) {
      to.meta = to.matched[to.matched.length - 1].meta;
    }
    if (access) {
      // 正常跳转
      next();
    } else {
      // 页面不存在
      next({ name: NOT_FOUND_ROUTER_NAME });
    }
    // 修改页面标题
    const locale = to.meta?.locale;
    if (locale) {
      document.title = locale;
    }
    NProgress.done();
  });
}
