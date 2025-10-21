// 缓存类型
export type CacheType = 'users' | 'menus' | 'roles'
  | 'hostGroups' | 'host_*' | 'authorizedHost_*'
  | 'hostKeys' | 'hostIdentities'
  | 'dictKeys'
  | 'execJob'
  | 'authorizedHostKeys' | 'authorizedHostIdentities'
  | 'commandSnippetGroups' | 'pathBookmarkGroups'
  | 'commandSnippets' | 'pathBookmarks'
  | 'alarmPolicy_*' | 'monitorMetrics'
  | 'systemSetting' | 'notifyTemplate*'
  | '*_Tags' | 'preference_*'
  | string

export interface CacheState {
  [key: CacheType]: any;
}
