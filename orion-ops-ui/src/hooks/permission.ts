import { RouteLocationNormalized, RouteRecordRaw } from 'vue-router';
import { useUserStore } from '@/store';

export default function usePermission() {
  const userStore = useUserStore();
  return {
    /**
     * 是否可访问路由
     */
    accessRouter(route: RouteLocationNormalized | RouteRecordRaw) {
      // route.name
      // TODO
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
