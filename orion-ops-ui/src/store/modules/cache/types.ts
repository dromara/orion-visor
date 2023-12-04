import type { UserQueryResponse } from '@/api/user/user';
import type { MenuQueryResponse } from '@/api/system/menu';
import type { RoleQueryResponse } from '@/api/user/role';
import type { HostQueryResponse } from '@/api/asset/host';
import type { HostGroupQueryResponse } from '@/api/asset/host-group';
import type { HostKeyQueryResponse } from '@/api/asset/host-key';
import type { HostIdentityQueryResponse } from '@/api/asset/host-identity';
import type { DictKeyQueryResponse } from '@/api/system/dict-key';

export interface CacheState {
  users?: UserQueryResponse[];
  menus?: MenuQueryResponse[];
  roles?: RoleQueryResponse[];
  hosts?: HostQueryResponse[];
  hostGroups?: HostGroupQueryResponse[];
  hostKeys?: HostKeyQueryResponse[];
  hostIdentities?: HostIdentityQueryResponse[];
  dictKeys?: DictKeyQueryResponse[];

  [key: string]: unknown;
}
