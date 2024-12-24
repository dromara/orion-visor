export interface AnyObject {
  [key: string]: unknown;
}

export interface Options {
  label: string;
  value: string | number | boolean;

  [key: string]: unknown;
}

export interface NodeOptions extends Options {
  children?: NodeOptions[];
}

export interface GetParams {
  body: null;
  type: string;
  url: string;
}

export interface PostData {
  body: string;
  type: string;
  url: string;
}

export interface Pagination {
  page?: number;
  limit?: number;
}

export interface ClearRequest {
  limit?: number;
}

export interface NodeData {
  [key: string]: any;
}

export interface DataGrid<T> {
  page: number;
  limit: number;
  total: number;
  rows: T[];
}

export type TimeRanger = [string, string];
