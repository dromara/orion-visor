/**
 * 验证方式
 */
export const AuthTypeEnum = {
  PASSWORD: {
    value: 'PASSWORD',
    label: '密码验证',
  },
  KEY: {
    value: 'KEY',
    label: '秘钥验证',
  },
  IDENTITY: {
    value: 'IDENTITY',
    label: '身份验证',
  },
};

/**
 * 主机所有配置
 */
export interface HostConfigWrapper {
  SSH: HostSshConfig | unknown;

  [key: string]: unknown;
}

/**
 * 主机 SSH 配置
 */
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
