import type { CacheState } from './types';
import { defineStore } from 'pinia';

export type CacheType = 'users' | 'menus' | 'roles'
  | 'host' | 'hostTags' | 'hostKeys' | 'hostIdentities'
  | 'dictKeys' | string

export default defineStore('cache', {
  state: (): CacheState => ({
    users: [],
    menus: [],
    roles: [],
    hosts: [],
    hostTags: [],
    hostKeys: [],
    hostIdentities: [],
    dictKeys: [],
  }),

  getters: {},

  actions: {
    // 设置
    set(name: CacheType, value: any) {
      this[name] = value;
    },

    // 清空
    reset(...names: CacheType[]) {
      for (let name of names) {
        this[name] = [];
      }
    },

    // 清除全部
    clear() {
      this.$reset();
    }
  },
});
