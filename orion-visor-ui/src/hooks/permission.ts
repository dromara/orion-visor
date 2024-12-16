import type { RouteLocationNormalized, RouteRecordNormalized, RouteRecordRaw } from 'vue-router';
import { useMenuStore, useUserStore } from '@/store';
import { DEFAULT_ROUTER, STATUS_ROUTER_LIST, WHITE_ROUTER_LIST } from '@/router/constants';
import { AdminRoleCode } from '@/types/const';

export default function usePermission() {
  const menuStore = useMenuStore();
  const userStore = useUserStore();
  return {
    /**
     * 是否可访问路由
     */
    accessRouter(route: RouteLocationNormalized | RouteRecordRaw) {
      // 没有名字证明路由不存在
      if (route.name === undefined) {
        return false;
      }
      // 检查路由是否存在于授权路由中
      const menuConfig = [...menuStore.appMenus, ...WHITE_ROUTER_LIST, ...STATUS_ROUTER_LIST, DEFAULT_ROUTER];
      let exist = false;
      while (menuConfig.length && !exist) {
        const element = menuConfig.shift();
        if (element?.name === route.name) exist = true;
        if (element?.children) {
          menuConfig.push(...(element.children as unknown as RouteRecordNormalized[]));
        }
      }
      return exist;
    },

    /**
     * 是否有权限
     */
    hasPermission(permission: string) {
      return userStore.permission?.includes(permission);
    },

    /**
     * 是否有权限
     */
    hasAnyPermission(permission: string[]) {
      return permission.map(s => userStore.permission?.includes(s))
        .filter(Boolean).length > 0;
    },

    /**
     * 是否有角色
     */
    hasRole(role: string) {
      return userStore.roles?.includes(AdminRoleCode) ||
        userStore.roles?.includes(role);
    },

    /**
     * 是否有角色
     */
    hasAnyRole(role: string[]) {
      return userStore.roles?.includes(AdminRoleCode) ||
        role.map(s => userStore.roles?.includes(s))
          .filter(Boolean).length > 0;
    }
  };
}
