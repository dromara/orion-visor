import type { TabBarState, TagProps } from './types';
import { defineStore } from 'pinia';
import { isString } from '@/utils/is';

export default defineStore('tabBar', {
  state: (): TabBarState => ({
    cacheTabList: new Set([]),
    tagList: [],
  }),

  getters: {
    getTabList(): TagProps[] {
      return this.tagList;
    },
    getCacheList(): string[] {
      return Array.from(this.cacheTabList);
    },
  },

  actions: {
    // 添加 tab
    addTab(tag: TagProps, ignoreCache: boolean) {
      this.tagList.push(tag);
      if (!ignoreCache) {
        this.cacheTabList.add(tag.name);
      }
    },

    // 移除 tab
    deleteTab(idx: number, tag: TagProps) {
      this.tagList.splice(idx, 1);
      this.cacheTabList.delete(tag.name);
    },

    // 添加缓存
    addCache(name: string) {
      if (isString(name) && name !== '') this.cacheTabList.add(name);
    },

    // 删除缓存
    deleteCache(tag: TagProps) {
      this.cacheTabList.delete(tag.name);
    },

    // 重设缓存
    freshTabList(tags: TagProps[]) {
      this.tagList = tags;
      this.cacheTabList.clear();
      // 要先判断 ignoreCache
      this.tagList.filter((el) => !el.ignoreCache)
        .map((el) => el.name)
        .forEach((x) => this.cacheTabList.add(x));
    },

    // 重设 tab
    resetTabList() {
      this.tagList = [];
      this.cacheTabList.clear();
    },
  },
});
