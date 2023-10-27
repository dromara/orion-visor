-- 默认管理员账号 
-- 账号: admin 密码: admin
INSERT INTO `system_user` VALUES (1, 'admin', 'c3284d0f94606de1fd2af172aba15bf3', '管理员呀', 'http', '122', '122', 1, '2023-08-17 14:23:59', '2023-07-13 22:11:57', '2023-08-17 14:23:59', NULL, NULL, 0);

-- 角色配置
INSERT INTO `system_role` VALUES (1, '管理员', 'admin', 1, '2023-07-16 21:13:14', '2023-07-17 17:31:29', '1', '1', 0);

-- 角色用户关联
INSERT INTO `system_user_role` VALUES (1, 1, 1, '2023-07-16 21:15:49', '2023-07-17 18:18:02', '1', '1', 0);

-- 菜单配置
INSERT INTO `system_menu` VALUES (1, 0, '工作台', NULL, 1, 10, 1, 1, 1, 'icon-storage', NULL, 'workplace', '2023-07-28 10:51:50', '2023-08-09 14:35:03', NULL, '1', 0);
INSERT INTO `system_menu` VALUES (5, 0, '用户设置', NULL, 1, 300, 1, 1, 1, 'icon-user', NULL, NULL, '2023-07-28 10:55:38', '2023-08-09 15:18:29', NULL, '1', 0);
INSERT INTO `system_menu` VALUES (8, 0, '项目地址 github', NULL, 1, 1000, 1, 1, 1, 'icon-github', 'https://github.com/lijiahangmax/orion-ops-pro', '', '2023-07-28 11:04:59', '2023-08-07 17:33:36', NULL, NULL, 0);
INSERT INTO `system_menu` VALUES (10, 5, '角色管理', NULL, 2, 10, 1, 1, 1, 'IconUserGroup', '', 'userRole', '2023-07-28 10:55:52', '2023-08-11 16:47:48', NULL, '1', 0);
INSERT INTO `system_menu` VALUES (11, 0, '项目地址 gitee', NULL, 1, 1010, 1, 1, 1, 'icon-gitlab', 'https://gitee.com/lijiahangmax/orion-ops-pro', '', '2023-08-02 18:08:07', '2023-08-11 18:11:34', NULL, '1', 0);
INSERT INTO `system_menu` VALUES (12, 0, '系统设置', NULL, 1, 310, 1, 1, 1, 'icon-tool', NULL, NULL, '2023-08-02 18:24:24', '2023-08-07 17:33:37', NULL, NULL, 0);
INSERT INTO `system_menu` VALUES (13, 12, '菜单配置', '', 2, 10, 1, 1, 1, 'icon-menu', NULL, 'systemMenu', '2023-08-02 18:29:01', '2023-08-09 15:42:50', NULL, '1', 0);
INSERT INTO `system_menu` VALUES (20, 10, '创建角色', 'infra:system-role:create', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:36:54', '2023-08-15 16:38:42', '1', '1', 0);
INSERT INTO `system_menu` VALUES (21, 10, '修改角色', 'infra:system-role:update', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:37:33', '2023-08-15 16:42:08', '1', '1', 0);
INSERT INTO `system_menu` VALUES (22, 10, '更新状态', 'infra:system-role:update-status', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:37:58', '2023-08-15 16:37:58', '1', '1', 0);
INSERT INTO `system_menu` VALUES (23, 10, '查询角色', 'infra:system-role:query', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:38:26', '2023-08-16 16:58:22', '1', '1', 0);
INSERT INTO `system_menu` VALUES (24, 10, '分配菜单', 'infra:system-role:grant-menu', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:39:41', '2023-08-15 16:39:41', '1', '1', 0);
INSERT INTO `system_menu` VALUES (25, 10, '删除角色', 'infra:system-role:delete', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:40:45', '2023-08-15 16:40:45', '1', '1', 0);
INSERT INTO `system_menu` VALUES (26, 13, '创建菜单', 'infra:system-menu:create', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:41:30', '2023-08-15 16:41:30', '1', '1', 0);
INSERT INTO `system_menu` VALUES (27, 13, '修改菜单', 'infra:system-menu:update', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:41:55', '2023-08-15 16:41:55', '1', '1', 0);
INSERT INTO `system_menu` VALUES (28, 13, '修改状态', 'infra:system-menu:update-status', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:42:41', '2023-08-15 16:42:41', '1', '1', 0);
INSERT INTO `system_menu` VALUES (29, 13, '查询菜单', 'infra:system-menu:query', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:42:57', '2023-08-15 16:42:57', '1', '1', 0);
INSERT INTO `system_menu` VALUES (30, 13, '删除菜单', 'infra:system-menu:delete', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-15 16:43:16', '2023-08-15 16:43:16', '1', '1', 0);
INSERT INTO `system_menu` VALUES (48, 5, '用户管理', NULL, 2, 10, 1, 1, 1, 'IconUserAdd', NULL, 'userUser', '2023-08-16 10:19:24', '2023-08-16 10:42:15', NULL, '1', 0);
INSERT INTO `system_menu` VALUES (49, 48, '创建用户', 'infra:system-user:create', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-16 10:19:24', '2023-08-16 10:19:24', NULL, NULL, 0);
INSERT INTO `system_menu` VALUES (50, 48, '修改用户', 'infra:system-user:update', 3, 20, 1, 1, 1, NULL, NULL, NULL, '2023-08-16 10:19:24', '2023-08-16 10:19:24', NULL, NULL, 0);
INSERT INTO `system_menu` VALUES (51, 48, '查询用户', 'infra:system-user:query', 3, 30, 1, 1, 1, NULL, NULL, NULL, '2023-08-16 10:19:24', '2023-08-16 10:19:24', NULL, NULL, 0);
INSERT INTO `system_menu` VALUES (52, 48, '删除用户', 'infra:system-user:delete', 3, 40, 1, 1, 1, NULL, NULL, NULL, '2023-08-16 10:19:24', '2023-08-16 10:19:24', NULL, NULL, 0);
INSERT INTO `system_menu` VALUES (53, 13, '初始化缓存', 'infra:system-menu:refresh-cache', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-16 10:29:10', '2023-08-16 10:29:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (60, 48, '修改用户状态', 'infra:system-user:update-status', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-16 11:49:04', '2023-08-16 11:49:04', '1', '1', 0);
INSERT INTO `system_menu` VALUES (61, 48, '分配用户角色', 'infra:system-user:grant-role', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-16 11:49:23', '2023-08-16 11:49:23', '1', '1', 0);
INSERT INTO `system_menu` VALUES (62, 48, '重置用户密码', 'infra:system-user:reset-password', 3, 10, 1, 1, 1, NULL, NULL, NULL, '2023-08-16 11:49:50', '2023-08-16 11:49:50', '1', '1', 0);

