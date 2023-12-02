import type { CacheState } from './types';
import type { AxiosResponse } from 'axios';
import { defineStore } from 'pinia';
import { getUserList } from '@/api/user/user';
import { getRoleList } from '@/api/user/role';

export type CacheType = 'users' | 'menus' | 'roles'
  | 'host' | 'hostGroups' | 'hostTags' | 'hostKeys' | 'hostIdentities'
  | 'dictKeys' | string

export default defineStore('cache', {
  state: (): CacheState => ({
    menus: [],
    roles: [],
    hosts: [],
    hostGroups: [],
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
        this[name] = undefined;
      }
    },

    // 清除全部
    clear() {
      this.$reset();
    },

    // 加载数据
    async load<T>(name: CacheType, loader: () => Promise<AxiosResponse<T>>, onErrorValue = []) {
      // 尝试直接从缓存中获取数据
      if (this[name]) {
        return this[name] as T;
      }
      // 加载数据
      try {
        const { data } = await loader();
        this[name] = data;
        return this[name] as T;
      } catch (e) {
        return onErrorValue as T;
      }
    },

    // 获取用户列表
    async loadUsers() {
      return await this.load('users', getUserList);
    },

    // 获取角色列表
    async loadRoles() {
      return await this.load('roles', getRoleList);
    },

  }
});
