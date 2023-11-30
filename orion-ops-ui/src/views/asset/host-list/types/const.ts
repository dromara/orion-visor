import type { HostSshConfig } from '../components/config/ssh/types/const';

// 主机所有配置
export interface HostConfigWrapper {
  SSH: HostSshConfig | unknown;

  [key: string]: unknown;
}

// tag 颜色
export const tagColor = [
  'arcoblue',
  'green',
  'purple',
  'pinkpurple',
  'magenta'
];

// 加载的字典值
export const dictKeys = ['hostAuthTypeType'];
