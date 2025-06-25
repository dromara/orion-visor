const debug = import.meta.env.MODE !== 'production';

// 当前环境是否为安全环境
export const isSecureEnvironment = (() => {
  return window.location.protocol === 'https:' || window.location.hostname === 'localhost';
})();

// 当前是否为单应用模式 PWA
export const isStandaloneMode = (() => (
  (window.matchMedia('(display-mode: standalone)').matches)
  || ((window.navigator as any).standalone)
  || document.referrer.includes('android-app://')
) === true)();

// http base url
export const httpBaseUrl = (() => {
  const configBase = import.meta.env.VITE_API_BASE_URL || '';
  if (configBase.startsWith('http')) {
    // 固定
    return configBase;
  } else {
    // 动态
    const protocol = window.location.protocol;
    const host = window.location.host;
    return `${protocol}//${host}${configBase}`;
  }
})();

// websocket base url
export const webSocketBaseUrl = (() => {
  const configBase = import.meta.env.VITE_WS_BASE_URL || '';
  if (configBase.startsWith('ws')) {
    // 固定
    return configBase;
  } else {
    // 动态
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.host;
    return `${protocol}//${host}${configBase}`;
  }
})();

export default debug;
