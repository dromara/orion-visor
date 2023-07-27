// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { AxiosRequestConfig } from 'axios';

declare module 'axios' {
  // eslint-disable-next-line no-shadow
  export interface AxiosRequestConfig {
    // 是否使用原始返回
    unwrap?: boolean;
    // 是否提示业务错误信息
    promptBizErrorMessage?: boolean;
  }
}
