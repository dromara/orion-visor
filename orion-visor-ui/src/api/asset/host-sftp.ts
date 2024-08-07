import type { DataGrid, Pagination } from '@/types/global';
import type { TableData } from '@arco-design/web-vue/es/table/interface';
import { httpBaseUrl } from '@/utils/env';
import axios from 'axios';
import qs from 'query-string';

/**
 * SFTP 操作日志 查询请求
 */
export interface HostSftpLogQueryRequest extends Pagination {
  userId?: number;
  hostId?: number;
  type?: string;
  result?: number;
  startTimeRange?: string[];
}

/**
 * SFTP 操作日志 查询响应
 */
export interface HostSftpLogQueryResponse extends TableData {
  id: number;
  userId: number;
  username: number;
  hostId: number;
  hostName: string;
  hostAddress: string;
  address: string;
  location: string;
  userAgent: string;
  paths: string[];
  type: string;
  result: string;
  startTime: number;
  extra: HostSftpLogExtra;
}

/**
 * SFTP 操作日志 拓展对象
 */
export interface HostSftpLogExtra {
  mod: number;
  target: string;
  maxCount: number;
}

/**
 * 分页查询 SFTP 操作日志
 */
export function getHostSftpLogPage(request: HostSftpLogQueryRequest) {
  return axios.post<DataGrid<HostSftpLogQueryResponse>>('/asset/host-sftp/query-log', request);
}

/**
 * 删除 SFTP 操作日志
 */
export function deleteHostSftpLog(idList: Array<number>) {
  return axios.delete('/asset/host-sftp/delete-log', {
    params: { idList },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 下载文件
 */
export function getDownloadTransferUrl(channelId: string, transferToken: string) {
  return `${httpBaseUrl}/asset/host-sftp/download?channelId=${channelId}&transferToken=${transferToken}`;
}

