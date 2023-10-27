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

// 菜单是否可见 值
export const MenuVisible = {
  // 隐藏
  HIDE: 0,
  // 显示
  SHOW: 1
};

// 菜单缓存状态 值
export const MenuCache = {
  // 禁用
  DISABLED: 0,
  // 启用
  ENABLED: 1
};

// 菜单类型 字典项
export const menuTypeKey = 'systemMenuType';
// 菜单状态 字典项
export const menuStatusKey = 'systemMenuStatus';
// 是否可见 字典项
export const menuVisibleKey = 'systemMenuVisible';
// 是否缓存 字典项
export const menuCacheKey = 'systemMenuCache';

// 加载的字典值
export const dictKeys = [menuTypeKey, menuStatusKey, menuVisibleKey, menuCacheKey];
