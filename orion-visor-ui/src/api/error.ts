import type { AxiosResponse } from 'axios';

// api 错误
export class ApiError extends Error {
  data: AxiosResponse;

  constructor(message: string, data: AxiosResponse) {
    super(message);
    this.name = 'ApiError';
    this.data = data;
  }
}
