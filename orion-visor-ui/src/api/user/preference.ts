import axios from 'axios';
import qs from 'query-string';

export type PreferenceType = 'SYSTEM' | 'TERMINAL'

/**
 * 用户偏好更新请求-单个
 */
export interface PreferenceUpdateRequest {
  type: PreferenceType;
  item: string;
  value: any;
}

/**
 * 用户偏好更新请求-部分
 */
export interface PreferenceUpdatePartialRequest {
  type: PreferenceType;
  config: Record<string, any> | object;
}

/**
 * 更新用户偏好-单个
 */
export function updatePreference(request: PreferenceUpdateRequest) {
  return axios.put('/infra/preference/update', request);
}

/**
 * 更新用户偏好-部分
 */
export function updatePreferencePartial(request: PreferenceUpdatePartialRequest) {
  return axios.put('/infra/preference/update-partial', request);
}

/**
 * 查询用户偏好
 */
export function getPreference<T>(type: PreferenceType, items: Array<string> | undefined = undefined) {
  return axios.get<T>('/infra/preference/get', {
    params: {
      type,
      items
    },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询默认偏好
 */
export function getDefaultPreference<T>(type: PreferenceType, items: Array<string> | undefined = undefined) {
  return axios.get<T>('/infra/preference/get-default', {
    params: {
      type,
      items
    },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

