import type { CSSProperties } from 'vue';

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  iconStyle?: CSSProperties;
  visible?: boolean;
  click: () => void;
}

// tab 元素
export interface TabItem {
  key: string;
  title: string;
  type: string;

  [key: string]: unknown;
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
  VIEW_SETTING: {
    key: 'viewSetting',
    title: '外观设置',
    type: TabType.SETTING
  },
};

// 新建连接类型
export const NewConnectionType = {
  GROUP: 'group',
  LIST: 'list',
  FAVORITE: 'favorite',
  LATEST: 'latest'
};

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

// 终端新建连接类型
export const NewConnectionTypeKey = 'terminalNewConnectionType';

// 加载的字典值
export const dictKeys = [
  darkThemeKey, fontFamilyKey,
  fontSizeKey, fontWeightKey,
  cursorStyleKey, NewConnectionTypeKey
];
