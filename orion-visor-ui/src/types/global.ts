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

export interface HttpResponse<T = unknown> {
  msg: string;
  code: number;
  data: T;
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

export interface FavoriteItem {
  favorite: boolean;
}

export interface OrderDirection {
  order?: number;
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

export interface StatisticsRangeRequest {
  range: string;
  startTime: number;
}

export interface LineSingleChartData {
  x: string[];
  data: Array<number>;
}

export interface LineChartData {
  x: string[];
  data: Record<string, Array<number>>;
}

export interface PieChartData {
  data: Record<string, number>;
}

export interface BarSingleChartData {
  data: Record<string, number>;
}
