// 主机 SSH 配置
export interface HostSshConfig {
  osType?: string;
  username?: string;
  password?: string;
  authType?: string;
  keyId?: number;
  identityId?: number;
  connectTimeout?: number;
  charset?: string;
  fileNameCharset?: string;
  fileContentCharset?: string;
  useNewPassword?: boolean;
  hasPassword?: boolean;
}

// 主机验证方式
export const SshAuthType = {
  // 密码验证
  PASSWORD: 'PASSWORD',
  // 密钥验证
  KEY: 'KEY',
  // 身份验证
  IDENTITY: 'IDENTITY'
};

// 主机验证方式 字典项
export const sshAuthTypeKey = 'hostSshAuthType';

// 主机系统类型 字典项
export const sshOsTypeKey = 'hostSshOsType';

// 加载的字典值
export const dictKeys = [sshAuthTypeKey, sshOsTypeKey];
