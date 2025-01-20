// 缓存类型
export type CacheType = 'users' | 'menus' | 'roles'
  | 'hostGroups' | 'hostKeys' | 'hostIdentities' | 'host_*'
  | 'dictKeys'
  | 'execJob'
  | 'authorizedHostKeys' | 'authorizedHostIdentities'
  | 'commandSnippetGroups' | 'pathBookmarkGroups'
  | 'commandSnippets' | 'pathBookmarks'
  | '*_Tags' | 'preference_*' | 'system_setting'
  | string

export interface CacheState {
  [key: CacheType]: unknown;
}
