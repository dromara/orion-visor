import type { TerminalDisplaySetting, TerminalPreference, TerminalState, TerminalThemeSchema } from './types';
import { defineStore } from 'pinia';
import { getPreference, updatePreference } from '@/api/user/preference';
import { Message } from '@arco-design/web-vue';
import { useDark } from '@vueuse/core';
import TerminalTabManager from '@/views/host/terminal/handler/terminal-tab-manager';
import TerminalSessionManager from '@/views/host/terminal/handler/terminal-session-manager';
import type { TerminalTheme } from '@/api/asset/host-terminal';
import { getTerminalThemes } from '@/api/asset/host-terminal';

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
      themeSchema: {} as TerminalThemeSchema,
      theme: {} as TerminalTheme
    },
    tabManager: new TerminalTabManager(),
    sessionManager: new TerminalSessionManager()
  }),

  actions: {
    // 加载终端偏好
    async fetchPreference() {
      try {
        // 加载偏好
        const { data } = await getPreference<TerminalPreference>('TERMINAL');
        // theme 不存在则默认加载第一个
        if (!data.theme) {
          const { data: themes } = await getTerminalThemes();
          data.theme = themes[0];
        }
        this.preference = data;
      } catch (e) {
        Message.error('配置加载失败');
      }
    },

    // 修改暗色主题
    // FIXME 删除 terminalDarkTheme
    async changeDarkTheme(darkTheme: string) {
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
      await this.updateTerminalPreference('darkTheme', darkTheme);
    },

    // 修改显示配置
    async changeDisplaySetting(displaySetting: TerminalDisplaySetting) {
      this.preference.displaySetting = displaySetting;
      // 同步配置
      await this.updateTerminalPreference('displaySetting', displaySetting);
    },

    // 选择终端主题
    async changeThemeSchema(themeSchema: TerminalThemeSchema) {
      this.preference.themeSchema = themeSchema;
      // 切换主题配色
      if (this.preference.darkTheme === DarkTheme.AUTO) {
        this.isDarkTheme = themeSchema.dark;
      }
      // 同步配置
      await this.updateTerminalPreference('themeSchema', themeSchema);
    },

    // 切换新建连接类型
    async changeNewConnectionType(newConnectionType: string) {
      this.preference.newConnectionType = newConnectionType;
      // 同步配置
      await this.updateTerminalPreference('newConnectionType', newConnectionType);
    },

    // 更新终端偏好
    async updateTerminalPreference(item: string, value: any) {
      try {
        // 修改配置
        await updatePreference({
          type: 'TERMINAL',
          item,
          value
        });
      } catch (e) {
        Message.error('同步失败');
      }
    },

  },

});
