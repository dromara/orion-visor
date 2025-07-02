import type {
  TerminalPreference,
  TerminalRdpActionBarSetting,
  TerminalRdpGraphSetting,
  TerminalRdpSessionSetting,
  TerminalShortcutSetting,
  TerminalSshActionBarSetting,
  TerminalSshDisplaySetting,
  TerminalSshInteractSetting,
  TerminalSshPluginsSetting,
  TerminalState
} from './types';
import type {
  IDomViewportHandler,
  ISshSession,
  ITerminalSession,
  TerminalSessionTabItem,
  TerminalSessionType,
  TerminalTheme,
  TerminalThemeSchema
} from '@/views/terminal/interfaces';
import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
import type { HostQueryResponse } from '@/api/asset/host';
import { markRaw } from 'vue';
import { getTerminalThemes } from '@/api/terminal/terminal';
import { defineStore } from 'pinia';
import { getPreference, updatePreference } from '@/api/user/preference';
import { getLatestConnectHostId } from '@/api/terminal/terminal-connect-log';
import { useCacheStore } from '@/store';
import { nextId } from '@/utils';
import { isObject } from '@/utils/is';
import { Message } from '@arco-design/web-vue';
import { TerminalSessionTypes, TerminalTabs } from '@/views/terminal/types/const';
import TerminalTabManager from '@/views/terminal/service/tab/terminal-tab-manager';
import TerminalPanelManager from '@/views/terminal/service/tab/terminal-panel-manager';
import TerminalSessionManager from '@/views/terminal/service/session/terminal-session-manager';
import TerminalTransferManager from '@/views/terminal/service/transfer/terminal-transfer-manager';

// 终端偏好项
export const TerminalPreferenceItem = {
  // 新建连接类型
  NEW_CONNECTION_TYPE: 'newConnectionType',
  // ssh 主题
  SSH_THEME: 'sshTheme',
  // ssh 显示设置
  SSH_DISPLAY_SETTING: 'sshDisplaySetting',
  // ssh 操作栏设置
  SSH_ACTION_BAR_SETTING: 'sshActionBarSetting',
  // ssh 右键菜单设置
  SSH_RIGHT_MENU_SETTING: 'sshRightMenuSetting',
  // ssh 交互设置
  SSH_INTERACT_SETTING: 'sshInteractSetting',
  // ssh 插件设置
  SSH_PLUGINS_SETTING: 'sshPluginsSetting',
  // rdp 图形化设置
  RDP_GRAPH_SETTING: 'rdpGraphSetting',
  // rdp 操作栏设置
  RDP_ACTION_BAR_SETTING: 'rdpActionBarSetting',
  // 会话设置
  RDP_SESSION_SETTING: 'rdpSessionSetting',
  // 快捷键设置
  SHORTCUT_SETTING: 'shortcutSetting',
};

export default defineStore('terminal', {
  state: (): TerminalState => ({
    preference: {
      newConnectionType: 'group',
      sshTheme: {
        schema: {} as TerminalThemeSchema
      } as TerminalTheme,
      sshDisplaySetting: {} as TerminalSshDisplaySetting,
      sshActionBarSetting: {} as TerminalSshActionBarSetting,
      sshRightMenuSetting: [],
      sshInteractSetting: {} as TerminalSshInteractSetting,
      sshPluginsSetting: {} as TerminalSshPluginsSetting,
      rdpGraphSetting: {} as TerminalRdpGraphSetting,
      rdpSessionSetting: {} as TerminalRdpSessionSetting,
      rdpActionBarSetting: {} as TerminalRdpActionBarSetting,
      shortcutSetting: {
        enabled: false,
        keys: []
      } as TerminalShortcutSetting,
    },
    layoutState: {
      commandBar: false,
      fullscreen: false,
    },
    hosts: {} as AuthorizedHostQueryResponse,
    tabManager: new TerminalTabManager(),
    panelManager: new TerminalPanelManager(),
    sessionManager: markRaw(new TerminalSessionManager()),
    transferManager: new TerminalTransferManager(),
  }),

  actions: {
    // 加载终端偏好
    async fetchPreference() {
      try {
        // 加载偏好
        const { data } = await getPreference<TerminalPreference>('TERMINAL');
        // theme 不存在则默认加载第一个
        if (!data.sshTheme?.name) {
          const { data: themes } = await getTerminalThemes();
          data.sshTheme = themes[0];
          // 更新默认主题偏好
          await this.updateTerminalPreference(TerminalPreferenceItem.SSH_THEME, data.sshTheme);
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
          value: isObject(value) ? JSON.stringify(value) : value,
        });
      } catch (e) {
        Message.error('同步失败');
      }
    },

    // 加载主机列表
    async loadHostList() {
      if (this.hosts.hostList?.length) {
        return;
      }
      // 查询授权主机
      const data = await useCacheStore().loadAuthorizedHosts();
      Object.keys(data).forEach(k => {
        this.hosts[k as keyof AuthorizedHostQueryResponse] = data[k as keyof AuthorizedHostQueryResponse] as any;
      });
      this.hosts.latestHosts = [];
      // 查询最近连接的主机
      const { data: latestHosts } = await getLatestConnectHostId('', 30);
      this.hosts.latestHosts = latestHosts;
    },

    // 打开会话
    openSession(record: HostQueryResponse, type: TerminalSessionType, panelIndex: number | undefined = undefined) {
      if (panelIndex === undefined) {
        panelIndex = this.panelManager.active;
      }
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
        key: nextId(),
        panelIndex: panelIndex,
        seq: nextSeq,
        name: record.alias || record.name,
        title: `(${nextSeq}) ${record.alias || record.name}`,
        hostId: record.id,
        address: record.address,
        color: record.color,
        icon: type.icon,
        type: type.type,
        extra: record.extra,
      });
    },

    // 重新打开会话
    async reOpenSession(sessionKey: string) {
      // 切换到终端面板页面
      this.tabManager.openTab(TerminalTabs.TERMINAL_PANEL);
      // 获取当前面板 tab
      const tab = this.panelManager.panels
        .map(s => s.items)
        .flat()
        .find(s => s.key === sessionKey);
      if (!tab) {
        return;
      }
      // 添加到最近连接
      this.hosts.latestHosts = [...new Set([tab.hostId, ...this.hosts.latestHosts])];
      // 重新打开会话
      await this.sessionManager.reOpenSession(sessionKey);
    },

    // 复制并且打开会话
    copySession(item: TerminalSessionTabItem, panelIndex: number) {
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

    // 检查是否在终端面板
    checkTerminalPanelActive(): boolean {
      // 获取当前 activeTab
      const activeTab = this.tabManager.active;
      if (activeTab !== TerminalTabs.TERMINAL_PANEL.key) {
        Message.warning('请切换到终端标签页');
        return false;
      }
      return true;
    },

    // 获取当前会话类型
    getCurrentSessionType() {
      return this.panelManager
        .getCurrentPanel()
        .getCurrentTab()
        ?.type;
    },

    // 获取当前 domViewportHandler
    getCurrentDomViewportHandler(): IDomViewportHandler | undefined {
      // 获取当前会话
      const session = this._getCurrentSession<ISshSession>();
      if (!session) {
        return;
      }
      return session as unknown as IDomViewportHandler;
    },

    // 获取当前会话
    getCurrentSession<T extends ITerminalSession>(type?: string, check: boolean = false) {
      // 获取当前 activeTab
      if (check && !this.checkTerminalPanelActive()) {
        return;
      }
      // 获取当前会话
      const session = this._getCurrentSession<T>(type);
      if (!session && check) {
        Message.warning(`请打开 ${type || '终端'}`);
        return;
      }
      return session;
    },

    // 获取当前会话
    _getCurrentSession<T extends ITerminalSession>(type?: string): T | undefined {
      // 获取面板会话
      const sessionTab = this.panelManager
        .getCurrentPanel()
        .getCurrentTab();
      if (!sessionTab) {
        return;
      }
      if (type && sessionTab.type !== type) {
        return;
      }
      // 获取会话
      return this.sessionManager.getSession<T>(sessionTab.key);
    },

    // 拼接命令到当前会话
    appendCommandToCurrentSession(command: string, newLine: boolean = false, focus?: boolean) {
      this.appendCommandToSession(this.getCurrentSession<ISshSession>(TerminalSessionTypes.SSH.type, true), command, newLine, focus);
    },

    // 拼接命令到会话
    appendCommandToSession(session: ISshSession | undefined, command: string, newLine: boolean = false, focus?: boolean) {
      const handler = session?.handler;
      if (handler && handler.enabledStatus('checkAppendMissing')) {
        if (newLine) {
          command = `${command}\r\n`;
        }
        handler.checkAppendMissing(command, focus);
      }
    },

    // 粘贴命令到会话
    pasteCommandToSession(session: ISshSession | undefined, command: string, newLine: boolean = false, focus?: boolean) {
      const handler = session?.handler;
      if (handler && handler.enabledStatus('pasteOrigin')) {
        if (newLine) {
          command = `${command}\r\n`;
        }
        handler.pasteOrigin(command, focus);
      }
    },

  },

});
