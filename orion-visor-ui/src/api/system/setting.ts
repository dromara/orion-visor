import axios from 'axios';

/**
 * 应用信息查询响应
 */
export interface AppInfoResponse {
  version: string;
  uuid: string;
}

/**
 * 仓库版本信息查询响应
 */
export interface RepoReleaseResponse {
  tag_name: string;
  body: string;
}

/**
 * 查询应用信息
 */
export function getSystemAppInfo() {
  return axios.get<AppInfoResponse>('/infra/system-setting/app-info');
}

/**
 * 获取仓库最后版本信息
 */
export function getRepoLatestRelease() {
  return axios.get<RepoReleaseResponse>('https://gitee.com/api/v5/repos/dromara/orion-visor/releases/latest', {
    // 不添加请求头 否则会报 401
    setAuthorization: false,
    // 返回原始输出
    unwrap: true,
    // 不提示请求错误信息 可能会 403
    promptRequestErrorMessage: false,
  });
}
