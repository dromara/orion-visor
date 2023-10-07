import { MenuQueryResponse } from '@/api/system/menu';
import { RoleQueryResponse } from '@/api/user/role';
import { TagQueryResponse } from '@/api/meta/tag';
import { HostKeyQueryResponse } from '@/api/asset/host-key';
import { HostIdentityQueryResponse } from '@/api/asset/host-identity';

export interface CacheState {
  menus: MenuQueryResponse[];
  roles: RoleQueryResponse[];
  hostTags: TagQueryResponse[];
  hostKeys: HostKeyQueryResponse[];
  hostIdentities: HostIdentityQueryResponse[];

  [key: string]: unknown;
}
