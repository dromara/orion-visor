import type { CacheState } from './types';
import type { AxiosResponse } from 'axios';
import type { TagType } from '@/api/meta/tag';
import { getTagList } from '@/api/meta/tag';
import { defineStore } from 'pinia';
import { getUserList } from '@/api/user/user';
import { getRoleList } from '@/api/user/role';
import { getDictKeyList } from '@/api/system/dict-key';
import { getHostKeyList } from '@/api/asset/host-key';
import { getHostIdentityList } from '@/api/asset/host-identity';
import { getHostList } from '@/api/asset/host';
import { getHostGroupTree } from '@/api/asset/host-group';
import { getMenuList } from '@/api/system/menu';
import { getCurrentAuthorizedHostIdentity, getCurrentAuthorizedHostKey } from '@/api/asset/asset-authorized-data';
import { getCommandSnippetGroupList } from '@/api/asset/command-snippet-group';
import { getExecJobList } from '@/api/exec/exec-job';
import { getPathBookmarkGroupList } from '@/api/asset/path-bookmark-group';

export type CacheType = 'users' | 'menus' | 'roles'
  | 'hosts' | 'hostGroups' | 'hostKeys' | 'hostIdentities'
  | 'dictKeys'
  | 'authorizedHostKeys' | 'authorizedHostIdentities'
  | 'commandSnippetGroups' | 'pathBookmarkGroups'
  | 'execJob'
  | string

export default defineStore('cache', {
  state: (): CacheState => ({}),

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
    async load<T>(name: CacheType, loader: () => Promise<AxiosResponse<T>>, force = false, onErrorValue = []) {
      // 尝试直接从缓存中获取数据
      if (this[name] && !force) {
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
    async loadUsers(force = false) {
      return await this.load('users', getUserList, force);
    },

    // 获取角色列表
    async loadRoles(force = false) {
      return await this.load('roles', getRoleList, force);
    },

    // 获取菜单列表
    async loadMenus(force = false) {
      return await this.load('menus', () => getMenuList({}), force);
    },

    // 获取主机分组列表
    async loadHostGroups(force = false) {
      return await this.load('hostGroups', getHostGroupTree, force);
    },

    // 获取主机列表
    async loadHosts(force = false) {
      return await this.load('hosts', getHostList, force);
    },

    // 获取主机秘钥列表
    async loadHostKeys(force = false) {
      return await this.load('hostKeys', getHostKeyList, force);
    },

    // 获取主机身份列表
    async loadHostIdentities(force = false) {
      return await this.load('hostIdentities', getHostIdentityList, force);
    },

    // 获取字典配置项列表
    async loadDictKeys(force = false) {
      return await this.load('dictKeys', getDictKeyList, force);
    },

    // 加载 tags
    async loadTags(type: TagType, force = false) {
      return await this.load(`${type}_Tags`, () => getTagList(type), force);
    },

    // 获取已授权的主机秘钥列表
    async loadAuthorizedHostKeys(force = false) {
      return await this.load('authorizedHostKeys', getCurrentAuthorizedHostKey, force);
    },

    // 获取已授权的主机身份列表
    async loadAuthorizedHostIdentities(force = false) {
      return await this.load('authorizedHostIdentities', getCurrentAuthorizedHostIdentity, force);
    },

    // 获取命令片段分组
    async loadCommandSnippetGroups(force = false) {
      return await this.load('commandSnippetGroups', getCommandSnippetGroupList, force);
    },

    // 获取路径书签分组
    async loadPathBookmarkGroups(force = false) {
      return await this.load('pathBookmarkGroups', getPathBookmarkGroupList, force);
    },

    // 获取执行计划列表
    async loadExecJobs(force = false) {
      return await this.load('execJob', getExecJobList, force);
    },

  }
});
