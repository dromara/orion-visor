import type { CSSProperties } from 'vue';
import { HostQueryResponse } from '@/api/asset/host';
import { TerminalTabItem } from '@/views/host/terminal/types/terminal.type';

// tab 元素
export interface TabItem {
  key: string;
  title: string;
  type: string;
  icon?: string;

  [key: string]: unknown;
}

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  visible?: boolean;
  disabled?: boolean;
  checked?: boolean;
  iconStyle?: CSSProperties;
  click: () => void;
}

// 组合操作元素
export interface CombinedHandlerItem {
  icon: string,
  type: string,
  title: string;
  settingTab?: TerminalTabItem;
  host?: HostQueryResponse;
}

// 右键菜单元素
export interface ContextMenuItem {
  item: string;
  icon: string;
  content: string;
}

// 快捷键元素
export interface ShortcutKeyItem {
  item: string;
  content: string;
  type: number;
}

// ssh 额外配置
export interface SshExtraModel {
  authType?: string;
  username?: string;
  keyId?: number;
  identityId?: number;
}
