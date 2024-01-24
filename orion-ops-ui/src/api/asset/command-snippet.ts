import type { CommandSnippetGroupQueryResponse } from './command-snippet-group';
import axios from 'axios';

/**
 * 命令片段创建请求
 */
export interface CommandSnippetCreateRequest {
  groupId?: number;
  name?: string;
  command?: string;
}

/**
 * 命令片段更新请求
 */
export interface CommandSnippetUpdateRequest extends CommandSnippetCreateRequest {
  id?: number;
}

/**
 * 命令片段查询响应
 */
export interface CommandSnippetQueryResponse extends CommandSnippetQueryResponseExtra {
  id: number;
  groupId: number;
  name: string;
  command: string;
}

export interface CommandSnippetQueryResponseExtra {
  visible: boolean;
  expand: boolean;
}

/**
 * 命令片段查询响应
 */
export interface CommandSnippetWrapperResponse {
  groups: Array<CommandSnippetGroupQueryResponse>;
  ungroupedItems: Array<CommandSnippetQueryResponse>;
}

/**
 * 创建命令片段
 */
export function createCommandSnippet(request: CommandSnippetCreateRequest) {
  return axios.post('/asset/command-snippet/create', request);
}

/**
 * 更新命令片段
 */
export function updateCommandSnippet(request: CommandSnippetUpdateRequest) {
  return axios.put('/asset/command-snippet/update', request);
}

/**
 * 查询全部命令片段
 */
export function getCommandSnippetList() {
  return axios.get<CommandSnippetWrapperResponse>('/asset/command-snippet/list');
}

/**
 * 删除命令片段
 */
export function deleteCommandSnippet(id: number) {
  return axios.delete('/asset/command-snippet/delete', { params: { id } });
}
