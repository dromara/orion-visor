import 'vue-router';

declare module 'vue-router' {
  interface RouteMeta {
    permission?: string;
    requiresAuth: boolean;
    icon?: string;
    locale?: string;
    hideInMenu?: boolean;
    hideChildrenInMenu?: boolean;
    activeMenu?: string;
    order?: number;
    noAffix?: boolean;
    ignoreCache?: boolean;
  }
}
