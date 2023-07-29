import 'vue-router';

declare module 'vue-router' {
  interface RouteMeta {
    // 后端赋值
    icon?: string;
    // 后端赋值
    locale?: string;
    // 后端赋值
    order?: number;
    // 后端赋值 是否隐藏菜单
    hideInMenu?: boolean;
    // 后端赋值 是否隐藏子菜单
    hideChildrenInMenu?: boolean;
    // 后端赋值 是否添加到 tab
    noAffix?: boolean;
    // 前端赋值 是否忽略缓存
    ignoreCache?: boolean;
    // 不赋值
    activeMenu?: string;
  }
}
