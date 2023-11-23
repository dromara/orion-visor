/**
 * tab 元素
 */
export interface TabRouterItem {
  key: string | number;
  text: string;
  icon?: string;
  permission?: string[];

  [key: string]: unknown;
}
