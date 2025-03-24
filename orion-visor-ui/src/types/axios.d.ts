import type { AxiosRequestConfig } from 'axios';

declare module 'axios' {
  export interface AxiosRequestConfig<D = any> extends AxiosRequestConfig<D> {
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
