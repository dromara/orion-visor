import type { CombinationKeyItem, ShortcutKeyItem } from './define';

// 首页推荐数量
export const emptyRecommendCount = 7;

// 自适应显示值
export const fitDisplayValue = 'fit';

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

// 终端会话 tab 类型
export const TerminalSessionTypes = {
  SSH: {
    type: 'SSH',
    protocol: 'SSH',
    channel: 'ssh',
    icon: 'icon-desktop',
    connectIcon: 'icon-thunderbolt',
  },
  SFTP: {
    type: 'SFTP',
    protocol: 'SSH',
    channel: 'sftp',
    icon: 'icon-folder',
    connectIcon: 'icon-folder',
  },
  RDP: {
    type: 'RDP',
    protocol: 'RDP',
    channel: 'rdp',
    icon: 'icon-desktop',
    connectIcon: 'icon-desktop',
  },
  VNC: {
    type: 'VNC',
    protocol: 'VNC',
    channel: 'vnc',
    icon: 'icon-computer',
    connectIcon: 'icon-computer',
  },
};

// 新建连接类型
export const NewConnectionType = {
  GROUP: 'group',
  LIST: 'list',
  FAVORITE: 'favorite',
  LATEST: 'latest'
};

// 主机额外配置项
export const ExtraSettingItems = {
  LABEL: 'LABEL',
  SSH: 'SSH',
  RDP: 'RDP',
  VNC: 'VNC',
};

// 主机额外配置认证方式
export const ExtraHostAuthType = {
  // 使用默认认证方式
  DEFAULT: 'DEFAULT',
  // 自定义密钥
  CUSTOM_KEY: 'CUSTOM_KEY',
  // 自定义身份
  CUSTOM_IDENTITY: 'CUSTOM_IDENTITY',
};

// 终端关闭码
export const TerminalCloseCode = {
  NORMAL: 0,
  FORCE: 10000,
  LOGGED_ELSEWHERE: 521,
  CONNECT_TIMEOUT: 10010,
};

// 终端消息
export const TerminalMessages = {
  sessionClosed: '会话已结束...',
  waitingReconnect: '输入回车重新连接...',
  loggedElsewhere: '该账号已在另一台设备登录',
  connectTimeout: '请检查远程计算机网络及其他配置是否正常',
  fileTransferError: '传输失败',
  fileSaveError: '保存失败',
  fileUploading: '已开始上传, 点击右侧传输列表查看进度',
  fileDownloading: '已开始下载, 点击右侧传输列表查看进度',
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

// 终端状态
export const TerminalStatus = {
  // 连接中
  CONNECTING: 0,
  // 已连接
  CONNECTED: 1,
  // 已断开
  CLOSED: 2
};

// 终端操作栏 - SSH
export const SshActionBarItems = [
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
    item: 'openSftp',
    icon: 'icon-folder',
    content: '打开 SFTP',
  }, {
    item: 'uploadFile',
    icon: 'icon-upload',
    content: '上传文件',
  }, {
    item: 'clear',
    icon: 'icon-delete',
    content: '清空',
  }, {
    item: 'disconnect',
    icon: 'icon-poweroff',
    content: '断开连接',
  }
];

// guacd 终端操作栏键
export const GuacdActionItemKeys = {
  DISPLAY: 'display',
  COMBINATION_KEY: 'combinationKey',
  CLIPBOARD: 'clipboard',
  UPLOAD: 'upload',
  SAVE_RDP: 'saveRdp',
  DISCONNECT: 'disconnect',
  CLOSE: 'close',
};

// guacd 终端操作栏
export const GuacdActionBarItemMap = {
  [GuacdActionItemKeys.DISPLAY]: {
    item: GuacdActionItemKeys.DISPLAY,
    icon: 'icon-desktop',
    content: '显示设置',
  },
  [GuacdActionItemKeys.COMBINATION_KEY]: {
    item: GuacdActionItemKeys.COMBINATION_KEY,
    icon: 'icon-command',
    content: '组合键',
  },
  [GuacdActionItemKeys.CLIPBOARD]: {
    item: GuacdActionItemKeys.CLIPBOARD,
    icon: 'icon-paste',
    content: '剪切板',
  },
  [GuacdActionItemKeys.UPLOAD]: {
    item: GuacdActionItemKeys.UPLOAD,
    icon: 'icon-upload',
    content: '文件上传',
  },
  [GuacdActionItemKeys.SAVE_RDP]: {
    item: GuacdActionItemKeys.SAVE_RDP,
    icon: 'icon-save',
    content: '保存 rdp 文件',
  },
  [GuacdActionItemKeys.DISCONNECT]: {
    item: GuacdActionItemKeys.DISCONNECT,
    icon: 'icon-stop',
    content: '断开连接',
  },
  [GuacdActionItemKeys.CLOSE]: {
    item: GuacdActionItemKeys.CLOSE,
    icon: 'icon-close',
    content: '关闭工具栏',
  },
};

// 终端操作栏 - RDP
export const RdpActionBarItems = [
  GuacdActionBarItemMap[GuacdActionItemKeys.DISPLAY],
  GuacdActionBarItemMap[GuacdActionItemKeys.COMBINATION_KEY],
  GuacdActionBarItemMap[GuacdActionItemKeys.CLIPBOARD],
  GuacdActionBarItemMap[GuacdActionItemKeys.UPLOAD],
  GuacdActionBarItemMap[GuacdActionItemKeys.SAVE_RDP],
  GuacdActionBarItemMap[GuacdActionItemKeys.DISCONNECT],
  GuacdActionBarItemMap[GuacdActionItemKeys.CLOSE],
];

// 终端操作栏 - VNC
export const VncActionBarItems = [
  GuacdActionBarItemMap[GuacdActionItemKeys.DISPLAY],
  GuacdActionBarItemMap[GuacdActionItemKeys.COMBINATION_KEY],
  GuacdActionBarItemMap[GuacdActionItemKeys.CLIPBOARD],
  GuacdActionBarItemMap[GuacdActionItemKeys.DISCONNECT],
  GuacdActionBarItemMap[GuacdActionItemKeys.CLOSE],
];

// 终端操作栏方向
export const ActionBarPosition = {
  TOP: 'top',
  RIGHT: 'right',
};

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
  // 打开命令片段
  OPEN_COMMAND_SNIPPET: 'openCommandSnippet',
  // 打开书签路径
  OPEN_PATH_BOOKMARK: 'openPathBookmark',
  // 打开文件传输列表
  OPEN_TRANSFER_LIST: 'openTransferList',
  // 打开发送命令
  OPEN_COMMAND_BAR: 'openCommandBar',
  // 截图
  SCREENSHOT: 'screenshot',
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
  // 粘贴
  PASTE: 'paste',
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
    item: TerminalShortcutKeys.OPEN_COMMAND_SNIPPET,
    content: '打开命令片段',
    type: TerminalShortcutType.GLOBAL
  }, {
    item: TerminalShortcutKeys.OPEN_PATH_BOOKMARK,
    content: '打开书签路径',
    type: TerminalShortcutType.GLOBAL
  }, {
    item: TerminalShortcutKeys.OPEN_TRANSFER_LIST,
    content: '打开文件传输列表',
    type: TerminalShortcutType.GLOBAL
  }, {
    item: TerminalShortcutKeys.OPEN_COMMAND_BAR,
    content: '打开发送命令',
    type: TerminalShortcutType.GLOBAL
  }, {
    item: TerminalShortcutKeys.SCREENSHOT,
    content: '截图',
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
    item: TerminalShortcutKeys.PASTE,
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
    item: 'uploadFile',
    content: '上传文件',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'fontSizePlus',
    content: '增大字号',
    type: TerminalShortcutType.TERMINAL
  }, {
    item: 'fontSizeSubtract',
    content: '减小字号',
    type: TerminalShortcutType.TERMINAL
  },
];

// Guacd 组合键元素
export const GuacdCombinationKeyItems: Array<CombinationKeyItem> = [
  {
    keys: [65307],
    name: 'Esc'
  },
  {
    keys: [65480],
    name: 'F11'
  },
  {
    keys: [65507, 65513, 65535],
    name: 'Ctrl+Alt+Delete'
  },
  {
    keys: [65507, 65513, 65288],
    name: 'Ctrl+Alt+Backspace'
  },
  {
    keys: [65513, 65289],
    name: 'Alt+Tab'
  },
  {
    keys: [65515],
    name: 'Windows'
  },
  {
    keys: [65515, 100],
    name: 'Windows+D'
  },
  {
    keys: [65515, 101],
    name: 'Windows+E'
  },
  {
    keys: [65515, 114],
    name: 'Windows+R'
  },
  {
    keys: [65515, 120],
    name: 'Windows+X'
  }, {
    keys: [65507, 99],
    name: 'Ctrl+C'
  },
  {
    keys: [65507, 118],
    name: 'Ctrl+V'
  },
  {
    keys: [65507, 120],
    name: 'Ctrl+X'
  },
  {
    keys: [65507, 97],
    name: 'Ctrl+A'
  },
  {
    keys: [65507, 122],
    name: 'Ctrl+Z'
  },
  {
    keys: [65507, 65535],
    name: 'Ctrl+Delete'
  },
  {
    keys: [65507, 65288],
    name: 'Ctrl+Backspace'
  },
  {
    keys: [65507, 65513, 65470],
    name: 'Ctrl+Alt+F1'
  },
  {
    keys: [65507, 65513, 65471],
    name: 'Ctrl+Alt+F2'
  },
  {
    keys: [65507, 65513, 65472],
    name: 'Ctrl+Alt+F3'
  },
  {
    keys: [65507, 65513, 65473],
    name: 'Ctrl+Alt+F4'
  },
  {
    keys: [65507, 65513, 65474],
    name: 'Ctrl+Alt+F5'
  },
  {
    keys: [65507, 65513, 65475],
    name: 'Ctrl+Alt+F6'
  },
  {
    keys: [65507, 65513, 65476],
    name: 'Ctrl+Alt+F7'
  },
  {
    keys: [65507, 65513, 65477],
    name: 'Ctrl+Alt+F8'
  },
  {
    keys: [65507, 65513, 65478],
    name: 'Ctrl+Alt+F9'
  },
  {
    keys: [65507, 65513, 65479],
    name: 'Ctrl+Alt+F10'
  },
  {
    keys: [65507, 65513, 65480],
    name: 'Ctrl+Alt+F11'
  },
  {
    keys: [65507, 65513, 65481],
    name: 'Ctrl+Alt+F12'
  }
];

// backspace 字符
export const BACKSPACE_CHAR = String.fromCharCode(127);

// ctrl^h 字符
export const CTRL_H_CHAR = String.fromCharCode(8);

// 传输状态
export const TransferStatus = {
  WAITING: 'waiting',
  TRANSFERRING: 'transferring',
  SUCCESS: 'success',
  ERROR: 'error',
};

// 传输类型
export const TransferType = {
  UPLOAD: 'upload',
  DOWNLOAD: 'download',
};

// 传输来源
export const TransferSource = {
  SFTP: 'sftp',
  RDP: 'rdp',
};

// 传输操作
export const TransferOperator = {
  START: 'start',
  FINISH: 'finish',
  ERROR: 'error',
  ABORT: 'abort',
};

// 传输响应
export const TransferReceiver = {
  NEXT_PART: 'nextPart',
  START: 'start',
  PROGRESS: 'progress',
  FINISH: 'finish',
  ERROR: 'error',
  ABORT: 'abort',
};

// 路径书签类型
export const PathBookmarkType = {
  FILE: 'FILE',
  DIR: 'DIR',
};

// 打开 extraModal key
export const openExtraModalKey = Symbol();

// 终端字体样式
export const fontFamilyKey = 'terminalFontFamily';

// 终端字体大小
export const fontSizeKey = 'terminalFontSize';

// 终端字体字重
export const fontWeightKey = 'terminalFontWeight';

// 终端光标样式
export const cursorStyleKey = 'terminalCursorStyle';

// 伪终端类型
export const emulationTypeKey = 'terminalEmulationType';

// 图形操作栏位置
export const graphActionBarPositionKey = 'graphActionBarPosition';

// 屏幕分辨率
export const screenResolutionKey = 'screenResolution';

// 图形化色彩深度
export const graphColorDepthKey = 'graphColorDepth';

// 驱动挂载模式
export const driveMountModeKey = 'driveMountMode';

// vnc光标
export const vcnCursorKey = 'vcnCursor';

// 新建连接类型
export const newConnectionTypeKey = 'hostNewConnectionType';

// ssh 额外认证方式
export const extraSshAuthTypeKey = 'hostExtraSshAuthType';

// 密码额外认证方式
export const extraPasswordAuthTypeKey = 'hostExtraPasswordAuthType';

// 终端状态
export const connectStatusKey = 'terminalSessionStatus';

// 终端标签颜色
export const tabColorKey = 'terminalTabColor';

// SFTP 传输状态
export const transferStatusKey = 'sftpTransferStatus';

// 路径书签类型
export const pathBookmarkTypeKey = 'pathBookmarkType';

// 加载的字典值
export const dictKeys = [
  fontFamilyKey, fontSizeKey, fontWeightKey, cursorStyleKey, emulationTypeKey,
  graphActionBarPositionKey, graphColorDepthKey, screenResolutionKey, driveMountModeKey, vcnCursorKey,
  newConnectionTypeKey, connectStatusKey, tabColorKey,
  extraSshAuthTypeKey, extraPasswordAuthTypeKey,
  transferStatusKey,
  pathBookmarkTypeKey
];
