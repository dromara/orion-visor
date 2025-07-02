import type { ITerminalPanelManager, ITerminalSessionManager, ITerminalTabManager, ITerminalTransferManager, TerminalTheme } from '@/views/terminal/interfaces';
import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';

export interface TerminalState {
  preference: TerminalPreference;
  layoutState: TerminalLayoutState;
  hosts: AuthorizedHostQueryResponse;
  tabManager: ITerminalTabManager;
  panelManager: ITerminalPanelManager;
  sessionManager: ITerminalSessionManager;
  transferManager: ITerminalTransferManager;
}

// 终端配置
export interface TerminalPreference {
  newConnectionType: string;
  sshTheme: TerminalTheme;
  sshDisplaySetting: TerminalSshDisplaySetting;
  sshActionBarSetting: TerminalSshActionBarSetting;
  sshRightMenuSetting: Array<string>,
  sshInteractSetting: TerminalSshInteractSetting;
  sshPluginsSetting: TerminalSshPluginsSetting;
  rdpGraphSetting: TerminalRdpGraphSetting;
  rdpActionBarSetting: TerminalRdpActionBarSetting;
  rdpSessionSetting: TerminalRdpSessionSetting;
  shortcutSetting: TerminalShortcutSetting;
}

// SSH 显示设置
export interface TerminalSshDisplaySetting {
  fontFamily?: string;
  fontSize?: number;
  lineHeight?: number;
  letterSpacing?: number;
  fontWeight?: string | number;
  fontWeightBold?: string | number;
  cursorStyle?: string;
  cursorBlink?: boolean;
}

// SSH 操作栏设置
export interface TerminalSshActionBarSetting {
  connectStatus?: boolean;
  share?: boolean;

  [key: string]: unknown;
}

// SSH 插件设置
export interface TerminalSshPluginsSetting {
  enableWeblinkPlugin: boolean;
  enableWebglPlugin: boolean;
  enableUnicodePlugin: boolean;
  enableImagePlugin: boolean;
}

// SSH 交互设置
export interface TerminalSshInteractSetting {
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
  terminalEmulationType: string;
  scrollBackLine: number;
}

// RDP 图形化设置
export interface TerminalRdpGraphSetting {
  displaySize?: string;
  displayWidth?: number;
  displayHeight?: number;

  colorDepth?: number;
  forceLossless?: boolean;
  enableWallpaper?: boolean;
  enableTheming?: boolean;
  enableFontSmoothing?: boolean;
  enableFullWindowDrag?: boolean;
  enableDesktopComposition?: boolean;
  enableMenuAnimations?: boolean;
  disableBitmapCaching?: boolean;
  disableOffscreenCaching?: boolean;
  disableGlyphCaching?: boolean;
  disableGfx?: boolean;
}

// RDP 操作栏设置
export interface TerminalRdpActionBarSetting {
  position?: string;
  display?: boolean;
  combinationKey?: boolean;
  clipboard?: boolean;
  upload?: boolean;
  saveRdp?: boolean;
  disconnect?: boolean;
  close?: boolean;

  [key: string]: unknown;
}

// RDP 会话设置
export interface TerminalRdpSessionSetting {
  enableAudioInput?: boolean;
  enableAudioOutput?: boolean;
  driveMountMode?: string;
}

// 终端快捷键设置
export interface TerminalShortcutSetting {
  enabled: boolean;
  keys: Array<TerminalShortcutKey>;
}

// 终端快捷键
export interface ShortcutKey {
  ctrlKey: boolean;
  shiftKey: boolean;
  altKey: boolean;
  code: string;
}

// 终端快捷键
export interface TerminalShortcutKey extends ShortcutKey {
  item: string;
  enabled: boolean;
}

// 终端快捷键编辑
export interface TerminalShortcutKeyEditable extends TerminalShortcutKey {
  editable: boolean;
  content: string;
  type: number;
  shortcutKey?: string;
}

// 终端布局状态
export interface TerminalLayoutState {
  commandBar: boolean;
  fullscreen: boolean;
}
