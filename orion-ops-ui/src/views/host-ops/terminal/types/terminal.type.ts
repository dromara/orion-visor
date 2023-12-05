import type { CSSProperties } from 'vue';

// Sidebar 操作类型
export interface SidebarAction {
  icon: string,
  content: string,
  style?: CSSProperties
  event: () => void,
}
