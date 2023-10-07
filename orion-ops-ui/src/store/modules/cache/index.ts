import { defineStore } from 'pinia';
import { CacheState } from './types';

export type CacheType = 'menus' | 'roles' | 'hostTags' | 'hostKeys' | 'hostIdentities'

export default defineStore('cache', {
  state: (): CacheState => ({
    menus: [],
    roles: [],
    hostTags: [],
    hostKeys: [],
    hostIdentities: [],
  }),

  getters: {},

  actions: {
    // 设置
    set(name: CacheType, value: any) {
      this[name] = value;
    }
  },
});
