import type { CacheState, CacheType } from './types';
import type { AxiosResponse } from 'axios';
import type { TagType } from '@/api/meta/tag';
import { getTagList } from '@/api/meta/tag';
import type { HostGroupQueryResponse } from '@/api/asset/host-group';
import { getHostGroupTree } from '@/api/asset/host-group';
import { getSystemAggregateSetting } from '@/api/system/setting';
import type { HostType } from '@/api/asset/host';
import { getHostList } from '@/api/asset/host';
import type { PreferenceType } from '@/api/user/preference';
import { getPreference } from '@/api/user/preference';
import usePermission from '@/hooks/permission';
import { defineStore } from 'pinia';
import { flatNodes } from '@/utils/tree';
import { getUserList } from '@/api/user/user';
import { getRoleList } from '@/api/user/role';
import { getDictKeyList } from '@/api/system/dict-key';
import { getHostKeyList } from '@/api/asset/host-key';
import { getHostIdentityList } from '@/api/asset/host-identity';
import { getMenuList } from '@/api/system/menu';
import { getCurrentAuthorizedHost, getCurrentAuthorizedHostIdentity, getCurrentAuthorizedHostKey } from '@/api/asset/asset-authorized-data';
import { getCommandSnippetGroupList } from '@/api/terminal/command-snippet-group';
import { getExecJobList } from '@/api/exec/exec-job';
import { getPathBookmarkGroupList } from '@/api/terminal/path-bookmark-group';
import { getCommandSnippetList } from '@/api/terminal/command-snippet';
import { getPathBookmarkList } from '@/api/terminal/path-bookmark';
import { getNotifyTemplateList } from '@/api/system/notify-template';
import { getAlarmPolicyList } from '@/api/monitor/alarm-policy';
import { getMetricsList } from '@/api/monitor/metrics';

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
    async load<T>(name: CacheType,
                  loader: () => Promise<AxiosResponse<T>>,
                  permissions: Array<string> | undefined = undefined,
                  force = false,
                  onErrorValue: any = []) {
      // 权限检查
      const len = permissions?.length;
      if (len) {
        const { hasPermission, hasAnyPermission } = usePermission();
        if (len === 1) {
          if (!hasPermission(permissions[0])) {
            return onErrorValue as T;
          }
        } else {
          if (!hasAnyPermission(permissions)) {
            return onErrorValue as T;
          }
        }
      }
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
      return await this.load('users', getUserList, ['infra:system-user:query'], force);
    },

    // 获取角色列表
    async loadRoles(force = false) {
      return await this.load('roles', getRoleList, ['infra:system-role:query'], force);
    },

    // 获取菜单列表
    async loadMenus(force = false) {
      return await this.load('menus', () => getMenuList({}), ['infra:system-menu:query'], force);
    },

    // 获取主机分组树
    async loadHostGroupTree(force = false) {
      return await this.load('hostGroups', getHostGroupTree, ['asset:host-group:update', 'asset:host:query'], force);
    },

    // 获取主机分组列表
    async loadHostGroupList(force = false) {
      const arr: Array<HostGroupQueryResponse> = [];
      // 展开节点
      flatNodes(await this.loadHostGroupTree(force), arr);
      return arr;
    },

    // 获取主机列表
    async loadHosts(type: HostType = '', force = false) {
      return await this.load(`host_${type || 'ALL'}`, () => getHostList(type), ['asset:host:query'], force);
    },

    // 获取主机密钥列表
    async loadHostKeys(force = false) {
      return await this.load('hostKeys', getHostKeyList, ['asset:host-key:query'], force);
    },

    // 获取主机身份列表
    async loadHostIdentities(force = false) {
      return await this.load('hostIdentities', getHostIdentityList, ['asset:host-identity:query'], force);
    },

    // 获取字典配置项列表
    async loadDictKeys(force = false) {
      return await this.load('dictKeys', getDictKeyList, undefined, force);
    },

    // 加载 tags
    async loadTags(type: TagType, force = false) {
      return await this.load(`${type}_Tags`, () => getTagList(type), undefined, force);
    },

    // 获取已授权的主机列表
    async loadAuthorizedHosts(type: HostType = '', force = false) {
      return await this.load(`authorizedHost_${type || 'ALL'}`, () => getCurrentAuthorizedHost(type), undefined, force);
    },

    // 获取已授权的主机密钥列表
    async loadAuthorizedHostKeys(force = false) {
      return await this.load('authorizedHostKeys', getCurrentAuthorizedHostKey, undefined, force);
    },

    // 获取已授权的主机身份列表
    async loadAuthorizedHostIdentities(force = false) {
      return await this.load('authorizedHostIdentities', getCurrentAuthorizedHostIdentity, undefined, force);
    },

    // 获取命令片段分组
    async loadCommandSnippetGroups(force = false) {
      return await this.load('commandSnippetGroups', getCommandSnippetGroupList, undefined, force);
    },

    // 获取路径书签分组
    async loadPathBookmarkGroups(force = false) {
      return await this.load('pathBookmarkGroups', getPathBookmarkGroupList, undefined, force);
    },

    // 获取命令片段列表
    async loadCommandSnippets(force = false) {
      return await this.load('commandSnippets', getCommandSnippetList, undefined, force, {});
    },

    // 获取路径书签列表
    async loadPathBookmarks(force = false) {
      return await this.load('pathBookmarks', getPathBookmarkList, undefined, force, {});
    },

    // 获取执行计划列表
    async loadExecJobs(force = false) {
      return await this.load('execJob', getExecJobList, ['exec:exec-job:query'], force);
    },

    // 查询监控告警策略列表
    async loadMonitorAlarmPolicy(type: string = 'all', force = false) {
      return await this.load(`alarmPolicy_${type}`, () => getAlarmPolicyList(type), ['monitor:alarm-policy:query'], force);
    },

    // 查询监控指标列表
    async loadMonitorMetricsList(force = false) {
      return await this.load('monitorMetrics', getMetricsList, ['monitor:monitor-metrics:query'], force);
    },

    // 查询通知模板列表
    async loadNotifyTemplate(bizType: string, force = false) {
      return await this.load(`notifyTemplate_${bizType}`, () => getNotifyTemplateList(bizType), ['infra:notify-template:query'], force);
    },

    // 加载偏好
    async loadPreference<T>(type: PreferenceType, force = false) {
      return await this.load(`preference_${type}`, () => getPreference<T>(type), undefined, force, {});
    },

    // 加载偏好项
    async loadPreferenceItem<T>(type: PreferenceType, item: string, force = false) {
      return await this.load(`preference_${type}_${item}`, () => getPreference<T>(type, [item]), undefined, force, {});
    },

    // 加载系统设置
    async loadSystemSetting(force = false) {
      return await this.load('systemSetting', getSystemAggregateSetting, undefined, force, {});
    }

  }
});
