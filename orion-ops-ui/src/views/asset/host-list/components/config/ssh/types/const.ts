// 主机 SSH 配置
export interface HostSshConfig {
  port?: number;
  username?: string;
  password?: string;
  authType?: string;
  identityId?: number;
  keyId?: number;
  connectTimeout?: number;
  charset?: string;
  fileNameCharset?: string;
  fileContentCharset?: string;
  useNewPassword?: boolean;
  hasPassword?: boolean;
}

// 主机验证方式
export const AuthType = {
  // 密码验证
  PASSWORD: 'PASSWORD',
  // 秘钥验证
  KEY: 'KEY',
  // 身份验证
  IDENTITY: 'IDENTITY'
};

// 主机验证方式 字典项
export const authTypeKey = 'hostAuthTypeType';
