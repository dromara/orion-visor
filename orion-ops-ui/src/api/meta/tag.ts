import axios from 'axios';

export type TagType = 'HOST'

/**
 * tag 创建对象
 */
export interface TagCreateRequest {
  name: number;
  type: TagType;
}

/**
 * tag 响应对象
 */
export interface TagQueryResponse {
  id: number;
  name: string;
}

/**
 * 创建标签
 */
export function createTag(request: TagCreateRequest) {
  return axios.post('/infra/tag/create', request);
}

/**
 * 查询标签
 */
export function getTagList(type: TagType) {
  return axios.get<TagQueryResponse>('/infra/tag/list', { params: { type } });
}
