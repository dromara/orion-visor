/**
 * 菜单类型
 */
export const MenuTypeEnum = {
  PARENT_MENU: {
    value: 1,
    label: '父菜单'
  },
  SUB_MENU: {
    value: 2,
    label: '子菜单'
  },
  FUNCTION: {
    value: 3,
    label: '功能'
  }
};

/**
 * 菜单状态
 */
export const MenuStatusEnum = {
  DISABLED: {
    value: 0,
    label: '停用',
    color: 'orange'
  },
  ENABLED: {
    value: 1,
    label: '启用',
    color: 'blue'
  }
};

/**
 * 菜单是否可见
 */
export const MenuVisibleEnum = {
  HIDE: {
    value: 0,
    label: '隐藏',
    color: 'orange'
  },
  SHOW: {
    value: 1,
    label: '显示',
    color: 'blue'
  }
};

/**
 * 菜单缓存状态
 */
export const MenuCacheEnum = {
  HIDE: {
    value: 0,
    label: '不缓存',
  },
  SHOW: {
    value: 1,
    label: '缓存',
  }
};
