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
  displaySetting: TerminalDisplaySetting;
  theme: TerminalTheme;
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

