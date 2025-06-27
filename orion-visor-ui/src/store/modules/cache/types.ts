// 缓存类型
export type CacheType = 'users' | 'menus' | 'roles'
  | 'hostGroups' | 'host_*' | 'authorizedHost_*'
  | 'hostKeys' | 'hostIdentities'
  | 'dictKeys'
  | 'execJob'
  | 'authorizedHostKeys' | 'authorizedHostIdentities'
  | 'commandSnippetGroups' | 'pathBookmarkGroups'
  | 'commandSnippets' | 'pathBookmarks'
  | 'system_setting'
  | '*_Tags' | 'preference_*'
  | string

export interface CacheState {
  [key: CacheType]: unknown;
}
