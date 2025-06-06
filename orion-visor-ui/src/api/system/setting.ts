import axios from 'axios';
import { dateFormat } from '@/utils';

/**
 * 系统设置更新请求
 */
export interface SystemSettingUpdateRequest {
  type?: string;
  value?: string;
  settings?: Record<string, any>;
}

/**
 * 应用信息查询响应
 */
export interface AppInfoResponse {
  version: string;
  uuid: string;
}

/**
 * 应用最新版本信息
 */
export interface AppReleaseResponse {
  tagName: string;
  releaseTime: string;
  body: string;
}

/**
 * RSA 密钥对响应
 */
export interface RsaKeyPairResponse {
  publicKey: string;
  privateKey: string;
}

/**
 * 系统设置
 */
export type SystemSetting = SftpSetting
  & LoginSetting & EncryptSetting
  & LogSetting & AutoClearSetting;

/**
 * SFTP 设置
 */
export interface SftpSetting {
  sftp_previewSize: number;
  sftp_uploadPresentBackup: string;
  sftp_uploadBackupFileName: string;
}

/**
 * 登录设置
 */
export interface LoginSetting {
  login_allowMultiDevice: string;
  login_allowRefresh: string;
  login_maxRefreshCount: number;
  login_refreshInterval: number;
  login_loginFailedLock: string;
  login_loginFailedLockThreshold: number;
  login_loginFailedLockTime: number;
  login_loginFailedSend: string;
  login_loginFailedSendThreshold: number;
  login_loginSessionTime: number;
}

/**
 * 加密设置
 */
export interface EncryptSetting {
  encrypt_publicKey: string;
  encrypt_privateKey: string;
}

/**
 * 日志设置
 */
export interface LogSetting {
  log_webScrollLines: number;
  log_trackerLoadLines: number;
  log_trackerLoadInterval: number;
  log_execDetailLog: string;
}

/**
 * 自动清理设置
 */
export interface AutoClearSetting {
  autoClear_execLogEnabled: string;
  autoClear_execLogKeepDays: number;
  autoClear_terminalLogEnabled: string;
  autoClear_terminalLogKeepDays: number;
}

/**
 * 查询应用信息
 */
export function getSystemAppInfo() {
  return axios.get<AppInfoResponse>('/infra/system-setting/app-info');
}

/**
 * 获取系统聚合设置
 */
export function getSystemAggregateSetting() {
  return axios.get<Record<keyof SystemSetting, string>>('/infra/system-setting/setting');
}

/**
 * 获取应用最新版本信息
 */
export function getAppLatestRelease() {
  return axios.get<AppReleaseResponse>(`https://visor.orionsec.cn/releases-latest.json?${dateFormat(new Date(), 'yyyyMMddHH')}`, {
    // 不添加请求头 否则会报 401
    setAuthorization: false,
    // 返回原始输出
    unwrap: true,
    // 不提示请求错误信息 可能会 403
    promptRequestErrorMessage: false,
  });
}

/**
 * 生成密钥对
 */
export function generatorKeypair() {
  return axios.get<RsaKeyPairResponse>('/infra/system-setting/generator-keypair');
}

/**
 * 更新系统设置-单个
 */
export function updateSystemSetting(request: SystemSettingUpdateRequest) {
  return axios.put<string>('/infra/system-setting/update', request);
}

/**
 * 更新系统设置-多个
 */
export function updateSystemSettingBatch(request: SystemSettingUpdateRequest) {
  return axios.put<string>('/infra/system-setting/update-batch', request);
}

/**
 * 查询系统设置
 */
export function getSystemSetting(type: string) {
  return axios.get<Record<keyof SystemSetting, string>>('/infra/system-setting/get', { params: { type } });
}
