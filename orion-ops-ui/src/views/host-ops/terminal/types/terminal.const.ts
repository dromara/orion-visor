import type { CSSProperties } from 'vue';

// 暗色主题
export const DarkTheme = {
  DARK: 'dark',
  LIGHT: 'light',
  AUTO: 'auto'
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
};

// tab 元素
export interface TabItem {
  key: string;
  title: string;
  type: string;

  [key: string]: unknown;
}

// 暗色主题切换标识
export const DarkThemeChangeSymbol = Symbol('DARK_THEME_CHANGE');

// 终端暗色模式 字典项
export const darkThemeKey = 'terminalDarkTheme';

// 加载的字典值
export const dictKeys = [darkThemeKey];
