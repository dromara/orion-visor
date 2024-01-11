import type { ITerminalSessionManager, ITerminalTabManager } from '@/views/host/terminal/types/terminal.type';
import type { TerminalTheme } from '@/api/asset/host-terminal';

export interface TerminalState {
  preference: TerminalPreference;
  tabManager: ITerminalTabManager;
  sessionManager: ITerminalSessionManager;
}

// 终端配置
export interface TerminalPreference {
  newConnectionType: string;
  theme: TerminalTheme;
  displaySetting: TerminalDisplaySetting;
  actionBarSetting: TerminalActionBarSetting;
  interactSetting: TerminalInteractSetting;
  pluginsSetting: TerminalPluginsSetting;
  sessionSetting: TerminalSessionSetting;
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
