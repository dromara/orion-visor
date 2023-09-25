export type Theme = 'light' | 'dark'
export type Device = 'desktop' | 'mobile'
export type ViewType = 'table' | 'card'

/**
 * 应用状态
 */
export interface AppState extends AppSetting, UserPreferenceLayout, UserPreferenceViews {
  [key: string]: unknown;
}

/**
 * 应用内配置
 */
export interface AppSetting {
  device: Device;
  menuCollapse: boolean;
  hideMenu: boolean;
}

/**
 * 用户偏好 - 布局
 */
export interface UserPreferenceLayout {
  theme: Theme;
  menu: boolean;
  topMenu: boolean;
  navbar: boolean;
  footer: boolean;
  tabBar: boolean;
  menuWidth: number;
  colorWeak: boolean;
}

/**
 * 用户偏好 - 页面视图
 */
export interface UserPreferenceViews {
  host: ViewType | string;
  hostKeys: ViewType | string;
  hostIdentity: ViewType | string;
}
