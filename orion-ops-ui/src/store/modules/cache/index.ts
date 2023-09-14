import { defineStore } from 'pinia';
import { CacheState } from './types';

export type CacheType = 'menus' | 'roles' | 'tags'

const useCacheStore = defineStore('cache', {
  state: (): CacheState => ({
    menus: [],
    roles: [],
    tags: []
  }),

  getters: {},

  actions: {
    /**
     * 设置
     */
    set(name: CacheType, value: any) {
      this[name] = value;
    }
  },
});

export default useCacheStore;
