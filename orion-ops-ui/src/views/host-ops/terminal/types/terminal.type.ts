import type { CSSProperties } from 'vue';

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  style?: CSSProperties;
  visible?: boolean;
  click: () => void;
}
