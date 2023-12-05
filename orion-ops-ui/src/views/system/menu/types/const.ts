// 自增排序步长
export const sortStep = 10;

// 菜单类型 值
export const MenuType = {
  // 父菜单
  PARENT_MENU: 1,
  // 子菜单
  SUB_MENU: 2,
  // 功能
  FUNCTION: 3
};

// 菜单类型 字典项
export const menuTypeKey = 'systemMenuType';
// 菜单状态 字典项
export const menuStatusKey = 'systemMenuStatus';
// 是否可见 字典项
export const menuVisibleKey = 'systemMenuVisible';
// 是否缓存 字典项
export const menuCacheKey = 'systemMenuCache';
// 是否新窗口打开 字典项
export const menuNewWindowKey = 'systemMenuNewWindow';

// 加载的字典值
export const dictKeys = [menuTypeKey, menuStatusKey, menuVisibleKey, menuCacheKey, menuNewWindowKey];
