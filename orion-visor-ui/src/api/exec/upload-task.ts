import type { ClearRequest, DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue';
import axios from 'axios';
import qs from 'query-string';

/**
 * 上传任务创建请求
 */
export interface UploadTaskCreateRequest {
  remotePath?: string;
  description?: string;
  hostIdList?: Array<number>;
  files?: Array<UploadTaskFileCreateRequest>;
}

/**
 * 上传任务文件创建请求
 */
export interface UploadTaskFileCreateRequest {
  fileId?: string;
  filePath?: string;
  fileSize?: number;
}

/**
 * 上传任务创建响应
 */
export interface UploadTaskCreateResponse {
  id: number;
  token: string;
}

/**
 * 上传任务查询请求
 */
export interface UploadTaskQueryRequest extends Pagination {
  id?: number;
  userId?: number;
  remotePath?: string;
  description?: string;
  status?: string;
  createTimeRange?: string[];
}

/**
 * 上传任务清理请求
 */
export interface UploadTaskClearRequest extends UploadTaskQueryRequest, ClearRequest {
}

/**
 * 上传任务查询响应
 */
export interface UploadTaskQueryResponse extends TableData {
  id: number;
  userId: number;
  username: string;
  remotePath: string;
  description: string;
  status: string;
  extraInfo: string;
  fileCount: number;
  hostCount: number;
  startTime: number;
  endTime: number;
  createTime: number;
  hosts: Array<UploadTaskHost>;
}

/**
 * 上传任务主机响应
 */
export interface UploadTaskHost {
  id: number;
  code: string;
  name: string;
  address: string;
  files: Array<UploadTaskFile>;
}

/**
 * 上传任务文件响应
 */
export interface UploadTaskFile {
  id: number;
  taskId: number;
  hostId: number;
  fileId: string;
  filePath: string;
  fileSize: number;
  status: string;
  errorMessage: string;
  startTime: number;
  endTime: number;
  current: number;
}

/**
 * 上传任务状态响应
 */
export interface UploadTaskStatusResponse extends TableData {
  id: number;
  status: string;
  startTime: number;
  endTime: number;
  files: Array<UploadTaskFile>;
}

/**
 * 创建上传任务
 */
export function createUploadTask(request: UploadTaskCreateRequest) {
  return axios.post<UploadTaskCreateResponse>('/asset/upload-task/create', request);
}

/**
 * 创建上传任务
 */
export function startUploadTask(id: number) {
  return axios.post('/asset/upload-task/start', { id });
}

/**
 * 创建上传任务
 */
export function cancelUploadTask(id: number, failed: boolean) {
  return axios.post('/asset/upload-task/cancel', { id, failed });
}

/**
 * 查询上传任务
 */
export function getUploadTask(id: number) {
  return axios.get<UploadTaskQueryResponse>('/asset/upload-task/get', { params: { id } });
}

/**
 * 分页查询上传任务
 */
export function getUploadTaskPage(request: UploadTaskQueryRequest) {
  return axios.post<DataGrid<UploadTaskQueryResponse>>('/asset/upload-task/query', request);
}

/**
 * 查询上传任务状态
 */
export function getUploadTaskStatus(idList: Array<number>, queryFiles: boolean) {
  return axios.get<Array<UploadTaskStatusResponse>>('/asset/upload-task/status', {
    params: { idList, queryFiles },
    promptBizErrorMessage: false,
    promptRequestErrorMessage: false,
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 删除上传任务
 */
export function deleteUploadTask(id: number) {
  return axios.delete('/asset/upload-task/delete', { params: { id } });
}

/**
 * 批量删除上传任务
 */
export function batchDeleteUploadTask(idList: Array<number>) {
  return axios.delete('/asset/upload-task/batch-delete', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询批量上传任务数量
 */
export function getUploadTaskCount(request: UploadTaskQueryRequest) {
  return axios.post<number>('/asset/upload-task/count', request);
}

/**
 * 清空查询批量上传任务
 */
export function clearUploadTask(request: UploadTaskClearRequest) {
  return axios.post<number>('/asset/upload-task/clear', request, {
    timeout: 60000,
  });
}
