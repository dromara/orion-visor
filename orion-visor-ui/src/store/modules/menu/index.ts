import type { RouteMeta, RouteRecordNormalized } from 'vue-router';
import type { MenuState } from './types';
import type { MenuQueryResponse } from '@/api/system/menu';
import router from '@/router';
import { defineStore } from 'pinia';
import { Notification } from '@arco-design/web-vue';
import { getMenuList } from '@/api/user/auth';
import { EnabledStatus } from '@/types/const';

export default defineStore('menu', {
  state: (): MenuState => ({
    serverMenus: [],
    menuFetched: false,
  }),

  getters: {
    appMenus(state: MenuState): RouteRecordNormalized[] {
      return state.serverMenus as unknown as RouteRecordNormalized[];
    },
  },

  actions: {
    // 转换菜单
    convert(item: MenuQueryResponse): RouteRecordNormalized {
      // 设置路由属性
      const meta: RouteMeta = {
        locale: item.name,
        icon: item.icon,
        order: item.sort,
        hideInMenu: item.visible === EnabledStatus.DISABLED,
        noAffix: item.visible === EnabledStatus.DISABLED,
        ignoreCache: item.cache === EnabledStatus.DISABLED,
        newWindow: item.newWindow === EnabledStatus.ENABLED,
      };
      // 获取 router
      const route = router.getRoutes().find(r => {
        return r.name === item.component;
      });
      // 设置 router meta
      if (route) {
        // 路由配置覆盖菜单配置
        route.meta = { ...meta, ...route.meta };
      }
      // 返回
      return {
        name: item.component,
        path: item.path,
        meta: meta,
        children: undefined
      } as unknown as RouteRecordNormalized;
    },

    // 加载菜单
    async fetchMenu() {
      try {
        // 查询菜单
        const { data } = await getMenuList();
        // 转换菜单
        this.serverMenus = data.map(s => {
          // 构建父目录
          const menu = this.convert(s);
          // 构建子目录
          if (s.children) {
            menu.children = s.children.map(this.convert);
          }
          return menu;
        }) as RouteRecordNormalized[];
        // 是否已加载过
        this.menuFetched = true;
        // 未配置菜单
        if (this.serverMenus.length === 0) {
          Notification.error({
            content: '该用户未配置菜单, 请先联系管理员配置',
            closable: true,
          });
        }
      } catch (error) {
        Notification.error({
          content: '加载菜单失败, 请刷新后重试',
          closable: true,
        });
      }
    },

    // 清空菜单
    clearMenu() {
      this.serverMenus = [];
      this.menuFetched = false;
    },
  },
});
