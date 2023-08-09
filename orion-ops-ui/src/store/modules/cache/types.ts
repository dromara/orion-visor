import { MenuQueryResponse } from '@/api/system/menu';

export interface CacheState {
  menus: MenuQueryResponse[],

  [key: string]: unknown;
}
