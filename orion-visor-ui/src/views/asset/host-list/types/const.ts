import WindowsIcon from '@/assets/images/icon/os_windows.svg';
import LinuxIcon from '@/assets/images/icon/os_linux.svg';

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
};

// 主机类型 字典项
export const hostTypeKey = 'hostType';

// 主机系统类型 字典项
export const hostOsTypeKey = 'hostOsType';

// 主机状态 字典项
export const hostStatusKey = 'hostStatus';

// 主机认证方式 字典项
export const sshAuthTypeKey = 'hostSshAuthType';

// 加载的字典值
export const dictKeys = [hostTypeKey, hostOsTypeKey, hostStatusKey, sshAuthTypeKey];
