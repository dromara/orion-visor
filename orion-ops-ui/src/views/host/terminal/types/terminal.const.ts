import type { CSSProperties } from 'vue';
import { getUUID } from '@/utils';

// sidebar 操作类型
export interface SidebarAction {
  icon: string;
  content: string;
  visible?: boolean;
  disabled?: boolean;
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
  SFTP_SETTING: {
    key: 'sftpSetting',
    title: 'sftp设置',
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

// TODO
// 显示设置
// 显示基础设置
// 右侧栏

// 交互设置
// 右键选中词条
// 右键粘贴
// 启用右键菜单
// 自动将选中内容复制到剪切板
// 粘贴时删除空格
// 复制时删除空格
// 分隔符  /\()"'-.,:;<>~!@#$%^&*|+=[]{}~?│   在终端中双击文本将使用到这些符号
// 自动检测 url 并可以点击
// 支持显示图片 使用 sixel 打开图片

// 终端设置
// bell sound
// terminal emulation type: xterm 256color
// 回滚（ScrollBack）
// 保存在缓冲区的行数



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

// 终端状态
export const TerminalStatus = {
  // 连接中
  CONNECTING: 0,
  // 已连接
  CONNECTED: 1,
  // 已断开
  CLOSED: 2
};

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
