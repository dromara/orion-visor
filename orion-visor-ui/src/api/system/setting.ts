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
  ['sftp.preview-size']: number;
  ['sftp.upload-present-backup']: string;
  ['sftp.upload-backup-file-name']: string;
}

/**
 * 登录设置
 */
export interface LoginSetting {
  ['login.allow-multi-device']: string;
  ['login.allow-refresh']: string;
  ['login.max-refresh-count']: number;
  ['login.refresh-interval']: number;
  ['login.login-failed-lock']: string;
  ['login.login-failed-lock-threshold']: number;
  ['login.login-failed-lock-time']: number;
  ['login.login-failed-send']: string;
  ['login.login-failed-send-threshold']: number;
  ['login.login-session-time']: number;
}

/**
 * 加密设置
 */
export interface EncryptSetting {
  ['encrypt.public-key']: string;
  ['encrypt.private-key']: string;
}

/**
 * 日志设置
 */
export interface LogSetting {
  ['log.web-scroll-lines']: number;
  ['log.tracker-load-lines']: number;
  ['log.tracker-load-interval']: number;
  ['log.exec-detail.enabled']: string;
}

/**
 * 自动清理设置
 */
export interface AutoClearSetting {
  ['auto-clear.exec-log.enabled']: string;
  ['auto-clear.exec-log.keep-days']: number;
  ['auto-clear.terminal-log.enabled']: string;
  ['auto-clear.terminal-log.keep-days']: number;
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
