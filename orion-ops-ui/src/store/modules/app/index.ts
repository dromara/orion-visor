import { defineStore } from 'pinia';
import defaultSettings from '@/config/settings.json';
import { AppState } from './types';

export default defineStore('app', {
  state: (): AppState => ({
    ...defaultSettings,
  }),

  getters: {
    appCurrentSetting(state: AppState): AppState {
      return { ...state };
    },
    appDevice(state: AppState) {
      return state.device;
    },
  },

  actions: {
    // 更新配置
    updateSettings(partial: Partial<AppState>) {
      this.$patch(partial as object);
      console.log(partial);
    },

    // 修改颜色主题
    toggleTheme(dark: boolean) {
      if (dark) {
        this.theme = 'dark';
        document.body.setAttribute('arco-theme', 'dark');
      } else {
        this.theme = 'light';
        document.body.removeAttribute('arco-theme');
      }
    },

    // 切换设备
    toggleDevice(device: string) {
      this.device = device;
    },

    // 切换菜单状态
    toggleMenu(value: boolean) {
      this.hideMenu = value;
    },
  },
});
