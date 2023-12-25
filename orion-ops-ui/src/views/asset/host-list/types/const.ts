import type { HostSshConfig } from '../components/config/ssh/types/const';

// 主机所有配置
export interface HostConfigWrapper {
  ssh: HostSshConfig | unknown;

  [key: string]: unknown;
}

// 主机配置类型
export const HostConfigType = {
  SSH: 'ssh'
};

// tag 颜色
export const tagColor = [
  'arcoblue',
  'green',
  'purple',
  'pinkpurple',
  'magenta'
];
