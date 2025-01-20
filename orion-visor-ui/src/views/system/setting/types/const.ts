import type { SystemSettingType } from '@/api/system/setting';

// 系统设置类型
export const SystemSettingTypes: Record<SystemSettingType, SystemSettingType> = {
  SFTP: 'SFTP',
  ENCRYPT: 'ENCRYPT',
};
