import type { CSSProperties } from 'vue';

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  iconStyle?: CSSProperties;
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

// ssh 额外配置
export interface SshExtraModel {
  authType?: string;
  username?: string;
  keyId?: number;
  identityId?: number;
}

// 主机额外配置 ssh 认证方式
export const ExtraSshAuthType = {
  // 使用默认认证方式
  DEFAULT: 'DEFAULT',
  // 自定义秘钥
  CUSTOM_KEY: 'CUSTOM_KEY',
  // 自定义身份
  CUSTOM_IDENTITY: 'CUSTOM_IDENTITY',
};

// 打开 sshModal key
export const openSshModalKey = Symbol();

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

// 终端新建连接类型
export const extraSshAuthTypeKey = 'hostExtraSshAuthType';

// 加载的字典值
export const dictKeys = [
  darkThemeKey, fontFamilyKey,
  fontSizeKey, fontWeightKey,
  cursorStyleKey, NewConnectionTypeKey,
  extraSshAuthTypeKey
];
