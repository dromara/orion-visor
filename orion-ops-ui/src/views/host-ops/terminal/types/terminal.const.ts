import type { CSSProperties } from 'vue';

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  iconStyle?: CSSProperties;
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
  NEW_CONNECTION: {
    key: 'newConnection',
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

// 字体后缀 兜底
export const fontFamilySuffix = ',courier-new, courier, monospace';

// 终端暗色模式 字典项
export const darkThemeKey = 'terminalDarkTheme';

// 终端字体样式
export const fontFamilyKey = 'terminalFontFamily';

// 终端字体大小
export const fontSizeKey = 'terminalFontSize';

// 终端字体字重
export const fontWeightKey = 'terminalFontWeight';

// 终端光标样式
export const cursorStyleKey = 'terminalCursorStyle';

// 加载的字典值
export const dictKeys = [darkThemeKey, fontFamilyKey, fontSizeKey, fontWeightKey, cursorStyleKey];
