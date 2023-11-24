type Theme = 'light' | 'dark';
type Device = 'desktop' | 'mobile';
type ViewType = 'table' | 'card' | undefined;

/**
 * 应用状态
 */
export interface AppState extends AppSetting, UserPreferenceLayout, UserPreferenceData, UserPreferenceViews {
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
 * 用户偏好 - 数据设置
 */
export interface UserPreferenceData {
  defaultPageSize: number;
  defaultCardSize: number;
}

/**
 * 用户偏好 - 页面视图
 */
export interface UserPreferenceViews {
  hostView: ViewType;
  hostKeyView: ViewType;
  hostIdentityView: ViewType;
}
