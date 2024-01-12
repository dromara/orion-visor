import type {
  TerminalActionBarSetting,
  TerminalDisplaySetting,
  TerminalInteractSetting,
  TerminalPluginsSetting,
  TerminalPreference,
  TerminalSessionSetting,
  TerminalState
} from './types';
import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
import { getCurrentAuthorizedHost } from '@/api/asset/asset-authorized-data';
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
  // 操作栏设置
  ACTION_BAR_SETTING: 'actionBarSetting',
  // 交互设置
  INTERACT_SETTING: 'interactSetting',
  // 插件设置
  PLUGINS_SETTING: 'pluginsSetting',
  // 会话设置
  SESSION_SETTING: 'sessionSetting',
};

export default defineStore('terminal', {
  state: (): TerminalState => ({
    preference: {
      newConnectionType: 'group',
      theme: {} as TerminalTheme,
      displaySetting: {} as TerminalDisplaySetting,
      actionBarSetting: {} as TerminalActionBarSetting,
      interactSetting: {} as TerminalInteractSetting,
      pluginsSetting: {} as TerminalPluginsSetting,
      sessionSetting: {} as TerminalSessionSetting,
    },
    hosts: {} as AuthorizedHostQueryResponse,
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
        if (!data.theme?.name) {
          const { data: themes } = await getTerminalThemes();
          data.theme = themes[0];
          // 更新默认主题偏好
          await this.updateTerminalPreference(PreferenceItem.THEME, data.theme);
        }
        // 选择赋值
        const keys = Object.keys(this.preference);
        keys.forEach(key => {
          const item = data[key as keyof TerminalPreference];
          if (item) {
            this.preference[key as keyof TerminalPreference] = item as any;
          }
        });
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

    // 加载主机列表
    async loadHosts() {
      if (this.hosts.hostList?.length) {
        return;
      }
      const { data } = await getCurrentAuthorizedHost();
      Object.keys(data).forEach(k => {
        this.hosts[k as keyof AuthorizedHostQueryResponse] = data[k as keyof AuthorizedHostQueryResponse] as any;
      });
    },

    // 添加到最近连接列表
    addToLatestConnect(hostId: number) {
      this.hosts.latestHosts = [...new Set([hostId, ...this.hosts.latestHosts])];
    }

  },

});
