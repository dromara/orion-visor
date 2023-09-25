import type { RouteRecordNormalized } from 'vue-router';

export interface MenuState {
  serverMenus: RouteRecordNormalized[];
  menuFetched: boolean;
}
