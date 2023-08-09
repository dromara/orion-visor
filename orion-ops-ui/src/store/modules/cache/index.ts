import { defineStore } from 'pinia';
import { CacheState } from './types';
import { MenuQueryResponse } from '@/api/system/menu';

const useCacheStore = defineStore('cache', {
  state: (): CacheState => ({
    menus: []
  }),

  getters: {},

  actions: {
    /**
     * 更新菜单
     */
    updateMenu(menus: MenuQueryResponse[]) {
      this.menus = menus;
    },

    /**
     * 清空菜单
     */
    resetMenu() {
      this.menus = [];
    },
  },
});

export default useCacheStore;
