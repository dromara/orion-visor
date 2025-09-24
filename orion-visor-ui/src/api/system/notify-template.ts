import type { TableData } from '@arco-design/web-vue';
import type { DataGrid, OrderDirection, Pagination } from '@/types/global';
import axios from 'axios';

/**
 * 通知模板创建请求
 */
export interface NotifyTemplateCreateRequest {
  name?: string;
  bizType?: string;
  channelType?: string;
  channelConfig?: string;
  messageTemplate?: string;
  description?: string;
}

/**
 * 通知模板更新请求
 */
export interface NotifyTemplateUpdateRequest extends NotifyTemplateCreateRequest {
  id?: number;
}

/**
 * 通知模板查询请求
 */
export interface NotifyTemplateQueryRequest extends Pagination, OrderDirection {
  id?: number;
  name?: string;
  bizType?: string;
  channelType?: string;
}

/**
 * 通知模板查询响应
 */
export interface NotifyTemplateQueryResponse extends TableData {
  id: number;
  name: string;
  bizType: string;
  channelType: string;
  channelConfig: string;
  messageTemplate: string;
  description: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
}

/**
 * 创建通知模板
 */
export function createNotifyTemplate(request: NotifyTemplateCreateRequest) {
  return axios.post<number>('/infra/notify-template/create', request);
}

/**
 * 更新通知模板
 */
export function updateNotifyTemplate(request: NotifyTemplateUpdateRequest) {
  return axios.put<number>('/infra/notify-template/update', request);
}

/**
 * 查询通知模板
 */
export function getNotifyTemplate(id: number) {
  return axios.get<NotifyTemplateQueryResponse>('/infra/notify-template/get', { params: { id } });
}

/**
 * 查询全部通知模板
 */
export function getNotifyTemplateList(bizType: string) {
  return axios.get<Array<NotifyTemplateQueryResponse>>('/infra/notify-template/list', { params: { bizType } });
}

/**
 * 分页查询通知模板
 */
export function getNotifyTemplatePage(request: NotifyTemplateQueryRequest) {
  return axios.post<DataGrid<NotifyTemplateQueryResponse>>('/infra/notify-template/query', request);
}

/**
 * 删除通知模板
 */
export function deleteNotifyTemplate(id: number) {
  return axios.delete<number>('/infra/notify-template/delete', { params: { id } });
}
