// eslint-disable-next-line @typescript-eslint/no-unused-vars
import type { AxiosRequestConfig } from 'axios';

declare module 'axios' {
  // eslint-disable-next-line no-shadow
  export interface AxiosRequestConfig {
    // 是否添加 Authorization
    setAuthorization?: boolean;
    // 是否使用原始返回
    unwrap?: boolean;
    // 是否提示业务错误信息
    promptBizErrorMessage?: boolean;
    // 是否提示请求错误信息
    promptRequestErrorMessage?: boolean;
  }
}
