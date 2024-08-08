// 缓存类型
export type CacheType = 'users' | 'menus' | 'roles'
  | 'hostGroups' | 'hostKeys' | 'hostIdentities'
  | 'dictKeys'
  | 'authorizedHostKeys' | 'authorizedHostIdentities'
  | 'commandSnippetGroups' | 'pathBookmarkGroups'
  | 'commandSnippets' | 'pathBookmarks'
  | 'execJob'
  | string

export interface CacheState {
  [key: CacheType]: unknown;
}
