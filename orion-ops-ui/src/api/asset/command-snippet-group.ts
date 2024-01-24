import axios from 'axios';

/**
 * 命令片段分组创建请求
 */
export interface CommandSnippetGroupCreateRequest {
  name?: string;
}

/**
 * 命令片段分组更新请求
 */
export interface CommandSnippetGroupUpdateRequest extends CommandSnippetGroupCreateRequest {
  id?: number;
}

/**
 * 命令片段分组查询响应
 */
export interface CommandSnippetGroupQueryResponse {
  id: number;
  name: string;
}

/**
 * 创建命令片段分组
 */
export function createCommandSnippetGroup(request: CommandSnippetGroupCreateRequest) {
  return axios.post('/asset/command-snippet-group/create', request);
}

/**
 * 更新命令片段分组
 */
export function updateCommandSnippetGroup(request: CommandSnippetGroupUpdateRequest) {
  return axios.put('/asset/command-snippet-group/update', request);
}

/**
 * 查询全部命令片段分组
 */
export function getCommandSnippetGroupList() {
  return axios.get<Array<CommandSnippetGroupQueryResponse>>('/asset/command-snippet-group/list');
}

/**
 * 删除命令片段分组
 */
export function deleteCommandSnippetGroup(id: number) {
  return axios.delete('/asset/command-snippet-group/delete', { params: { id } });
}

