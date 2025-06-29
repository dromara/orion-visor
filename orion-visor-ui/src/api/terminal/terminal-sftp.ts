import { httpBaseUrl } from '@/utils/env';
import axios from 'axios';

/**
 * 获取 SFTP 文件内容
 */
export function getSftpFileContent(token: string) {
  return axios.get<string>('/terminal/terminal-sftp/get-content', {
    unwrap: true,
    params: { token },
    timeout: 60000
  });
}

/**
 * 设置 SFTP 文件内容
 */
export function setSftpFileContent(token: string, content: string) {
  const formData = new FormData();
  formData.append('token', token);
  formData.append('file', new File([content], Date.now() + '', { type: 'text/plain' }));
  return axios.post<boolean>('/terminal/terminal-sftp/set-content', formData, {
    timeout: 60000,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/**
 * 下载文件
 */
export function getDownloadTransferUrl(channelId: string, transferToken: string) {
  return `${httpBaseUrl}/terminal/terminal-sftp/download?channelId=${channelId}&transferToken=${transferToken}`;
}
