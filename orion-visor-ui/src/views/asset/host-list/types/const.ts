import WindowsIcon from '@/assets/images/icon/os_windows.svg';
import LinuxIcon from '@/assets/images/icon/os_linux.svg';
import DarwinIcon from '@/assets/images/icon/os_darwin.svg';

// 表名称
export const TableName = 'host';

// tag 颜色
export const tagColor = [
  'arcoblue',
  'green',
  'purple',
  'pinkpurple',
  'magenta'
];

// 主机类型
export const HostType = {
  SSH: {
    value: 'SSH',
    port: 22,
  },
};

// 系统类型
export const HostOsType = {
  LINUX: {
    value: 'LINUX',
    icon: LinuxIcon,
  },
  WINDOWS: {
    value: 'WINDOWS',
    icon: WindowsIcon,
  },
  DARWIN: {
    value: 'DARWIN',
    icon: DarwinIcon,
  },
};

// 主机认证方式
export const HostAuthType = {
  // 密码认证
  PASSWORD: 'PASSWORD',
  // 密钥认证
  KEY: 'KEY',
  // 身份认证
  IDENTITY: 'IDENTITY'
};

// 获取系统类型 icon
export const getHostOsIcon = (osType: string) => {
  return HostOsType[osType as keyof typeof HostOsType]?.icon;
};

// 主机类型 字典项
export const hostTypeKey = 'hostType';

// 主机系统类型 字典项
export const hostOsTypeKey = 'hostOsType';

// 主机系统架构 字典项
export const hostArchTypeKey = 'hostArchType';

// 主机状态 字典项
export const hostStatusKey = 'hostStatus';

// 主机 SSH 认证方式 字典项
export const sshAuthTypeKey = 'hostSshAuthType';

// 加载的字典值
export const dictKeys = [hostTypeKey, hostOsTypeKey, hostArchTypeKey, hostStatusKey, sshAuthTypeKey];
