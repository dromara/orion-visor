import type { CSSProperties } from 'vue';
import type { HostQueryResponse } from '@/api/asset/host';
import type { TerminalTabItem } from '@/views/terminal/interfaces';

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  visible?: boolean;
  disabled?: boolean;
  checked?: boolean;
  active?: boolean;
  iconStyle?: CSSProperties;
  click: () => void;
}

// 组合操作元素
export interface CombinedHandlerItem {
  icon: string,
  title: string;
  tab?: TerminalTabItem;
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

// 组合键元素
export interface CombinationKeyItem {
  name: string;
  keys: Array<number>;
}
