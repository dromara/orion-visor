import type { AppState } from './types';
import { defineStore } from 'pinia';

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
  hostView: 'table',
  hostKeyView: 'table',
  hostIdentityView: 'table',
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
    // 更新配置
    updateSettings(partial: Partial<AppState>) {
      // 主题颜色
      if (partial.theme !== undefined) {
        document.body.setAttribute('arco-theme', partial.theme);
      }
      // 色弱模式
      if (partial.colorWeak !== undefined) {
        document.body.style.filter = partial.colorWeak ? 'invert(80%)' : 'none';
      }
      // 修改配置
      this.$patch(partial as object);
    },
  },
});
