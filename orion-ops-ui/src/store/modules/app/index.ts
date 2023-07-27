import { defineStore } from 'pinia';
import { Notification } from '@arco-design/web-vue';
import type { RouteRecordNormalized } from 'vue-router';
import defaultSettings from '@/config/settings.json';
import { getMenuList } from '@/api/user';
import { AppState } from './types';

const useAppStore = defineStore('app', {
  state: (): AppState => ({ ...defaultSettings }),

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
        this.serverMenu = data;
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
    },
  },
});

export default useAppStore;
