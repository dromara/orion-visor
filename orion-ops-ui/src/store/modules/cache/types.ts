import { MenuQueryResponse } from '@/api/system/menu';
import { RoleQueryResponse } from '@/api/user/role';
import { TagResponse } from '@/api/meta/tag';

export interface CacheState {
  menus: MenuQueryResponse[];
  roles: RoleQueryResponse[];
  tags: TagResponse[];
}
