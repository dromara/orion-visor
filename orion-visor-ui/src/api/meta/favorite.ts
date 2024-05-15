import axios from 'axios';

export type FavoriteType = 'HOST'

/**
 * 收藏操作对象
 */
export interface FavoriteOperatorRequest {
  relId: number;
  type: FavoriteType;
}

/**
 * 添加收藏
 */
export function addFavorite(request: FavoriteOperatorRequest) {
  return axios.put('/infra/favorite/add', request);
}

/**
 * 取消收藏
 */
export function cancelFavorite(request: FavoriteOperatorRequest) {
  return axios.put('/infra/favorite/cancel', request);
}
