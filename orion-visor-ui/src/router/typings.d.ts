import type { RouteLocationNormalized } from 'vue-router';
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
    // 是否添加到 tab
    noAffix?: boolean;
    // 是否忽略缓存
    ignoreCache?: boolean;
    // 是否新窗口打开
    newWindow?: boolean;
    // 是否活跃
    activeMenu?: string;
    // 是否允许打开多个 tag
    multipleTab?: boolean;
    // 名称模板
    localeTemplate?: (route: RouteLocationNormalized) => string | undefined;
  }
}
