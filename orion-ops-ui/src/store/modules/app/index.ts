import { defineStore } from 'pinia';
import { AppState } from './types';

const defaultConfig: AppState = {
  // 应用设置
  device: 'desktop',
  menuCollapse: false,
  hideMenu: false,
  // 用户偏好-布局
  theme: 'light',
  menu: true,
  topMenu: false,
  navbar: true,
  footer: true,
  tabBar: true,
  menuWidth: 220,
  colorWeak: false,
  // 用户偏好-页面视图
  host: 'table',
  hostKeys: 'table',
  hostIdentity: 'table',
};

export default defineStore('app', {
  state: (): AppState => ({
    ...defaultConfig
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
    // 修改颜色主题
    toggleTheme(dark: boolean) {
      this.updateSettings({
        theme: dark ? 'dark' : 'light'
      });
      document.body.setAttribute('arco-theme', dark ? 'dark' : 'light');
    },

    // 更新配置
    updateSettings(partial: Partial<AppState>) {
      this.$patch(partial as object);
    },
  },
});
