// tab 类型
import { ShortcutKeyItem } from '@/views/host/terminal/types/terminal.type';

export const TerminalTabType = {
  SETTING: 'setting',
  TERMINAL: 'terminal',
};

// 内置 tab
export const InnerTabs = {
  NEW_CONNECTION: {
    key: 'newConnection',
    title: '新建连接',
    icon: 'icon-plus',
    type: TerminalTabType.SETTING
  },
  SHORTCUT_SETTING: {
    key: 'shortcutSetting',
    title: '快捷键设置',
    icon: 'icon-command',
    type: TerminalTabType.SETTING
  },
  DISPLAY_SETTING: {
    key: 'displaySetting',
    title: '显示设置',
    icon: 'icon-stamp',
    type: TerminalTabType.SETTING
  },
  THEME_SETTING: {
    key: 'themeSetting',
    title: '主题设置',
    icon: 'icon-palette',
    type: TerminalTabType.SETTING
  },
  TERMINAL_SETTING: {
    key: 'terminalSetting',
    title: '终端设置',
    icon: 'icon-settings',
    type: TerminalTabType.SETTING
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
    item: 'selectAll',
    icon: 'icon-expand',
    content: '全选',
  }, {
    item: 'search',
    icon: 'icon-find-replace',
    content: '搜索',
  }, {
    item: 'copy',
    icon: 'icon-copy',
    content: '复制',
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
    item: 'fontSizePlus',
    icon: 'icon-zoom-in',
    content: '增大字号',
  }, {
    item: 'fontSizeSubtract',
    icon: 'icon-zoom-out',
    content: '减小字号',
  }, {
    item: 'commandEditor',
    icon: 'icon-code-square',
    content: '命令编辑器',
  }, {
    item: 'clear',
    icon: 'icon-delete',
    content: '清空',
  }, {
    item: 'disconnect',
    icon: 'icon-poweroff',
    content: '断开连接',
  }, {
    item: 'closeTab',
    icon: 'icon-close',
    content: '关闭终端',
  }
];

// 终端快捷键操作类型
export const TerminalShortcutType = {
  SYSTEM: 1,
  TERMINAL: 2
};

// 终端操作快捷键 key
export const TerminalShortcutKeys = {
  // 切换为前一个 tab
  CHANGE_TO_PREV_TAB: 'changeToPrevTab',
  // 切换为后一个 tab
  CHANGE_TO_NEXT_TAB: 'changeToNextTab',
  // 关闭 tab
  CLOSE_TAB: 'closeTab',
  // 打开新建连接 tab
  OPEN_NEW_CONNECT_TAB: 'openNewConnectTab',
};

// 终端操作快捷键
export const TerminalShortcutItems: Array<ShortcutKeyItem> = [
  {
    item: TerminalShortcutKeys.CHANGE_TO_PREV_TAB,
    content: '切换为前一个 tab',
    type: TerminalShortcutType.SYSTEM
  }, {
    item: TerminalShortcutKeys.CHANGE_TO_NEXT_TAB,
    content: '切换为后一个 tab',
    type: TerminalShortcutType.SYSTEM
  }, {
    item: TerminalShortcutKeys.CLOSE_TAB,
    content: '关闭当前 tab',
    type: TerminalShortcutType.SYSTEM
  }, {
    item: TerminalShortcutKeys.OPEN_NEW_CONNECT_TAB,
    content: '打开新建连接 tab',
    type: TerminalShortcutType.SYSTEM
  }, {
    item: 'openCopyTerminalTab',
    content: '复制当前终端 tab',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'copy',
    content: '复制',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'paste',
    content: '粘贴',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'toTop',
    content: '去顶部',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'toBottom',
    content: '去底部',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'selectAll',
    content: '全选',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'search',
    content: '搜索',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'fontSizePlus',
    content: '增大字号',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'fontSizeSubtract',
    content: '减小字号',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'commandEditor',
    content: '命令编辑器',
    type: TerminalShortcutType.TERMINAL
  },
];

// 代码片段操作提示
export const snippetTipsKey = 'snippet:opt';

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

// 终端类型
export const terminalEmulationTypeKey = 'terminalEmulationType';

// 加载的字典值
export const dictKeys = [
  fontFamilyKey, fontSizeKey,
  fontWeightKey, cursorStyleKey,
  newConnectionTypeKey, extraSshAuthTypeKey,
  connectStatusKey, terminalEmulationTypeKey
];
