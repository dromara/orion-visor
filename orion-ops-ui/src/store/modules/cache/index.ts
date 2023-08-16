import { defineStore } from 'pinia';
import { CacheState } from './types';
import { MenuQueryResponse } from '@/api/system/menu';
import { RoleQueryResponse } from '@/api/user/role';

const useCacheStore = defineStore('cache', {
  state: (): CacheState => ({
    menus: [],
    roles: [],
  }),

  getters: {},

  actions: {
    /**
     * 更新菜单
     */
    updateMenus(menus: MenuQueryResponse[]) {
      this.menus = menus;
    },

    /**
     * 清空菜单
     */
    resetMenus() {
      this.menus = [];
    },

    /**
     * 更新角色
     */
    updateRoles(roles: RoleQueryResponse[]) {
      this.roles = roles;
    },

    /**
     * 清空角色
     */
    resetRoles() {
      this.roles = [];
    },
  },
});

export default useCacheStore;
