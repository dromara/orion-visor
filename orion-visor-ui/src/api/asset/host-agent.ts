import axios from 'axios';
import qs from 'query-string';

/**
 * 主机探针状态
 */
export interface HostAgentStatusResponse {
  id: number;
  agentVersion: string;
  latestVersion: string;
  agentInstallStatus: number;
  agentOnlineStatus: number;
}

/**
 * 探针日志
 */
export interface HostAgentLogResponse {
  id: number;
  hostId: number;
  agentKey: string;
  type: string;
  status: string;
  message: string;
  createTime: number;
  updateTime: number;
  creator: string;
  updater: string;
  agentStatus: HostAgentStatusResponse;
}

/**
 * 安装探针请求
 */
export interface HostInstallAgentRequest {
  idList?: Array<number>;
}

/**
 * 主机安装探针更新状态请求
 */
export interface HostAgentInstallStatusUpdateRequest {
  id?: number;
  status?: string;
  message?: string;
}

/**
 * 安装主机探针
 */
export function installHostAgent(request: HostInstallAgentRequest) {
  return axios.post('/asset/host-agent/install', request);
}

/**
 * 修改探针安装状态
 */
export function updateAgentInstallStatus(request: HostAgentInstallStatusUpdateRequest) {
  return axios.put('/asset/host-agent/update-install-status', request);
}

/**
 * 查询主机探针状态
 */
export function getHostAgentStatus(idList: Array<number>) {
  return axios.get<Array<HostAgentStatusResponse>>('/asset/host-agent/status', {
    params: { idList },
    promptBizErrorMessage: false,
    promptRequestErrorMessage: false,
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 查询探针安装状态
 */
export function getAgentInstallLogStatus(idList: Array<number>) {
  return axios.get<Array<HostAgentLogResponse>>('/asset/host-agent/install-status', {
    params: { idList },
    promptBizErrorMessage: false,
    promptRequestErrorMessage: false,
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'comma' });
    }
  });
}

/**
 * 上传探针发布包
 */
export function uploadAgentRelease(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return axios.post<string>('/asset/host-agent/upload-agent-release', formData, {
    timeout: 120000,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
  });
}
