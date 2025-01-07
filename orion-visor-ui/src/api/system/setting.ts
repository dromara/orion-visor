import axios from 'axios';
import { dateFormat } from '@/utils';

/**
 * 系统配置类型
 */
export type SystemSettingType = 'SFTP';

/**
 * 系统设置更新请求
 */
export interface SystemSettingUpdateRequest {
  type?: SystemSettingType;
  item?: string;
  value?: any;
  settings?: Record<string, any>;
}

/**
 * 应用信息查询响应
 */
export interface SystemLicenseResponse {
  userCount: number;
  hostCount: number;
  release: string;
  releaseName: string;
  issueDate: number;
  expireDate: number;
  expireDay: number;
  uuid: string;
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
  body: string;
}

/**
 * SFTP 配置
 */
export interface SftpSetting {
  previewSize: number;
}

/**
 * 查询应用信息
 */
export function getSystemAppInfo() {
  return axios.get<AppInfoResponse>('/infra/system-setting/app-info');
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
 * 更新系统设置
 */
export function updateSystemSetting(request: SystemSettingUpdateRequest) {
  return axios.put<number>('/infra/system-setting/update', request);
}

/**
 * 更新部分系统设置
 */
export function updatePartialSystemSetting(request: SystemSettingUpdateRequest) {
  return axios.put<number>('/infra/system-setting/update-partial', request);
}

/**
 * 查询系统设置
 */
export function getSystemSetting<T>(type: SystemSettingType) {
  return axios.get<T>('/infra/system-setting/setting', { params: { type } });
}
