import { ShortcutKeyItem } from './terminal.type';

// 终端 tab
export const TerminalTabs = {
  NEW_CONNECTION: {
    key: 'newConnection',
    title: '新建连接',
    icon: 'icon-plus',
  },
  SHORTCUT_SETTING: {
    key: 'shortcutSetting',
    title: '快捷键设置',
    icon: 'icon-command',
  },
  DISPLAY_SETTING: {
    key: 'displaySetting',
    title: '显示设置',
    icon: 'icon-stamp',
  },
  THEME_SETTING: {
    key: 'themeSetting',
    title: '主题设置',
    icon: 'icon-palette',
  },
  TERMINAL_SETTING: {
    key: 'terminalSetting',
    title: '终端设置',
    icon: 'icon-settings',
  },
  TERMINAL_PANEL: {
    key: 'terminalPanel',
    title: '主机终端',
    icon: 'icon-desktop',
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

// 文件类型
export const FILE_TYPE = {
  NORMAL_FILE: {
    value: '-',
    label: '普通文件',
    icon: 'icon-file'
  },
  DIRECTORY: {
    value: 'd',
    label: '目录',
    icon: 'icon-folder'
  },
  LINK_FILE: {
    value: 'l',
    label: '链接文件',
    icon: 'icon-link'
  },
  MANAGE_FILE: {
    value: 'p',
    label: '管理文件',
    icon: 'icon-drive-file'
  },
  BLOCK_DEVICE_FILE: {
    value: 'b',
    label: '块设备文件',
    icon: 'icon-drive-file'
  },
  CHARACTER_DEVICE_FILE: {
    value: 'c',
    label: '字符设备文件',
    icon: 'icon-drive-file'
  },
  SOCKET_FILE: {
    value: 's',
    label: '套接字文件',
    icon: 'icon-drive-file'
  }
};

// 面板会话 tab 类型
export const PanelSessionType = {
  SSH: {
    type: 'SSH',
    icon: 'icon-desktop'
  },
  SFTP: {
    type: 'SFTP',
    icon: 'icon-folder'
  },
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
  GLOBAL: 1,
  PANEL: 2,
  SESSION: 3,
  TERMINAL: 4
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
  // 打开新建连接弹框
  OPEN_NEW_CONNECT_MODAL: 'openNewConnectModal',
  // 复制会话
  COPY_SESSION: 'copySession',
  // 关闭会话
  CLOSE_SESSION: 'closeSession',
  // 切换至前一个会话
  CHANGE_TO_PREV_SESSION: 'changeToPrevSession',
  // 切换至后一个会话
  CHANGE_TO_NEXT_SESSION: 'changeToNextSession',
};

// 终端操作快捷键
export const TerminalShortcutItems: Array<ShortcutKeyItem> = [
  {
    item: TerminalShortcutKeys.CHANGE_TO_PREV_TAB,
    content: '切换为前一个 tab',
    type: TerminalShortcutType.GLOBAL
  }, {
    item: TerminalShortcutKeys.CHANGE_TO_NEXT_TAB,
    content: '切换为后一个 tab',
    type: TerminalShortcutType.GLOBAL
  }, {
    item: TerminalShortcutKeys.CLOSE_TAB,
    content: '关闭当前 tab',
    type: TerminalShortcutType.GLOBAL
  }, {
    item: TerminalShortcutKeys.OPEN_NEW_CONNECT_TAB,
    content: '打开新建连接 tab',
    type: TerminalShortcutType.GLOBAL
  }, {
    item: TerminalShortcutKeys.OPEN_NEW_CONNECT_MODAL,
    content: '打开新建连接弹框',
    type: TerminalShortcutType.SESSION
  }, {
    item: TerminalShortcutKeys.COPY_SESSION,
    content: '复制会话',
    type: TerminalShortcutType.SESSION
  }, {
    item: TerminalShortcutKeys.CLOSE_SESSION,
    content: '关闭会话',
    type: TerminalShortcutType.SESSION
  }, {
    item: TerminalShortcutKeys.CHANGE_TO_PREV_SESSION,
    content: '切换至前一个会话',
    type: TerminalShortcutType.SESSION
  }, {
    item: TerminalShortcutKeys.CHANGE_TO_NEXT_SESSION,
    content: '切换至后一个会话',
    type: TerminalShortcutType.SESSION
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

// 新建连接类型
export const newConnectionTypeKey = 'hostNewConnectionType';

// ssh 认证类型
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
