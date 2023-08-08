import { MenuQueryResponse } from '@/api/system/menu';

export interface MenuState {
  menus: MenuQueryResponse[],

  [key: string]: unknown;
}
