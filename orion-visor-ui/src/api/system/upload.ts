import { createAppWebSocket } from '@/utils/http';

/**
 * 打开文件上传 websocket
 */
export const openFileUploadChannel = (uploadToken: string) => {
  return createAppWebSocket(`/file/upload/${uploadToken}`);
};
