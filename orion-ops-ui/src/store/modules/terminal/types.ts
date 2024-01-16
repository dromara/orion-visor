import type { ITerminalSessionManager, ITerminalTabManager } from '@/views/host/terminal/types/terminal.type';
import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
import type { TerminalTheme } from '@/api/asset/host-terminal';

export interface TerminalState {
  preference: TerminalPreference;
  hosts: AuthorizedHostQueryResponse;
  tabManager: ITerminalTabManager;
  sessionManager: ITerminalSessionManager;
}

// 终端配置
export interface TerminalPreference {
  newConnectionType: string;
  theme: TerminalTheme;
  displaySetting: TerminalDisplaySetting;
  actionBarSetting: TerminalActionBarSetting;
  rightMenuSetting: Array<string>,
  interactSetting: TerminalInteractSetting;
  pluginsSetting: TerminalPluginsSetting;
  sessionSetting: TerminalSessionSetting;
  shortcutSetting: TerminalShortcutSetting;
}

// 显示设置
export interface TerminalDisplaySetting {
  fontFamily?: string;
  fontSize?: number;
  lineHeight?: number;
  fontWeight?: string | number;
  fontWeightBold?: string | number;
  cursorStyle?: string;
  cursorBlink?: boolean;
}

// 操作栏设置
export interface TerminalActionBarSetting {
  commandInput?: boolean;
  connectStatus?: boolean;

  [key: string]: unknown;
}

// 交互设置
export interface TerminalInteractSetting {
  fastScrollModifier: boolean;
  altClickMovesCursor: boolean;
  rightClickSelectsWord: boolean;
  selectionChangeCopy: boolean;
  copyAutoTrim: boolean;
  pasteAutoTrim: boolean;
  rightClickPaste: boolean;
  enableRightClickMenu: boolean;
  enableBell: boolean;
  wordSeparator: string;
}

// 插件设置
export interface TerminalPluginsSetting {
  enableWeblinkPlugin: boolean;
  enableWebglPlugin: boolean;
  enableImagePlugin: boolean;
}

// 会话设置
export interface TerminalSessionSetting {
  terminalEmulationType: string;
  scrollBackLine: number;
}

// 终端快捷键设置
export interface TerminalShortcutSetting {
  enabled: boolean;
  keys: Array<TerminalShortcutKey>;
}

// 终端快捷键
export interface TerminalShortcutKey {
  option: string;
  ctrlKey: boolean;
  shiftKey: boolean;
  altKey: boolean;
  code: string;
}
