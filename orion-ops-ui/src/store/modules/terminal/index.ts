import type {
  TerminalActionBarSetting,
  TerminalDisplaySetting,
  TerminalInteractSetting,
  TerminalPluginsSetting,
  TerminalPreference,
  TerminalSessionSetting,
  TerminalShortcutSetting,
  TerminalState
} from './types';
import type { PanelSessionTab, TerminalPanelTabItem } from '@/views/host/terminal/types/terminal.type';
import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
import { getCurrentAuthorizedHost } from '@/api/asset/asset-authorized-data';
import type { HostQueryResponse } from '@/api/asset/host';
import type { TerminalTheme, TerminalThemeSchema } from '@/api/asset/host-terminal';
import { getTerminalThemes } from '@/api/asset/host-terminal';
import { defineStore } from 'pinia';
import { getPreference, updatePreference } from '@/api/user/preference';
import { nextSessionId } from '@/utils';
import { Message } from '@arco-design/web-vue';
import { PanelSessionType, TerminalTabs } from '@/views/host/terminal/types/terminal.const';
import TerminalTabManager from '@/views/host/terminal/handler/terminal-tab-manager';
import TerminalSessionManager from '@/views/host/terminal/handler/terminal-session-manager';
import TerminalPanelManager from '@/views/host/terminal/handler/terminal-panel-manager';

// 终端偏好项
export const TerminalPreferenceItem = {
  // 新建连接类型
  NEW_CONNECTION_TYPE: 'newConnectionType',
  // 终端主题
  THEME: 'theme',
  // 显示设置
  DISPLAY_SETTING: 'displaySetting',
  // 操作栏设置
  ACTION_BAR_SETTING: 'actionBarSetting',
  // 右键菜单设置
  RIGHT_MENU_SETTING: 'rightMenuSetting',
  // 交互设置
  INTERACT_SETTING: 'interactSetting',
  // 插件设置
  PLUGINS_SETTING: 'pluginsSetting',
  // 会话设置
  SESSION_SETTING: 'sessionSetting',
  // 快捷键设置
  SHORTCUT_SETTING: 'shortcutSetting',
};

export default defineStore('terminal', {
  state: (): TerminalState => ({
    preference: {
      newConnectionType: 'group',
      theme: {
        schema: {} as TerminalThemeSchema
      } as TerminalTheme,
      displaySetting: {} as TerminalDisplaySetting,
      actionBarSetting: {} as TerminalActionBarSetting,
      rightMenuSetting: [],
      interactSetting: {} as TerminalInteractSetting,
      pluginsSetting: {} as TerminalPluginsSetting,
      sessionSetting: {} as TerminalSessionSetting,
      shortcutSetting: {
        enabled: false,
        keys: []
      } as TerminalShortcutSetting,
    },
    hosts: {} as AuthorizedHostQueryResponse,
    tabManager: new TerminalTabManager(TerminalTabs.NEW_CONNECTION),
    panelManager: new TerminalPanelManager(),
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
          await this.updateTerminalPreference(TerminalPreferenceItem.THEME, data.theme);
        }
        // 移除禁用的快捷键
        if (data.shortcutSetting?.enabled) {
          data.shortcutSetting.keys = data.shortcutSetting.keys.filter(s => s.enabled);
        } else {
          data.shortcutSetting = {
            enabled: false,
            keys: []
          };
        }
        // 选择赋值 (不能修改引用)
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

    // 打开会话
    openSession(record: HostQueryResponse, session: PanelSessionTab, panelIndex: number = 0) {
      // 添加到最近连接
      this.hosts.latestHosts = [...new Set([record.id, ...this.hosts.latestHosts])];
      // 切换到终端面板页面
      this.tabManager.openTab(TerminalTabs.TERMINAL_PANEL);
      // 获取 seq
      const seqArr = this.panelManager
        .getPanel(panelIndex)
        .items
        .map(s => s.seq)
        .filter(Boolean)
        .map(Number);
      const nextSeq = seqArr.length
        ? Math.max(...seqArr) + 1
        : 1;
      // 打开 tab
      this.panelManager.getPanel(panelIndex).openTab({
        key: nextSessionId(10),
        seq: nextSeq,
        title: `(${nextSeq})  ${record.alias || record.name}`,
        hostId: record.id,
        address: record.address,
        icon: session.icon,
        type: session.type
      });
    },

    // 复制并且打开会话
    copySession(item: TerminalPanelTabItem, panelIndex: number = 0) {
      const host = this.hosts.hostList
        .find(s => s.id === item.hostId);
      if (host) {
        const sessionType = {
          type: item.type,
          icon: item.icon
        };
        this.openSession(host, sessionType, panelIndex);
      }
    },

    // 检查当前是否为终端页面 并且获取当前终端会话
    getAndCheckCurrentTerminalSession(tips: boolean = true) {
      // 获取当前 activeTab
      const activeTab = this.tabManager.active;
      if (activeTab !== TerminalTabs.TERMINAL_PANEL.key) {
        if (tips) {
          Message.warning('请切换到终端标签页');
        }
        return;
      }
      // 获取当前会话
      const session = this.getCurrentTerminalSession();
      if (!session && tips) {
        Message.warning('请打开终端');
      }
      return session;
    },

    // 获取当前终端会话
    getCurrentTerminalSession() {
      // 获取面板会话
      const sessionTab = this.panelManager
        .getCurrentPanel()
        .getCurrentTab();
      if (!sessionTab || sessionTab.type !== PanelSessionType.TERMINAL.type) {
        return;
      }
      // 获取会话
      return this.sessionManager.getSession(sessionTab.key);
    },

  },

});
