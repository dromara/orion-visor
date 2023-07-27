import axios from 'axios';
import type { AxiosRequestConfig, AxiosResponse } from 'axios';
import { Message, Notification } from '@arco-design/web-vue';
import { useUserStore } from '@/store';
import { getToken } from '@/utils/auth';

export interface HttpResponse<T = unknown> {
  msg: string;
  code: number;
  data: T;
}

axios.defaults.timeout = 10000;
axios.defaults.promptBizErrorMessage = true;
if (import.meta.env.VITE_API_BASE_URL) {
  axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL;
}

axios.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 获取 token
    const token = getToken();
    if (token) {
      if (!config.headers) {
        config.headers = {};
      }
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  (response: AxiosResponse<HttpResponse>) => {
    // 不转换
    if (response.config.unwrap) {
      return response;
    }
    const res = response.data;
    const { code } = res;
    // 200 成功
    if (code === 200) {
      return res;
    }
    // 非 200 业务异常
    if (response.config.promptBizErrorMessage) {
      Message.error({
        content: res.msg || 'Error',
        duration: 5 * 1000,
      });
    }
    // 业务判断
    if (
      [401, 700, 701, 702].includes(code) &&
      response.config.url !== '/infra/auth/login'
    ) {
      Notification.error({
        closable: true,
        content: res.msg,
      });
      setTimeout(async () => {
        // 登出
        await useUserStore().logout();
        window.location.reload();
      });
    }
    return Promise.reject(new Error(res.msg || 'Error'));
  },
  (error) => {
    Message.error({
      content: error.msg || '请求失败',
      duration: 5 * 1000,
    });
    return Promise.reject(error);
  }
);
