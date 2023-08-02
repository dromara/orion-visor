import { RouteLocationNormalized, RouteRecordNormalized, RouteRecordRaw } from 'vue-router';
import { useAppStore, useUserStore } from '@/store';
import { WHITE_ROUTER_LIST } from '@/router/constants';

export default function usePermission() {
  const appStore = useAppStore();
  const userStore = useUserStore();
  return {
    /**
     * 是否可访问路由
     */
    accessRouter(route: RouteLocationNormalized | RouteRecordRaw) {
      // 检查路由是否存在于授权路由中
      const menuConfig = [...appStore.appAsyncMenus, ...WHITE_ROUTER_LIST];
      let exist = false;
      while (menuConfig.length && !exist) {
        const element = menuConfig.shift();
        if (element?.name === route.name) exist = true;
        if (element?.children) {
          menuConfig.push(
            ...(element.children as unknown as RouteRecordNormalized[])
          );
        }
      }
      return exist;
    },

    /**
     * 是否有权限
     */
    hasPermission(permission: string) {
      return userStore.permission?.includes('*') ||
        userStore.permission?.includes(permission);
    },

    /**
     * 是否有权限
     */
    hasAnyPermission(permission: string[]) {
      return userStore.permission?.includes('*') ||
        permission.map(s => userStore.permission?.includes(s))
        .filter(Boolean).length > 0;
    },

    /**
     * 是否有角色
     */
    hasRole(role: string) {
      return userStore.roles?.includes('admin') ||
        userStore.roles?.includes(role);
    },

    /**
     * 是否有角色
     */
    hasAnyRole(role: string[]) {
      return userStore.roles?.includes('*') ||
        role.map(s => userStore.roles?.includes(s))
        .filter(Boolean).length > 0;
    }
  };
}
