import type { TerminalDisplaySetting, TerminalPreference, TerminalState } from './types';
import type { TerminalTheme } from '@/api/asset/host-terminal';
import { getTerminalThemes } from '@/api/asset/host-terminal';
import { defineStore } from 'pinia';
import { getPreference, updatePreference } from '@/api/user/preference';
import { Message } from '@arco-design/web-vue';
import TerminalTabManager from '@/views/host/terminal/handler/terminal-tab-manager';
import TerminalSessionManager from '@/views/host/terminal/handler/terminal-session-manager';

// 偏好项
export const PreferenceItem = {
  // 新建连接类型
  NEW_CONNECTION_TYPE: 'newConnectionType',
  // 终端主题
  THEME: 'theme',
  // 显示设置
  DISPLAY_SETTING: 'displaySetting',
};

export default defineStore('terminal', {
  state: (): TerminalState => ({
    preference: {
      newConnectionType: 'group',
      displaySetting: {} as TerminalDisplaySetting,
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
          // 更新默认主题偏好
          await this.updateTerminalPreference(PreferenceItem.THEME, data.theme);
        }
        this.preference = data;
      } catch (e) {
        Message.error('配置加载失败');
      }
    },

    // 更新终端偏好
    async updateTerminalPreference(item: string, value: any, setLocal = false) {
      if (setLocal) {
        this.preference[item as keyof TerminalPreference] = value;
      }
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
