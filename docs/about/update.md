⚡ 注意: 应用不支持跨版本升级, 可以进行多次升级

## v1.0.4

> sql 脚本

```sql
ALTER TABLE `exec_template` 
CHANGE COLUMN `parameter` `parameter_schema` json NULL COMMENT '参数定义' AFTER `timeout`;

ALTER TABLE `exec_log` 
ADD COLUMN `exec_seq` int(0) NULL DEFAULT 0 COMMENT '执行序列' AFTER `description`;
```

## v1.0.3

> sql 脚本

```sql
DELETE FROM preference WHERE type = 'TERMINAL';
```

## v1.0.2

> sql 脚本

```sql
-- 表结构
ALTER TABLE `host_connect_log` 
MODIFY COLUMN `start_time` datetime(3) NULL DEFAULT NULL COMMENT '开始时间' AFTER `token`,
MODIFY COLUMN `end_time` datetime(3) NULL DEFAULT NULL COMMENT '结束时间' AFTER `start_time`;

CREATE TABLE `exec_host_log`
(
    `id`            bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `log_id`        bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行日志id',
    `host_id`       bigint(0)                                                     NULL DEFAULT NULL COMMENT '主机id',
    `host_name`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机名称',
    `host_address`  varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机地址',
    `status`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT NULL COMMENT '执行状态',
    `command`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '执行命令',
    `parameter`     json                                                          NULL COMMENT '执行参数',
    `exit_status`   int(0)                                                        NULL DEFAULT NULL COMMENT '退出码',
    `log_path`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志路径',
    `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
    `start_time`    datetime(3)                                                   NULL DEFAULT NULL COMMENT '执行开始时间',
    `finish_time`   datetime(3)                                                   NULL DEFAULT NULL COMMENT '执行结束时间',
    `create_time`   datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`       tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_log_id` (`log_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '批量执行主机日志'
  ROW_FORMAT = Dynamic;

CREATE TABLE `exec_log`
(
    `id`               bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`          bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行用户id',
    `username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '执行用户名',
    `source`           char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT NULL COMMENT '执行来源',
    `source_id`        bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行来源id',
    `description`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行描述',
    `command`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '执行命令',
    `parameter_schema` json                                                          NULL COMMENT '参数 schema',
    `timeout`          int(0)                                                        NULL DEFAULT NULL COMMENT '超时时间',
    `status`           char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT NULL COMMENT '执行状态',
    `start_time`       datetime(3)                                                   NULL DEFAULT NULL COMMENT '执行开始时间',
    `finish_time`      datetime(3)                                                   NULL DEFAULT NULL COMMENT '执行完成时间',
    `create_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`          tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_source` (`source`, `source_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '批量执行日志'
  ROW_FORMAT = Dynamic;

CREATE TABLE `exec_template`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `command`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL COMMENT '命令',
    `timeout`     int(0)                                                       NULL DEFAULT 0 COMMENT '超时时间秒 0不超时',
    `parameter`   json                                                         NULL COMMENT '参数',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '执行模板'
  ROW_FORMAT = Dynamic;

-- 菜单配置
TRUNCATE `system_menu`;
INSERT INTO `system_menu` VALUES (1, 0, '工作台', NULL, 1, 10, 1, 1, 1, 0, 'IconComputer', NULL, 'workplace', '2023-07-28 10:51:50', '2023-09-11 15:27:52', '1', '1', 0);
INSERT INTO `system_menu` VALUES (5, 0, '用户设置', NULL, 1, 700, 1, 1, 1, 0, 'icon-user', NULL, '', '2023-07-28 10:55:38', '2024-03-07 19:03:52', '1', '1', 0);
INSERT INTO `system_menu` VALUES (8, 0, '项目地址 github', NULL, 1, 1000, 1, 1, 1, 0, 'icon-github', 'https://github.com/lijiahangmax/orion-ops-pro', '', '2023-07-28 11:04:59', '2023-10-12 15:21:22', '1', '1', 0);
INSERT INTO `system_menu` VALUES (10, 5, '角色管理', NULL, 2, 10, 1, 1, 1, 0, 'IconUserGroup', '', 'role', '2023-07-28 10:55:52', '2024-03-07 19:10:13', '1', '1', 0);
INSERT INTO `system_menu` VALUES (11, 0, '项目地址 gitee', NULL, 1, 1010, 1, 1, 1, 0, 'icon-gitlab', 'https://gitee.com/lijiahangmax/orion-ops-pro', '', '2023-08-02 18:08:07', '2023-08-11 18:11:34', '1', '1', 0);
INSERT INTO `system_menu` VALUES (12, 0, '系统设置', NULL, 1, 800, 1, 1, 1, 0, 'icon-tool', NULL, '', '2023-08-02 18:24:24', '2024-03-07 19:03:57', '1', '1', 0);
INSERT INTO `system_menu` VALUES (13, 12, '系统菜单', '', 2, 10, 1, 1, 1, 0, 'icon-menu', NULL, 'systemMenu', '2023-08-02 18:29:01', '2024-03-07 22:25:00', '1', '1', 0);
INSERT INTO `system_menu` VALUES (20, 10, '创建角色', 'infra:system-role:create', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:36:54', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (21, 10, '修改角色', 'infra:system-role:update', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:37:33', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (22, 10, '更新状态', 'infra:system-role:update-status', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:37:58', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (23, 10, '查询角色', 'infra:system-role:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:38:26', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (24, 10, '分配菜单', 'infra:system-role:grant-menu', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:39:41', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (25, 10, '删除角色', 'infra:system-role:delete', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:40:45', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (26, 13, '创建菜单', 'infra:system-menu:create', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:41:30', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (27, 13, '修改菜单', 'infra:system-menu:update', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:41:55', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (28, 13, '修改状态', 'infra:system-menu:update-status', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:42:41', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (29, 13, '查询菜单', 'infra:system-menu:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:42:57', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (30, 13, '删除菜单', 'infra:system-menu:delete', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-15 16:43:16', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (48, 5, '用户管理', NULL, 2, 10, 1, 1, 1, 0, 'IconUserAdd', NULL, 'user', '2023-08-16 10:19:24', '2024-03-07 19:10:21', '1', '1', 0);
INSERT INTO `system_menu` VALUES (49, 48, '创建用户', 'infra:system-user:create', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-16 10:19:24', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (50, 48, '修改用户', 'infra:system-user:update', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-16 10:19:24', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (51, 48, '查询用户', 'infra:system-user:query', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-16 10:19:24', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (52, 48, '删除用户', 'infra:system-user:delete', 3, 40, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-16 10:19:24', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (53, 13, '刷新缓存', 'infra:system-menu:management:refresh-cache', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-16 10:29:10', '2023-12-27 12:39:48', '1', '1', 0);
INSERT INTO `system_menu` VALUES (60, 48, '修改用户状态', 'infra:system-user:update-status', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-16 11:49:04', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (61, 48, '分配用户角色', 'infra:system-user:grant-role', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-16 11:49:23', '2023-10-27 01:20:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (62, 48, '重置用户密码', 'infra:system-user:management:reset-password', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-08-16 11:49:50', '2023-12-27 12:42:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (63, 0, '资产管理', NULL, 1, 300, 1, 1, 1, 0, 'IconStorage', NULL, '', '2023-09-11 14:17:31', '2024-03-07 19:03:39', '1', '1', 0);
INSERT INTO `system_menu` VALUES (64, 63, '主机管理', NULL, 2, 40, 1, 1, 1, 0, 'IconDesktop', NULL, 'hostList', '2023-09-11 14:17:31', '2024-03-07 19:09:20', '1', '1', 0);
INSERT INTO `system_menu` VALUES (65, 64, '查询主机', 'asset:host:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-11 14:17:31', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (66, 64, '创建主机', 'asset:host:create', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-11 14:17:31', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (67, 64, '修改主机', 'asset:host:update', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-11 14:17:31', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (68, 64, '删除主机', 'asset:host:delete', 3, 40, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-11 14:17:31', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (70, 64, '修改配置', 'asset:host:update-config', 3, 60, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-14 16:27:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (72, 63, '主机身份', NULL, 2, 60, 1, 1, 1, 0, 'IconIdcard', NULL, 'hostIdentity', '2023-09-20 11:47:18', '2024-03-07 19:09:31', '1', '1', 0);
INSERT INTO `system_menu` VALUES (73, 72, '查询主机身份', 'asset:host-identity:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (74, 72, '创建主机身份', 'asset:host-identity:create', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (75, 72, '修改主机身份', 'asset:host-identity:update', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (76, 72, '删除主机身份', 'asset:host-identity:delete', 3, 40, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (79, 63, '主机秘钥', NULL, 2, 50, 1, 1, 1, 0, 'IconLock', NULL, 'hostKey', '2023-09-20 11:47:18', '2024-03-07 19:09:26', '1', '1', 0);
INSERT INTO `system_menu` VALUES (80, 79, '查询主机秘钥', 'asset:host-key:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (81, 79, '创建主机秘钥', 'asset:host-key:create', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (82, 79, '修改主机秘钥', 'asset:host-key:update', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (83, 79, '删除主机秘钥', 'asset:host-key:delete', 3, 40, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-10-27 01:15:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (84, 79, '查询主机秘钥详情', 'asset:host-key:query-detail', 3, 50, 1, 1, 1, 0, NULL, NULL, NULL, '2023-09-20 11:47:18', '2023-11-09 15:52:57', '1', '1', 0);
INSERT INTO `system_menu` VALUES (94, 5, '个人中心', NULL, 2, 20, 0, 1, 0, 0, 'IconUser', NULL, 'userInfo', '2023-10-08 18:53:01', '2023-11-02 11:47:34', '1', '1', 0);
INSERT INTO `system_menu` VALUES (97, 12, '数据字典项', NULL, 2, 20, 1, 1, 1, 0, 'IconBook', NULL, 'dictKey', '2023-10-17 11:38:13', '2024-03-07 19:10:45', '1', '1', 0);
INSERT INTO `system_menu` VALUES (99, 97, '创建字典配置项', 'infra:dict-key:create', 3, 110, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-17 11:38:13', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (100, 97, '修改字典配置项', 'infra:dict-key:update', 3, 120, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-17 11:38:13', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (101, 97, '删除字典配置项', 'infra:dict-key:delete', 3, 130, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-17 11:38:13', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (105, 12, '数据字典值', NULL, 2, 30, 1, 1, 1, 0, 'IconNav', NULL, 'dictValue', '2023-10-17 11:38:13', '2024-03-07 19:10:49', '1', '1', 0);
INSERT INTO `system_menu` VALUES (106, 105, '查询字典配置值', 'infra:dict-value:query', 3, 210, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-17 11:38:18', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (107, 105, '创建字典配置值', 'infra:dict-value:create', 3, 220, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-17 11:38:18', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (108, 105, '修改字典配置值', 'infra:dict-value:update', 3, 230, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-17 11:38:18', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (109, 105, '删除字典配置值', 'infra:dict-value:delete', 3, 240, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-17 11:38:18', '2023-10-27 01:16:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (120, 97, '查询字典配置项', 'infra:dict-key:query', 3, 100, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-20 11:27:12', '2023-12-27 18:39:22', '1', '1', 0);
INSERT INTO `system_menu` VALUES (121, 97, '刷新缓存', 'infra:dict-key:management:refresh-cache', 3, 140, 1, 1, 1, 0, NULL, NULL, NULL, '2023-10-27 15:50:04', '2023-12-27 12:40:12', '1', '1', 0);
INSERT INTO `system_menu` VALUES (122, 5, '操作日志', NULL, 2, 30, 1, 1, 1, 0, 'IconCalendarClock', NULL, 'operatorLog', '2023-11-01 14:09:36', '2024-03-07 19:10:30', '1', '1', 0);
INSERT INTO `system_menu` VALUES (123, 122, '查询操作日志', 'infra:operator-log:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-11-02 11:22:54', '2023-11-02 11:22:54', '1', '1', 0);
INSERT INTO `system_menu` VALUES (124, 48, '查询用户会话', 'infra:system-user:query-session', 3, 50, 1, 1, 1, 0, NULL, NULL, NULL, '2023-11-02 11:24:14', '2023-11-02 11:24:14', '1', '1', 0);
INSERT INTO `system_menu` VALUES (125, 48, '下线用户会话', 'infra:system-user:management:offline-session', 3, 60, 1, 1, 1, 0, NULL, NULL, NULL, '2023-11-02 11:24:37', '2023-12-27 12:39:17', '1', '1', 0);
INSERT INTO `system_menu` VALUES (126, 48, '查询用户登录日志', 'infra:system-user:login-history', 3, 70, 1, 1, 1, 0, NULL, NULL, NULL, '2023-12-27 15:05:37', '2023-12-27 15:07:19', '1', '1', 0);
INSERT INTO `system_menu` VALUES (129, 64, '编辑主机分组', 'asset:host-group:update', 3, 100, 1, 1, 1, 0, NULL, NULL, NULL, '2023-11-13 18:16:32', '2023-12-01 01:47:58', '1', '1', 0);
INSERT INTO `system_menu` VALUES (133, 144, '主机分组授权', 'asset:host-group:grant', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-11-23 18:08:57', '2023-11-30 22:39:53', '1', '1', 0);
INSERT INTO `system_menu` VALUES (142, 144, '主机秘钥授权', 'asset:host-key:grant', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2023-11-30 21:06:13', '2023-11-30 22:39:47', '1', '1', 0);
INSERT INTO `system_menu` VALUES (143, 144, '主机身份授权', 'asset:host-identity:grant', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2023-11-30 21:06:26', '2023-11-30 22:40:11', '1', '1', 0);
INSERT INTO `system_menu` VALUES (144, 63, '资产授权', NULL, 2, 70, 1, 1, 1, 0, 'icon-safe', NULL, 'assetGrant', '2023-11-30 22:38:57', '2023-11-30 22:39:06', '1', '1', 0);
INSERT INTO `system_menu` VALUES (145, 0, '主机运维', NULL, 1, 400, 1, 1, 1, 1, 'IconDesktop', NULL, '', '2023-12-04 23:33:25', '2024-03-07 19:03:46', '1', '1', 0);
INSERT INTO `system_menu` VALUES (146, 145, '主机终端', NULL, 2, 10, 1, 1, 1, 1, 'icon-code-square', NULL, 'terminal', '2023-12-04 23:38:01', '2024-03-07 19:09:44', '1', '1', 0);
INSERT INTO `system_menu` VALUES (148, 152, '连接日志', NULL, 2, 10, 1, 1, 1, 0, 'IconLink', NULL, 'connectLog', '2023-12-26 22:53:07', '2024-03-07 19:09:59', '1', '1', 0);
INSERT INTO `system_menu` VALUES (149, 148, '查询连接日志', 'asset:host-connect-log:management:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-12-26 22:53:08', '2024-03-04 13:40:42', '1', '1', 0);
INSERT INTO `system_menu` VALUES (151, 146, '连接终端', 'asset:host-terminal:access', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-12-27 18:56:33', '2023-12-27 18:56:33', '2', '2', 0);
INSERT INTO `system_menu` VALUES (152, 0, '运维审计', NULL, 1, 410, 1, 1, 1, 0, 'IconSafe', NULL, '', '2024-01-04 17:54:56', '2024-03-07 19:02:28', '1', '1', 0);
INSERT INTO `system_menu` VALUES (153, 148, '删除连接日志', 'asset:host-connect-log:management:delete', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 13:39:46', '2024-03-04 13:40:29', '1', '1', 0);
INSERT INTO `system_menu` VALUES (154, 148, '清空连接日志', 'asset:host-connect-log:management:clear', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 13:40:05', '2024-03-04 13:40:34', '1', '1', 0);
INSERT INTO `system_menu` VALUES (155, 148, '强制断开连接', 'asset:host-connect-log:management:force-offline', 3, 40, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 13:41:02', '2024-03-05 23:32:01', '1', '1', 0);
INSERT INTO `system_menu` VALUES (156, 122, '删除操作日志', 'infra:operator-log:delete', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 17:06:55', '2024-03-04 17:08:22', '1', '1', 0);
INSERT INTO `system_menu` VALUES (157, 122, '清空操作日志', 'infra:operator-log:clear', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 17:07:25', '2024-03-04 17:08:27', '1', '1', 0);
INSERT INTO `system_menu` VALUES (158, 152, 'SFTP 操作日志', NULL, 2, 20, 1, 1, 1, 0, 'IconFile', NULL, 'sftpLog', '2024-03-05 15:30:13', '2024-03-07 19:10:05', '1', '1', 0);
INSERT INTO `system_menu` VALUES (159, 158, '查询 SFTP 操作日志', 'asset:host-sftp-log:management:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-05 15:31:02', '2024-03-05 15:57:20', '1', '1', 0);
INSERT INTO `system_menu` VALUES (160, 158, '删除 SFTP 操作日志', 'asset:host-sftp-log:management:delete', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-05 15:31:17', '2024-03-05 15:57:30', '1', '1', 0);
INSERT INTO `system_menu` VALUES (161, 145, '执行模板', NULL, 2, 50, 1, 1, 1, 0, 'IconBookmark', NULL, 'execTemplate', '2024-03-07 18:32:41', '2024-03-21 14:03:17', '1', '1', 0);
INSERT INTO `system_menu` VALUES (162, 161, '查询执行模板', 'asset:exec-template:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-07 18:32:41', '2024-03-07 18:32:41', '1', '1', 0);
INSERT INTO `system_menu` VALUES (163, 161, '创建执行模板', 'asset:exec-template:create', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-07 18:32:41', '2024-03-07 18:32:41', '1', '1', 0);
INSERT INTO `system_menu` VALUES (164, 161, '修改执行模板', 'asset:exec-template:update', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-07 18:32:41', '2024-03-07 18:32:41', '1', '1', 0);
INSERT INTO `system_menu` VALUES (165, 161, '删除执行模板', 'asset:exec-template:delete', 3, 40, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-07 18:32:41', '2024-03-07 18:32:41', '1', '1', 0);
INSERT INTO `system_menu` VALUES (167, 145, '执行记录', NULL, 2, 30, 1, 1, 1, 0, 'icon-history', NULL, 'execLog', '2024-03-13 15:08:23', '2024-03-13 15:28:29', '1', '1', 0);
INSERT INTO `system_menu` VALUES (168, 167, '查询执行记录', 'asset:exec-log:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-13 15:08:23', '2024-03-13 15:14:32', '1', '1', 0);
INSERT INTO `system_menu` VALUES (169, 167, '删除执行记录', 'asset:exec-log:delete', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-13 15:08:23', '2024-03-13 15:32:09', '1', '1', 0);
INSERT INTO `system_menu` VALUES (170, 167, '清理执行记录', 'asset:exec-log:management:clear', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-13 15:08:23', '2024-03-13 15:30:09', '1', '1', 0);
INSERT INTO `system_menu` VALUES (172, 145, '批量执行', NULL, 2, 20, 1, 1, 1, 0, 'icon-thunderbolt', NULL, 'execCommand', '2024-03-13 15:08:23', '2024-03-13 15:29:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (173, 172, '执行命令', 'asset:exec:exec-command', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-13 15:08:23', '2024-03-13 15:25:41', '1', '1', 0);
INSERT INTO `system_menu` VALUES (174, 172, '中断执行', 'asset:exec:interrupt-exec', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-13 15:25:36', '2024-03-13 15:25:57', '1', '1', 0);
INSERT INTO `system_menu` VALUES (175, 145, '执行日志新页面', NULL, 2, 40, 0, 1, 0, 1, 'IconUnorderedList', NULL, 'execLogView', '2024-03-21 14:03:10', '2024-03-21 14:03:10', '1', '1', 0);

-- 字典配置项
DELETE FROM `dict_key` WHERE `id` >= 34;
INSERT INTO `dict_key` VALUES (34, 'execStatus', 'STRING', '[{\"name\": \"color\", \"type\": \"STRING\"}]', '批量执行状态', '2024-03-13 15:08:43', '2024-03-13 15:39:46', '1', '1', 0);
INSERT INTO `dict_key` VALUES (35, 'execHostStatus', 'STRING', '[{\"name\": \"color\", \"type\": \"COLOR\"}, {\"name\": \"execColor\", \"type\": \"COLOR\"}]', '主机执行状态', '2024-03-13 15:09:10', '2024-03-17 20:31:07', '1', '1', 0);

-- 字典配置值
DELETE FROM `dict_value` WHERE `id` >= 232;
INSERT INTO `dict_value` VALUES (232, 1, 'operatorLogModule', 'asset:exec-template', '执行模板', '{}', 2070, '2024-03-07 18:32:41', '2024-03-07 18:32:41', '1', '1', 0);
INSERT INTO `dict_value` VALUES (233, 2, 'operatorLogType', 'exec-template:create', '创建执行模板', '{}', 110, '2024-03-07 18:32:41', '2024-03-07 18:32:41', '1', '1', 0);
INSERT INTO `dict_value` VALUES (234, 2, 'operatorLogType', 'exec-template:update', '更新执行模板', '{}', 120, '2024-03-07 18:32:41', '2024-03-07 18:32:41', '1', '1', 0);
INSERT INTO `dict_value` VALUES (235, 2, 'operatorLogType', 'exec-template:delete', '删除执行模板', '{}', 130, '2024-03-07 18:32:41', '2024-03-07 18:32:41', '1', '1', 0);
INSERT INTO `dict_value` VALUES (236, 1, 'operatorLogModule', 'asset:exec', '批量执行', '{}', 2080, '2024-03-13 15:08:43', '2024-03-13 15:21:20', '1', '1', 0);
INSERT INTO `dict_value` VALUES (237, 2, 'operatorLogType', 'exec:delete-log', '删除执行记录', '{}', 40, '2024-03-13 15:08:43', '2024-03-18 17:26:43', '1', '1', 0);
INSERT INTO `dict_value` VALUES (238, 2, 'operatorLogType', 'exec:clear-log', '清理执行记录', '{}', 50, '2024-03-13 15:08:43', '2024-03-18 17:26:44', '1', '1', 0);
INSERT INTO `dict_value` VALUES (239, 2, 'operatorLogType', 'exec:delete-host-log', '删除主机执行记录', '{}', 60, '2024-03-13 15:08:43', '2024-03-18 17:26:47', '1', '1', 0);
INSERT INTO `dict_value` VALUES (240, 34, 'execStatus', 'WAITING', '等待中', '{\"color\": \"gray\"}', 10, '2024-03-13 15:08:43', '2024-03-13 15:53:55', '1', '1', 0);
INSERT INTO `dict_value` VALUES (241, 34, 'execStatus', 'RUNNING', '运行中', '{\"color\": \"green\"}', 20, '2024-03-13 15:08:43', '2024-03-13 15:53:55', '1', '1', 0);
INSERT INTO `dict_value` VALUES (242, 34, 'execStatus', 'COMPLETED', '执行完成', '{\"color\": \"arcoblue\"}', 30, '2024-03-13 15:08:43', '2024-03-13 15:53:55', '1', '1', 0);
INSERT INTO `dict_value` VALUES (243, 34, 'execStatus', 'FAILED', '执行失败', '{\"color\": \"red\"}', 40, '2024-03-13 15:08:43', '2024-03-13 15:53:55', '1', '1', 0);
INSERT INTO `dict_value` VALUES (244, 35, 'execHostStatus', 'WAITING', '等待中', '{\"color\": \"gray\", \"execColor\": \"#94D82D\"}', 10, '2024-03-13 15:09:11', '2024-03-19 19:09:53', '1', '1', 0);
INSERT INTO `dict_value` VALUES (245, 35, 'execHostStatus', 'RUNNING', '运行中', '{\"color\": \"green\", \"execColor\": \"#339AF0\"}', 20, '2024-03-13 15:09:11', '2024-03-17 20:50:53', '1', '1', 0);
INSERT INTO `dict_value` VALUES (246, 35, 'execHostStatus', 'COMPLETED', '执行完成', '{\"color\": \"arcoblue\", \"execColor\": \"#5C7CFA\"}', 30, '2024-03-13 15:09:11', '2024-03-17 20:42:17', '1', '1', 0);
INSERT INTO `dict_value` VALUES (247, 35, 'execHostStatus', 'FAILED', '执行失败', '{\"color\": \"red\", \"execColor\": \"#FF6B6B\"}', 40, '2024-03-13 15:09:11', '2024-03-17 20:42:39', '1', '1', 0);
INSERT INTO `dict_value` VALUES (248, 35, 'execHostStatus', 'INTERRUPTED', '已中断', '{\"color\": \"purple\", \"execColor\": \"#845EF7\"}', 60, '2024-03-13 15:09:11', '2024-03-19 19:05:03', '1', '1', 0);
INSERT INTO `dict_value` VALUES (249, 2, 'operatorLogType', 'exec:exec-command', '执行主机命令', '{}', 10, '2024-03-13 15:08:43', '2024-03-18 17:26:31', '1', '1', 0);
INSERT INTO `dict_value` VALUES (250, 2, 'operatorLogType', 'exec:interrupt-exec', '中断执行命令', '{}', 20, '2024-03-13 15:08:43', '2024-03-18 17:26:33', '1', '1', 0);
INSERT INTO `dict_value` VALUES (251, 2, 'operatorLogType', 'exec:interrupt-host', '中断主机命令', '{}', 30, '2024-03-13 15:08:43', '2024-03-18 17:26:34', '1', '1', 0);
INSERT INTO `dict_value` VALUES (252, 2, 'operatorLogType', 'exec:download-host-log', '下载执行日志', '{}', 70, '2024-03-18 17:25:44', '2024-03-18 17:26:49', '1', '1', 0);
INSERT INTO `dict_value` VALUES (253, 35, 'execHostStatus', 'TIMEOUT', '执行超时', '{\"color\": \"orangered\", \"execColor\": \"#fA8C16\"}', 50, '2024-03-19 19:05:56', '2024-03-19 19:08:14', '1', '1', 0);
```

## v1.0.1

> sql 脚本

```sql
DROP TABLE IF EXISTS `command_template`;
ALTER TABLE `operator_log` ADD INDEX `idx_type`(`type`);
-- 菜单配置
DELETE FROM `system_menu` WHERE id IN (148, 149);
INSERT INTO `system_menu` VALUES (148, 152, '连接日志', NULL, 2, 10, 1, 1, 1, 0, 'IconLink', NULL, 'assetAuditConnectLog', '2023-12-26 22:53:07', '2024-03-05 23:31:23', '1', '1', 0);
INSERT INTO `system_menu` VALUES (149, 148, '查询连接日志', 'asset:host-connect-log:management:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2023-12-26 22:53:08', '2024-03-04 13:40:42', '1', '1', 0);
INSERT INTO `system_menu` VALUES (152, 0, '运维审计', NULL, 1, 410, 1, 1, 1, 0, 'IconSafe', NULL, 'assetAudit', '2024-01-04 17:54:56', '2024-03-05 23:31:10', '1', '1', 0);
INSERT INTO `system_menu` VALUES (153, 148, '删除连接日志', 'asset:host-connect-log:management:delete', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 13:39:46', '2024-03-04 13:40:29', '1', '1', 0);
INSERT INTO `system_menu` VALUES (154, 148, '清空连接日志', 'asset:host-connect-log:management:clear', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 13:40:05', '2024-03-04 13:40:34', '1', '1', 0);
INSERT INTO `system_menu` VALUES (155, 148, '强制断开连接', 'asset:host-connect-log:management:force-offline', 3, 40, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 13:41:02', '2024-03-05 23:32:01', '1', '1', 0);
INSERT INTO `system_menu` VALUES (156, 122, '删除操作日志', 'infra:operator-log:delete', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 17:06:55', '2024-03-04 17:08:22', '1', '1', 0);
INSERT INTO `system_menu` VALUES (157, 122, '清空操作日志', 'infra:operator-log:clear', 3, 30, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-04 17:07:25', '2024-03-04 17:08:27', '1', '1', 0);
INSERT INTO `system_menu` VALUES (158, 152, 'SFTP 操作日志', NULL, 2, 20, 1, 1, 1, 0, 'IconFile', NULL, 'assetAuditSftpLog', '2024-03-05 15:30:13', '2024-03-05 23:31:32', '1', '1', 0);
INSERT INTO `system_menu` VALUES (159, 158, '查询 SFTP 操作日志', 'asset:host-sftp-log:management:query', 3, 10, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-05 15:31:02', '2024-03-05 15:57:20', '1', '1', 0);
INSERT INTO `system_menu` VALUES (160, 158, '删除 SFTP 操作日志', 'asset:host-sftp-log:management:delete', 3, 20, 1, 1, 1, 0, NULL, NULL, NULL, '2024-03-05 15:31:17', '2024-03-05 15:57:30', '1', '1', 0);
-- 字典配置项
INSERT INTO `dict_key` VALUES (33, 'sftpOperatorType', 'STRING', '[]', 'SFTP 操作类型', '2024-03-05 16:49:54', '2024-03-05 16:49:54', '1', '1', 0);
-- 字典配置值
INSERT INTO `dict_value` VALUES (214, 28, 'hostConnectStatus', 'FORCE_OFFLINE', '强制下线', '{\"color\": \"rgb(var(--red-6))\"}', 40, '2024-03-04 12:51:13', '2024-03-04 12:51:13', '1', '1', 0);
INSERT INTO `dict_value` VALUES (215, 1, 'operatorLogModule', 'asset:host-connect-log', '主机连接日志', '{}', 2060, '2024-03-04 13:43:33', '2024-03-04 13:43:33', '1', '1', 0);
INSERT INTO `dict_value` VALUES (216, 2, 'operatorLogType', 'host-connect-log:delete', '删除记录', '{}', 10, '2024-03-04 13:44:34', '2024-03-04 13:44:34', '1', '1', 0);
INSERT INTO `dict_value` VALUES (217, 2, 'operatorLogType', 'host-connect-log:clear', '清空记录', '{}', 20, '2024-03-04 13:45:07', '2024-03-04 14:22:08', '1', '1', 0);
INSERT INTO `dict_value` VALUES (218, 2, 'operatorLogType', 'host-connect-log:force-offline', '强制下线', '{}', 30, '2024-03-04 13:45:36', '2024-03-04 13:45:36', '1', '1', 0);
INSERT INTO `dict_value` VALUES (219, 1, 'operatorLogModule', 'infra:operator-log', '操作日志', '{}', 1060, '2024-03-04 16:32:11', '2024-03-04 16:32:11', '1', '1', 0);
INSERT INTO `dict_value` VALUES (220, 2, 'operatorLogType', 'operator-log:delete', '删除操作日志', '{}', 10, '2024-03-04 16:33:11', '2024-03-04 16:33:44', '1', '1', 0);
INSERT INTO `dict_value` VALUES (221, 2, 'operatorLogType', 'operator-log:clear', '清空操作日志', '{}', 20, '2024-03-04 16:33:31', '2024-03-04 16:33:31', '1', '1', 0);
INSERT INTO `dict_value` VALUES (222, 2, 'operatorLogType', 'host-terminal:delete-sftp-log', '删除SFTP操作日志', '{}', 15, '2024-03-05 15:28:00', '2024-03-05 17:40:47', '1', '1', 0);
INSERT INTO `dict_value` VALUES (223, 33, 'sftpOperatorType', 'host-terminal:sftp-mkdir', '创建文件夹', '{}', 10, '2024-03-05 16:50:17', '2024-03-05 16:50:17', '1', '1', 0);
INSERT INTO `dict_value` VALUES (224, 33, 'sftpOperatorType', 'host-terminal:sftp-touch', '创建文件', '{}', 20, '2024-03-05 16:50:27', '2024-03-05 16:50:27', '1', '1', 0);
INSERT INTO `dict_value` VALUES (225, 33, 'sftpOperatorType', 'host-terminal:sftp-move', '移动文件', '{}', 30, '2024-03-05 16:50:41', '2024-03-05 16:50:41', '1', '1', 0);
INSERT INTO `dict_value` VALUES (226, 33, 'sftpOperatorType', 'host-terminal:sftp-remove', '删除文件', '{}', 40, '2024-03-05 16:50:53', '2024-03-05 16:50:53', '1', '1', 0);
INSERT INTO `dict_value` VALUES (227, 33, 'sftpOperatorType', 'host-terminal:sftp-truncate', '截断文件', '{}', 50, '2024-03-05 16:51:04', '2024-03-05 16:51:04', '1', '1', 0);
INSERT INTO `dict_value` VALUES (228, 33, 'sftpOperatorType', 'host-terminal:sftp-chmod', '文件提权', '{}', 60, '2024-03-05 16:51:15', '2024-03-05 16:51:15', '1', '1', 0);
INSERT INTO `dict_value` VALUES (229, 33, 'sftpOperatorType', 'host-terminal:sftp-set-content', '修改文件内容', '{}', 70, '2024-03-05 16:51:30', '2024-03-05 16:51:48', '1', '1', 0);
INSERT INTO `dict_value` VALUES (230, 33, 'sftpOperatorType', 'host-terminal:sftp-upload', '上传文件', '{}', 80, '2024-03-05 16:52:06', '2024-03-05 16:52:06', '1', '1', 0);
INSERT INTO `dict_value` VALUES (231, 33, 'sftpOperatorType', 'host-terminal:sftp-download', '下载文件', '{}', 90, '2024-03-05 16:52:18', '2024-03-05 16:52:18', '1', '1', 0);
```

## v1.0.0

> sql 脚本

```sql
INSERT INTO `dict_key` VALUES (32, 'terminalTabColor', 'COLOR', '[]', '终端标签页颜色', '2024-03-01 15:01:44', '2024-03-01 15:01:44', '1', '1', 0);
INSERT INTO `dict_value` VALUES (203, 32, 'terminalTabColor', 'rgb(var(--red-6))', '红色', '{}', 10, '2024-03-01 15:07:41', '2024-03-01 15:07:41', '1', '1', 0);
INSERT INTO `dict_value` VALUES (204, 32, 'terminalTabColor', 'rgb(var(--orange-6))', '橙色', '{}', 20, '2024-03-01 15:07:55', '2024-03-01 15:07:55', '1', '1', 0);
INSERT INTO `dict_value` VALUES (205, 32, 'terminalTabColor', 'rgb(var(--yellow-6))', '黄色', '{}', 30, '2024-03-01 15:08:13', '2024-03-01 15:08:13', '1', '1', 0);
INSERT INTO `dict_value` VALUES (206, 32, 'terminalTabColor', 'rgb(var(--green-6))', '绿色', '{}', 40, '2024-03-01 15:08:23', '2024-03-01 15:08:23', '1', '1', 0);
INSERT INTO `dict_value` VALUES (207, 32, 'terminalTabColor', 'rgb(var(--cyan-6))', '青色', '{}', 50, '2024-03-01 15:08:46', '2024-03-01 15:08:46', '1', '1', 0);
INSERT INTO `dict_value` VALUES (208, 32, 'terminalTabColor', 'rgb(var(--blue-6))', '浅蓝', '{}', 60, '2024-03-01 15:11:01', '2024-03-01 15:11:01', '1', '1', 0);
INSERT INTO `dict_value` VALUES (209, 32, 'terminalTabColor', 'rgb(var(--arcoblue-6))', '蓝色', '{}', 70, '2024-03-01 15:11:11', '2024-03-01 15:11:11', '1', '1', 0);
INSERT INTO `dict_value` VALUES (210, 32, 'terminalTabColor', 'rgb(var(--purple-6))', '紫色', '{}', 80, '2024-03-01 15:11:20', '2024-03-01 15:11:20', '1', '1', 0);
INSERT INTO `dict_value` VALUES (211, 32, 'terminalTabColor', 'rgb(var(--pinkpurple-6))', '粉紫', '{}', 90, '2024-03-01 15:11:41', '2024-03-01 15:11:41', '1', '1', 0);
INSERT INTO `dict_value` VALUES (213, 32, 'terminalTabColor', 'rgb(var(--gray-6))', '灰色', '{}', 100, '2024-03-01 15:12:01', '2024-03-01 15:39:34', '1', '1', 0);
```
