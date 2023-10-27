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
