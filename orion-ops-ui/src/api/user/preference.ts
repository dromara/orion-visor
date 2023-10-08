import axios from 'axios';

type Preference = 'SYSTEM'

/**
 * 用户偏好更新请求
 */
export interface PreferenceUpdateRequest {
  type: Preference;
  config: object;
}

/**
 * 用户偏好查询响应
 */
export interface PreferenceQueryResponse {
  config: object;
}

/**
 * 更新用户偏好-整体
 */
export function updatePreference(request: PreferenceUpdateRequest) {
  return axios.put('/infra/preference/update', request);
}

/**
 * 更新用户偏好-部分
 */
export function updatePreferencePartial(request: PreferenceUpdateRequest) {
  return axios.put('/infra/preference/update-partial', request);
}

/**
 * 查询用户偏好
 */
export function getPreference(type: Preference) {
  return axios.get<PreferenceQueryResponse>('/infra/preference/get', { params: { type } });
}

