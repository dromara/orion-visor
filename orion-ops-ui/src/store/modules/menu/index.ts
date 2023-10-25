import type { RouteRecordNormalized } from 'vue-router';
import type { MenuState } from './types';
import { defineStore } from 'pinia';
import { Notification } from '@arco-design/web-vue';
import { getMenuList } from '@/api/user/auth';
import router from '@/router';

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
    // 加载菜单
    async fetchMenu() {
      try {
        const { data } = await getMenuList();
        // @ts-ignore
        this.serverMenus = (data as Array<any>).map(s => {
          // 转换
          const convert = (item: any) => {
            // 设置路由属性
            const meta = {
              locale: item.name,
              icon: item.icon,
              order: item.sort,
              hideInMenu: item.visible === 0,
              hideChildrenInMenu: item.visible === 0,
              noAffix: item.visible === 0,
              ignoreCache: item.cache === 0,
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
              children: undefined as unknown
            };
          };
          // 构建父目录
          const menu = convert(s);
          // 构建子目录
          if (s.children) {
            menu.children = (s.children as Array<any>).map(convert);
          }
          return menu;
        });
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
          content: '加载菜单失败',
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
