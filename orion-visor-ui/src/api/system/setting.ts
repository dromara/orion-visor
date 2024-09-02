import axios from 'axios';

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
 * 查询 license 信息
 */
export function getSystemLicenseInfo() {
  return axios.get<SystemLicenseResponse>('/infra/system-setting/license');
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
  return axios.get<AppReleaseResponse>('https://visor.orionsec.cn/releases-latest.json', {
    // 不添加请求头 否则会报 401
    setAuthorization: false,
    // 返回原始输出
    unwrap: true,
    // 不提示请求错误信息 可能会 403
    promptRequestErrorMessage: false,
  });
}
