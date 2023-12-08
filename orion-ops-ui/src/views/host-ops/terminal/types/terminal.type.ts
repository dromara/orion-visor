import type { CSSProperties } from 'vue';

// 暗色主题
export const DarkTheme = {
  DARK: {
    value: 'dark',
    label: '暗色'
  },
  LIGHT: {
    value: 'light',
    label: '亮色'
  },
  AUTO: {
    value: 'auto',
    label: '自动'
  }
};

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  style?: CSSProperties;
  visible?: boolean;
  click: () => void;
}

// tab 类型
export const TabType = {
  SETTING: 'setting',
  TERMINAL: 'terminal',
};

// 内置 tab
export const InnerTabs = {
  HOST_LIST: {
    key: 'hostList',
    title: '新建连接',
    type: TabType.SETTING
  },
  SHORTCUT_SETTING: {
    key: 'shortcutSetting',
    title: '快捷键设置',
    type: TabType.SETTING
  },
  THEME_SETTING: {
    key: 'themeSetting',
    title: '主题设置',
    type: TabType.SETTING
  },
  VIEW_SETTING: {
    key: 'viewSetting',
    title: '显示设置',
    type: TabType.SETTING
  },
};

// tab 元素
export interface TabItem {
  key: string;
  title: string;
  type: string;

  [key: string]: unknown;
}
