import type { TerminalDisplaySetting, TerminalPreference, TerminalState, TerminalThemeSchema } from './types';
import { defineStore } from 'pinia';
import { getPreference, updatePreferencePartial } from '@/api/user/preference';
import { Message } from '@arco-design/web-vue';
import { useDark, useDebounceFn } from '@vueuse/core';
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
      newConnectionType: 'group',
      displaySetting: {} as TerminalDisplaySetting,
      themeSchema: {} as TerminalThemeSchema
    },
  }),

  actions: {
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

    // 修改暗色主题
    changeDarkTheme(darkTheme: string) {
      this.preference.darkTheme = darkTheme;
      if (darkTheme === DarkTheme.DARK) {
        // 暗色
        this.isDarkTheme = true;
      } else if (darkTheme === DarkTheme.LIGHT) {
        // 亮色
        this.isDarkTheme = false;
      } else if (darkTheme === DarkTheme.AUTO) {
        // 自动配色
        this.isDarkTheme = this.preference.themeSchema.dark;
      }
      // 同步配置
      this.updateTerminalPreference();
    },

    // 修改显示配置
    changeDisplaySetting(displaySetting: TerminalDisplaySetting) {
      this.preference.displaySetting = displaySetting;
      // 同步配置
      this.updateTerminalPreference();
    },

    // 选择终端主题
    changeThemeSchema(themeSchema: TerminalThemeSchema) {
      this.preference.themeSchema = themeSchema;
      // 切换主题配色
      if (this.preference.darkTheme === DarkTheme.AUTO) {
        this.isDarkTheme = themeSchema.dark;
      }
      // 同步配置
      this.updateTerminalPreference();
    },

    // 切换新建连接类型
    changeNewConnectionType(newConnectionType: string) {
      this.preference.newConnectionType = newConnectionType;
      // 同步配置
      this.updateTerminalPreference();
    },

    // 更新终端偏好-防抖
    updateTerminalPreference() {
      // 初始化函数
      if (!this.updateTerminalPreferenceFn) {
        this.updateTerminalPreferenceFn = useDebounceFn(async () => {
          try {
            // 修改配置
            await updatePreferencePartial({
              type: 'TERMINAL',
              config: this.preference
            });
          } catch (e) {
            Message.error('同步失败');
          }
        }, 1500);
      }
      // 更新
      this.updateTerminalPreferenceFn();
    }

  },
});
