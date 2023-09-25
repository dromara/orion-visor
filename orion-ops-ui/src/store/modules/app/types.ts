export interface AppState {
  theme: string;
  colorWeak: boolean;
  navbar: boolean;
  menu: boolean;
  topMenu: boolean;
  hideMenu: boolean;
  menuCollapse: boolean;
  footer: boolean;
  menuWidth: number;
  globalSettings: boolean;
  device: string;
  tabBar: boolean;

  [key: string]: unknown;
}
