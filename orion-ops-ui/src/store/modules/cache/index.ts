import { defineStore } from 'pinia';
import { CacheState } from './types';

export type CacheType = 'menus' | 'roles' | 'hostTags' | 'hostKeys' | 'hostIdentities' | 'dictKeys' | string

export default defineStore('cache', {
  state: (): CacheState => ({
    menus: [],
    roles: [],
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
    }
  },
});
