import type { TerminalDisplaySetting, TerminalPreference, TerminalState, TerminalThemeSchema } from './types';
import { defineStore } from 'pinia';
import { getPreference, updatePreferencePartial } from '@/api/user/preference';
import { Message } from '@arco-design/web-vue';
import { useDark } from '@vueuse/core';
import { DEFAULT_SCHEMA } from '@/views/host-ops/terminal/types/terminal.theme';

// 暗色主题
export const DarkTheme = {
  DARK: 'dark',
  LIGHT: 'light',
  AUTO: 'auto'
};

export default defineStore('terminal', {
  state: (): TerminalState => ({
    isDarkTheme: useDark({
      selector: 'body',
      attribute: 'terminal-theme',
      valueDark: DarkTheme.DARK,
      valueLight: DarkTheme.LIGHT,
      initialValue: DarkTheme.DARK as any,
      storageKey: null
    }),
    preference: {
      darkTheme: 'auto',
      themeSchema: {} as TerminalThemeSchema,
      displaySetting: {} as TerminalDisplaySetting
    },
  }),

  actions: {
    // 修改暗色主题
    changeDarkTheme(dark: boolean) {
      this.isDarkTheme = dark;
    },

    // 加载终端偏好
    async fetchPreference() {
      try {
        const { data } = await getPreference<TerminalPreference>('TERMINAL');
        // 设置默认终端主题
        if (!data.config.themeSchema?.name) {
          data.config.themeSchema = DEFAULT_SCHEMA;
        }
        this.preference = data.config;
        // 设置暗色主题
        const userDarkTheme = data.config.darkTheme;
        if (userDarkTheme === DarkTheme.AUTO) {
          this.isDarkTheme = data.config.themeSchema?.dark === true;
        } else {
          this.isDarkTheme = userDarkTheme === DarkTheme.DARK;
        }
      } catch (e) {
        Message.error('配置加载失败');
      }
    },

    // 更新终端偏好
    async updatePreference() {
      try {
        // 修改配置
        await updatePreferencePartial({
          type: 'TERMINAL',
          config: this.preference
        });
      } catch (e) {
        Message.error('同步失败');
      }
    },

  },
});
