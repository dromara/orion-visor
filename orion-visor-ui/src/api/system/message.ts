import type { Pagination } from '@/types/global';
import axios from 'axios';

/**
 * 系统消息查询请求
 */
export interface MessageQueryRequest extends Pagination {
  maxId?: number;
  classify?: string;
  queryUnread?: boolean;
}

/**
 * 系统消息查询响应
 */
export interface MessageRecordResponse {
  id: number;
  classify: string;
  type: string;
  status: number;
  relKey: string;
  title: string;
  content: string;
  contentHtml: string;
  createTime: number;
}

/**
 * 查询系统消息列表
 */
export function getSystemMessageList(request: MessageQueryRequest) {
  return axios.post<Array<MessageRecordResponse>>('/infra/system-message/list', request);
}

/**
 * 查询系统消息数量
 */
export function getSystemMessageCount(queryUnread: boolean) {
  return axios.get<Record<string, number>>('/infra/system-message/count', { params: { queryUnread } });
}

/**
 * 查询是否有未读消息
 */
export function checkHasUnreadMessage() {
  return axios.get<boolean>('/infra/system-message/has-unread');
}

/**
 * 更新系统消息为已读
 */
export function updateSystemMessageRead(id: number) {
  return axios.put('/infra/system-message/read', undefined, { params: { id } });
}

/**
 * 更新全部系统消息为已读
 */
export function updateSystemMessageReadAll(classify: string) {
  return axios.put('/infra/system-message/read-all', undefined, { params: { classify } });
}

/**
 * 删除系统消息
 */
export function deleteSystemMessage(id: number) {
  return axios.delete('/infra/system-message/delete', { params: { id } });
}

/**
 * 清理已读的系统消息
 */
export function clearSystemMessage(classify: string) {
  return axios.delete('/infra/system-message/clear', { params: { classify } });
}
