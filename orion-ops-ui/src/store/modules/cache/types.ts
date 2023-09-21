import { MenuQueryResponse } from '@/api/system/menu';
import { RoleQueryResponse } from '@/api/user/role';
import { TagQueryResponse } from '@/api/meta/tag';
import { HostKeyQueryResponse } from '@/api/asset/host-key';

export interface CacheState {
  menus: MenuQueryResponse[];
  roles: RoleQueryResponse[];
  tags: TagQueryResponse[];
  hostKeys: HostKeyQueryResponse[];

  [key: string]: unknown;
}
