import { webSocketBaseUrl } from '@/utils/env';

/**
 * 创建应用 websocket
 */
export const createAppWebSocket = (url: string): Promise<WebSocket> => {
  return createWebSocket(webSocketBaseUrl + url);
};

/**
 * 创建 websocket
 */
export const createWebSocket = (url: string): Promise<WebSocket> => {
  return new Promise<WebSocket>((resolve, reject) => {
    const socket = new WebSocket(url);

    socket.onopen = () => {
      resolve(socket);
    };

    socket.onerror = e => {
      reject(e);
    };
  });
};
