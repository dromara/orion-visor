import { getUUID } from '@/utils';

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
  DISPLAY_SETTING: {
    key: 'displaySetting',
    title: '显示设置',
    type: TabType.SETTING
  },
  INTERACT_SETTING: {
    key: 'interactSetting',
    title: '交互设置',
    type: TabType.SETTING
  },
  THEME_SETTING: {
    key: 'themeSetting',
    title: '主题设置',
    type: TabType.SETTING
  },
  TERMINAL_SETTING: {
    key: 'terminalSetting',
    title: '终端设置',
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

// 主机额外配置 ssh 认证方式
export const ExtraSshAuthType = {
  // 使用默认认证方式
  DEFAULT: 'DEFAULT',
  // 自定义秘钥
  CUSTOM_KEY: 'CUSTOM_KEY',
  // 自定义身份
  CUSTOM_IDENTITY: 'CUSTOM_IDENTITY',
};

// 终端状态
export const TerminalStatus = {
  // 连接中
  CONNECTING: 0,
  // 已连接
  CONNECTED: 1,
  // 已断开
  CLOSED: 2
};

// 终端操作栏-操作项
export const ActionBarItems = [
  {
    item: 'toTop',
    icon: 'icon-up',
    content: '去顶部',
  }, {
    item: 'toBottom',
    icon: 'icon-down',
    content: '去底部',
  }, {
    item: 'checkAll',
    icon: 'icon-expand',
    content: '全选',
  }, {
    item: 'copy',
    icon: 'icon-copy',
    content: '复制选中部分',
  }, {
    item: 'paste',
    icon: 'icon-paste',
    content: '粘贴',
  }, {
    item: 'interrupt',
    icon: 'icon-formula',
    content: 'ctrl + c',
  }, {
    item: 'enter',
    icon: 'icon-play-arrow-fill',
    content: '回车',
  }, {
    item: 'commandEditor',
    icon: 'icon-code-square',
    content: '命令编辑器',
  }, {
    item: 'search',
    icon: 'icon-search',
    content: '搜索',
  }, {
    item: 'fontSizePlus',
    icon: 'icon-zoom-in',
    content: '增大字号',
  }, {
    item: 'fontSizeSubtract',
    icon: 'icon-zoom-out',
    content: '减小字号',
  }, {
    item: 'clear',
    icon: 'icon-delete',
    content: '清空',
  }, {
    item: 'disconnect',
    icon: 'icon-poweroff',
    content: '断开连接',
  }, {
    item: 'close',
    icon: 'icon-close',
    content: '关闭',
  }
];

// 获取会话id
export const nextSessionId = (): string => {
  return getUUID().replaceAll('-', '').substring(0, 10);
};

// 打开 sshModal key
export const openSshModalKey = Symbol();

// 字体后缀 兜底
export const fontFamilySuffix = ',courier-new, courier, monospace';

// 终端字体样式
export const fontFamilyKey = 'terminalFontFamily';

// 终端字体大小
export const fontSizeKey = 'terminalFontSize';

// 终端字体字重
export const fontWeightKey = 'terminalFontWeight';

// 终端光标样式
export const cursorStyleKey = 'terminalCursorStyle';

// 终端新建连接类型
export const newConnectionTypeKey = 'terminalNewConnectionType';

// 终端新建连接类型
export const extraSshAuthTypeKey = 'hostExtraSshAuthType';

// 终端状态
export const connectStatusKey = 'terminalConnectStatus';

// 加载的字典值
export const dictKeys = [
  fontFamilyKey,
  fontSizeKey, fontWeightKey,
  cursorStyleKey, newConnectionTypeKey,
  extraSshAuthTypeKey, connectStatusKey
];
