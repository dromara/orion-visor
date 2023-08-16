import { MenuQueryResponse } from '@/api/system/menu';
import { RoleQueryResponse } from '@/api/user/role';

export interface CacheState {
  menus: MenuQueryResponse[],
  roles: RoleQueryResponse[],

  [key: string]: unknown;
}
