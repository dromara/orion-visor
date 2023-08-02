import 'vue-router';

/**
 * 前端覆盖后端
 */
declare module 'vue-router' {
  interface RouteMeta {
    // 图标
    icon?: string;
    // 名称
    locale?: string;
    // 排序
    order?: number;
    // 是否隐藏菜单
    hideInMenu?: boolean;
    // 是否隐藏子菜单
    hideChildrenInMenu?: boolean;
    // 是否添加到 tab
    noAffix?: boolean;
    // 是否忽略缓存
    ignoreCache?: boolean;
    // 是否活跃
    activeMenu?: string;
  }
}
