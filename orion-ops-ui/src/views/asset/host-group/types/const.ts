// 导航 key
export const tabItemKeys = {
  SETTING: 1,
  ROLE_GRANT: 2,
  USER_GRANT: 3
};

// 导航路径
export const tabItems = [{
  key: tabItemKeys.SETTING,
  text: '分组配置',
  icon: 'icon-unordered-list',
  permission: []
}, {
  key: tabItemKeys.ROLE_GRANT,
  text: '角色授权',
  icon: 'icon-safe',
  permission: []
}, {
  key: tabItemKeys.USER_GRANT,
  text: '用户授权',
  icon: 'icon-user',
  permission: []
}];
