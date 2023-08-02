import { defineStore } from 'pinia';
import { Notification } from '@arco-design/web-vue';
import type { RouteRecordNormalized } from 'vue-router';
import defaultSettings from '@/config/settings.json';
import { getMenuList } from '@/api/user/auth';
import { AppState } from './types';
import router from '@/router';

const useAppStore = defineStore('app', {
  state: (): AppState => ({
    ...defaultSettings,
    menuFetched: false,
  }),

  getters: {
    appCurrentSetting(state: AppState): AppState {
      return { ...state };
    },
    appDevice(state: AppState) {
      return state.device;
    },
    appAsyncMenus(state: AppState): RouteRecordNormalized[] {
      return state.serverMenu as unknown as RouteRecordNormalized[];
    },
  },

  actions: {
    /**
     * 更新配置
     */
    updateSettings(partial: Partial<AppState>) {
      // @ts-ignore-next-line
      this.$patch(partial);
    },

    /**
     * 修改颜色主题
     */
    toggleTheme(dark: boolean) {
      if (dark) {
        this.theme = 'dark';
        document.body.setAttribute('arco-theme', 'dark');
      } else {
        this.theme = 'light';
        document.body.removeAttribute('arco-theme');
      }
    },

    /**
     * 切换设备
     */
    toggleDevice(device: string) {
      this.device = device;
    },

    /**
     * 切换菜单状态
     */
    toggleMenu(value: boolean) {
      this.hideMenu = value;
    },

    /**
     * 加载菜单
     */
    async fetchMenuConfig() {
      try {
        const { data } = await getMenuList();
        // @ts-ignore
        this.serverMenu = (data as Array<any>).map(s => {
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
        if (this.serverMenu.length === 0) {
          Notification.error({
            content: '该用户未配置菜单, 请先配置',
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

    /**
     * 清空菜单
     */
    clearMenu() {
      this.serverMenu = [];
      this.menuFetched = false;
    },
  },
});

export default useAppStore;
