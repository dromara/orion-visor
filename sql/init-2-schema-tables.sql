SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for _copy
-- ----------------------------
DROP TABLE IF EXISTS `_copy`;
CREATE TABLE `_copy`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for command_snippet
-- ----------------------------
DROP TABLE IF EXISTS `command_snippet`;
CREATE TABLE `command_snippet`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `group_id`    bigint(0)                                                    NULL DEFAULT NULL COMMENT '分组id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
    `command`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci        NULL COMMENT '代码片段',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '命令片段'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_extra
-- ----------------------------
DROP TABLE IF EXISTS `data_extra`;
CREATE TABLE `data_extra`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `rel_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '数据id',
    `type`        char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '数据类型',
    `item`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置项',
    `value`       json                                                         NULL COMMENT '配置值',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_type_rel_id` (`type`, `rel_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '数据拓展信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_group
-- ----------------------------
DROP TABLE IF EXISTS `data_group`;
CREATE TABLE `data_group`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `parent_id`   bigint(0)                                                    NULL DEFAULT NULL COMMENT '父id',
    `type`        char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '组类型',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组名称',
    `sort`        int(0)                                                       NULL DEFAULT 10 COMMENT '排序',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_type_user` (`type`, `user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '数据分组'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_group_rel
-- ----------------------------
DROP TABLE IF EXISTS `data_group_rel`;
CREATE TABLE `data_group_rel`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `type`        char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '组类型',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `group_id`    bigint(0)                                                    NULL DEFAULT NULL COMMENT '组id',
    `rel_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '引用id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_group_rel` (`group_id`, `rel_id`) USING BTREE,
    INDEX `idx_type_user` (`type`, `user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '数据分组关联'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_permission
-- ----------------------------
DROP TABLE IF EXISTS `data_permission`;
CREATE TABLE `data_permission`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `role_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '角色id',
    `rel_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '引用id',
    `type`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据类型',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_role_id` (`role_id`) USING BTREE,
    INDEX `idx_type_rel` (`type`, `rel_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '数据权限表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dict_key
-- ----------------------------
DROP TABLE IF EXISTS `dict_key`;
CREATE TABLE `dict_key`
(
    `id`           bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `key_name`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '配置项',
    `value_type`   char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '配置值定义',
    `extra_schema` json                                                          NULL COMMENT '额外配置定义',
    `description`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置描述',
    `create_time`  datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`  datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`      tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_key` (`key_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '字典配置项'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dict_value
-- ----------------------------
DROP TABLE IF EXISTS `dict_value`;
CREATE TABLE `dict_value`
(
    `id`          bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT 'id',
    `key_id`      bigint(0)                                                      NOT NULL COMMENT '配置项id',
    `key_name`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL COMMENT '配置项',
    `value`       varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置值',
    `label`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '配置描述',
    `extra`       json                                                           NULL COMMENT '额外参数',
    `sort`        int(0)                                                         NULL DEFAULT NULL COMMENT '排序',
    `create_time` datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                     NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_key_id` (`key_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '字典配置值'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exec_host_log
-- ----------------------------
DROP TABLE IF EXISTS `exec_host_log`;
CREATE TABLE `exec_host_log`
(
    `id`            bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `log_id`        bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行日志id',
    `host_id`       bigint(0)                                                     NULL DEFAULT NULL COMMENT '主机id',
    `host_name`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主机名称',
    `host_address`  varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主机地址',
    `status`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '执行状态',
    `command`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '执行命令',
    `parameter`     json                                                          NULL COMMENT '执行参数',
    `exit_code`     int(0)                                                        NULL DEFAULT NULL COMMENT '退出码',
    `log_path`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '日志路径',
    `script_path`   varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '脚本路径',
    `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '错误信息',
    `start_time`    datetime(6)                                                   NULL DEFAULT NULL COMMENT '执行开始时间',
    `finish_time`   datetime(6)                                                   NULL DEFAULT NULL COMMENT '执行结束时间',
    `create_time`   datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`   datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`       tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_log_id` (`log_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '批量执行主机日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exec_job
-- ----------------------------
DROP TABLE IF EXISTS `exec_job`;
CREATE TABLE `exec_job`
(
    `id`               bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '任务名称',
    `exec_seq`         int(0)                                                        NULL DEFAULT 0 COMMENT '执行序列',
    `expression`       varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'cron 表达式',
    `timeout`          int(0)                                                        NULL DEFAULT 0 COMMENT '超时时间',
    `script_exec`      tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否使用脚本执行',
    `command`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '执行命令',
    `parameter_schema` json                                                          NULL COMMENT '命令参数',
    `status`           tinyint(0)                                                    NULL DEFAULT 0 COMMENT '任务状态',
    `recent_log_id`    bigint(0)                                                     NULL DEFAULT NULL COMMENT '最近执行id',
    `exec_user_id`     bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行用户id',
    `exec_username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '执行用户名',
    `create_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`          tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_exec_user_id` (`exec_user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '计划任务'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exec_job_host
-- ----------------------------
DROP TABLE IF EXISTS `exec_job_host`;
CREATE TABLE `exec_job_host`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `job_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '任务id',
    `host_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '主机id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '计划任务主机'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exec_log
-- ----------------------------
DROP TABLE IF EXISTS `exec_log`;
CREATE TABLE `exec_log`
(
    `id`               bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`          bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行用户id',
    `username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '执行用户名',
    `source`           char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '执行来源',
    `source_id`        bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行来源id',
    `exec_mode`        char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '执行方式',
    `description`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '执行描述',
    `exec_seq`         int(0)                                                        NULL DEFAULT 0 COMMENT '执行序列',
    `command`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '执行命令',
    `parameter_schema` json                                                          NULL COMMENT '参数 schema',
    `timeout`          int(0)                                                        NULL DEFAULT NULL COMMENT '超时时间',
    `script_exec`      tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否使用脚本执行',
    `status`           char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '执行状态',
    `start_time`       datetime(6)                                                   NULL DEFAULT NULL COMMENT '执行开始时间',
    `finish_time`      datetime(6)                                                   NULL DEFAULT NULL COMMENT '执行完成时间',
    `create_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`          tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_source` (`source`, `source_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '批量执行日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exec_template
-- ----------------------------
DROP TABLE IF EXISTS `exec_template`;
CREATE TABLE `exec_template`
(
    `id`               bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
    `command`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci        NULL COMMENT '命令',
    `timeout`          int(0)                                                       NULL DEFAULT 0 COMMENT '超时时间秒 0不超时',
    `script_exec`      tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否使用脚本执行',
    `parameter_schema` json                                                         NULL COMMENT '参数定义',
    `create_time`      datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`      datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`          tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '执行模板'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exec_template_host
-- ----------------------------
DROP TABLE IF EXISTS `exec_template_host`;
CREATE TABLE `exec_template_host`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `template_id` bigint(0)                                                    NULL DEFAULT NULL COMMENT '模板id',
    `host_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '主机id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `template_id` (`template_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '执行模板主机'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `rel_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '引用id',
    `type`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '收藏类型',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_type_user` (`type`, `user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '收藏关联'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for history_value
-- ----------------------------
DROP TABLE IF EXISTS `history_value`;
CREATE TABLE `history_value`
(
    `id`           bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `rel_id`       bigint(0)                                                    NULL DEFAULT NULL COMMENT '引用id',
    `type`         char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '类型',
    `before_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci        NULL COMMENT '修改前',
    `after_value`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci        NULL COMMENT '修改后',
    `create_time`  datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `creator`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `deleted`      tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '历史归档表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host
-- ----------------------------
DROP TABLE IF EXISTS `host`;
CREATE TABLE `host`
(
    `id`                       bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `types`                    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '主机类型',
    `os_type`                  char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '系统类型',
    `arch_type`                char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '系统架构',
    `name`                     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '主机名称',
    `code`                     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '主机编码',
    `address`                  varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主机地址',
    `status`                   char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '主机状态',
    `agent_key`                char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT 'agentKey',
    `agent_version`            char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '探针版本',
    `agent_install_status`     tinyint(0)                                                    NULL DEFAULT NULL COMMENT '探针安装状态',
    `agent_online_status`      tinyint(0)                                                    NULL DEFAULT NULL COMMENT '探针在线状态',
    `agent_online_change_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '探针切换在线状态时间',
    `description`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主机描述',
    `create_time`              datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`              datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`                  tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_agent_key` (`agent_key`) USING BTREE,
    INDEX `idx_name` (`name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '主机'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host_agent_log
-- ----------------------------
DROP TABLE IF EXISTS `host_agent_log`;
CREATE TABLE `host_agent_log`
(
    `id`          bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT 'id',
    `host_id`     bigint(0)                                                      NULL DEFAULT NULL COMMENT '主机id',
    `agent_key`   char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT 'agentKey',
    `type`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '类型',
    `status`      char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '状态',
    `message`     varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '消息',
    `create_time` datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                     NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_agent_key` (`agent_key`) USING BTREE,
    INDEX `idx_host_id` (`host_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '主机探针日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host_config
-- ----------------------------
DROP TABLE IF EXISTS `host_config`;
CREATE TABLE `host_config`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `host_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '主机id',
    `type`        char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '配置类型',
    `status`      char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '配置状态',
    `config`      json                                                         NULL COMMENT '配置值',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `host_type_idx` (`host_id`, `type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '主机配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host_identity
-- ----------------------------
DROP TABLE IF EXISTS `host_identity`;
CREATE TABLE `host_identity`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '名称',
    `type`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '类型',
    `username`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
    `password`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户密码',
    `key_id`      bigint(0)                                                     NULL DEFAULT NULL COMMENT '密钥id',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '主机身份'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host_key
-- ----------------------------
DROP TABLE IF EXISTS `host_key`;
CREATE TABLE `host_key`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '名称',
    `public_key`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '公钥文本',
    `private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '私钥文本',
    `password`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '主机密钥'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_alarm_event
-- ----------------------------
DROP TABLE IF EXISTS `monitor_alarm_event`;
CREATE TABLE `monitor_alarm_event`
(
    `id`                  bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT 'id',
    `agent_key`           char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT 'agentKey',
    `source_type`         char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '事件来源',
    `source_id`           bigint(0)                                                      NULL DEFAULT NULL COMMENT '事件来源id',
    `source_info`         json                                                           NULL COMMENT '事件来源信息',
    `policy_id`           bigint(0)                                                      NULL DEFAULT NULL COMMENT '策略id',
    `policy_rule_id`      bigint(0)                                                      NULL DEFAULT NULL COMMENT '策略规则id',
    `metrics_id`          bigint(0)                                                      NULL DEFAULT NULL COMMENT '指标id',
    `metrics_measurement` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '指标数据集',
    `alarm_tags`          json                                                           NULL COMMENT '告警标签',
    `alarm_value`         decimal(19, 4)                                                 NULL DEFAULT NULL COMMENT '告警值',
    `alarm_threshold`     decimal(19, 4)                                                 NULL DEFAULT NULL COMMENT '告警阈值',
    `alarm_info`          varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '告警摘要',
    `alarm_level`         tinyint(0)                                                     NULL DEFAULT NULL COMMENT '告警级别',
    `trigger_condition`   char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci       NULL DEFAULT NULL COMMENT '告警条件',
    `consecutive_count`   int(0)                                                         NULL DEFAULT NULL COMMENT '连续触发次数',
    `false_alarm`         tinyint(0)                                                     NULL DEFAULT NULL COMMENT '是否误报',
    `handle_status`       char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '处理状态',
    `handle_time`         datetime(0)                                                    NULL DEFAULT NULL COMMENT '处理时间',
    `handle_remark`       varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '处理备注',
    `handle_user_id`      bigint(0)                                                      NULL DEFAULT NULL COMMENT '处理人id',
    `handle_username`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '处理人用户名',
    `create_time`         datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`         datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '创建人',
    `updater`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '更新人',
    `deleted`             tinyint(0)                                                     NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_agent_key` (`agent_key`) USING BTREE,
    INDEX `idx_handle_user` (`handle_user_id`) USING BTREE,
    INDEX `idx_source` (`source_type`, `source_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '监控告警事件'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_alarm_policy
-- ----------------------------
DROP TABLE IF EXISTS `monitor_alarm_policy`;
CREATE TABLE `monitor_alarm_policy`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `type`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '策略类型',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '策略名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '策略描述',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '监控告警策略'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_alarm_policy_notify
-- ----------------------------
DROP TABLE IF EXISTS `monitor_alarm_policy_notify`;
CREATE TABLE `monitor_alarm_policy_notify`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `policy_id`   bigint(0)                                                    NULL DEFAULT NULL COMMENT '策略id',
    `notify_id`   bigint(0)                                                    NULL DEFAULT NULL COMMENT '通知id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_policy_id` (`policy_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '监控告警策略通知'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_alarm_policy_rule
-- ----------------------------
DROP TABLE IF EXISTS `monitor_alarm_policy_rule`;
CREATE TABLE `monitor_alarm_policy_rule`
(
    `id`                  bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `policy_id`           bigint(0)                                                     NULL DEFAULT NULL COMMENT '策略id',
    `metrics_id`          bigint(0)                                                     NULL DEFAULT NULL COMMENT '指标id',
    `metrics_measurement` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '指标数据集',
    `tags`                json                                                          NULL COMMENT '指标标签',
    `rule_switch`         tinyint(0)                                                    NULL DEFAULT NULL COMMENT '规则开关',
    `all_effect`          tinyint(0)                                                    NULL DEFAULT 0 COMMENT '全部生效',
    `level`               tinyint(0)                                                    NULL DEFAULT NULL COMMENT '告警级别',
    `trigger_condition`   char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '告警条件',
    `threshold`           decimal(19, 4)                                                NULL DEFAULT NULL COMMENT '触发阈值',
    `silence_period`      int(0)                                                        NULL DEFAULT NULL COMMENT '静默时间',
    `consecutive_count`   int(0)                                                        NULL DEFAULT NULL COMMENT '连续触发次数',
    `description`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则描述',
    `create_time`         datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`         datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`             tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_policy_id` (`policy_id`) USING BTREE,
    INDEX `idx_metric_id` (`metrics_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '监控告警策略指标'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_host
-- ----------------------------
DROP TABLE IF EXISTS `monitor_host`;
CREATE TABLE `monitor_host`
(
    `id`             bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `host_id`        bigint(0)                                                    NULL DEFAULT NULL COMMENT '主机id',
    `policy_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '策略id',
    `agent_key`      char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT 'agent key',
    `alarm_switch`   tinyint(0)                                                   NULL DEFAULT 0 COMMENT '告警开关',
    `owner_user_id`  bigint(0)                                                    NULL DEFAULT NULL COMMENT '负责人id',
    `owner_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '负责人用户名',
    `monitor_meta`   json                                                         NULL COMMENT '监控元数据',
    `monitor_config` json                                                         NULL COMMENT '监控配置',
    `create_time`    datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`    datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`        tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_agent_key` (`agent_key`) USING BTREE,
    INDEX `idx_host_id` (`host_id`) USING BTREE,
    INDEX `idx_policy_id` (`policy_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '监控主机'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_metrics
-- ----------------------------
DROP TABLE IF EXISTS `monitor_metrics`;
CREATE TABLE `monitor_metrics`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '指标名称',
    `measurement` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '数据集',
    `value`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '指标项',
    `unit`        char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT 'NONE' COMMENT '单位',
    `suffix`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '后缀',
    `description` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '指标描述',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_value` (`value`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '监控指标'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notify_template
-- ----------------------------
DROP TABLE IF EXISTS `notify_template`;
CREATE TABLE `notify_template`
(
    `id`               bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '通知名称',
    `biz_type`         char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '业务类型',
    `channel_type`     char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '渠道类型',
    `channel_config`   json                                                          NULL COMMENT '渠道配置',
    `message_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '消息模板',
    `description`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
    `create_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`          tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_biz_type` (`biz_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '通知模板'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for operator_log
-- ----------------------------
DROP TABLE IF EXISTS `operator_log`;
CREATE TABLE `operator_log`
(
    `id`            bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`       bigint(0)                                                     NULL DEFAULT NULL COMMENT '用户id',
    `username`      char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '用户名',
    `trace_id`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT 'traceId',
    `address`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '请求ip',
    `location`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '请求地址',
    `user_agent`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'userAgent',
    `risk_level`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '风险等级',
    `module`        char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '模块',
    `type`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '操作类型',
    `log_info`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '日志',
    `extra`         json                                                          NULL COMMENT '参数',
    `result`        int(0)                                                        NULL DEFAULT NULL COMMENT '操作结果 0失败 1成功',
    `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '错误信息',
    `return_value`  json                                                          NULL COMMENT '返回值',
    `duration`      int(0)                                                        NULL DEFAULT NULL COMMENT '操作时间',
    `start_time`    datetime(6)                                                   NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`      datetime(6)                                                   NULL DEFAULT NULL COMMENT '结束时间',
    `create_time`   datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `deleted`       tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_type` (`type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for path_bookmark
-- ----------------------------
DROP TABLE IF EXISTS `path_bookmark`;
CREATE TABLE `path_bookmark`
(
    `id`          bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                      NULL DEFAULT NULL COMMENT '用户id',
    `group_id`    bigint(0)                                                      NULL DEFAULT NULL COMMENT '分组id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '名称',
    `type`        char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci       NULL DEFAULT NULL COMMENT '类型',
    `path`        varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路径',
    `create_time` datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                     NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '路径书签'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for preference
-- ----------------------------
DROP TABLE IF EXISTS `preference`;
CREATE TABLE `preference`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `type`        char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '偏好类型',
    `item`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置项',
    `value`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci        NULL COMMENT '配置值',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_type` (`user_id`, `type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户偏好'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `parent_id`   bigint(0)                                                     NOT NULL COMMENT '父id',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '菜单名称',
    `permission`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '菜单权限',
    `type`        tinyint(0)                                                    NOT NULL COMMENT '菜单类型 1父菜单 2子菜单 3功能',
    `sort`        int(0)                                                        NULL DEFAULT 0 COMMENT '排序',
    `visible`     tinyint(0)                                                    NULL DEFAULT 1 COMMENT '是否可见 0不可见 1可见',
    `status`      tinyint(0)                                                    NULL DEFAULT 1 COMMENT '菜单状态 0停用 1启用',
    `cache`       tinyint(0)                                                    NULL DEFAULT 1 COMMENT '是否缓存 0不缓存 1缓存',
    `new_window`  tinyint(0)                                                    NULL DEFAULT 0 COMMENT '新窗口打开 0关闭 1开启',
    `icon`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '菜单图标',
    `path`        varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '链接地址',
    `component`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件名称',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_message
-- ----------------------------
DROP TABLE IF EXISTS `system_message`;
CREATE TABLE `system_message`
(
    `id`                bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `classify`          char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL DEFAULT NULL COMMENT '消息分类',
    `type`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '消息类型',
    `status`            tinyint(0)                                                    NULL DEFAULT NULL COMMENT '消息状态',
    `rel_key`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '消息关联',
    `title`             varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
    `content`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NULL COMMENT '消息内容',
    `receiver_id`       bigint(0)                                                     NULL DEFAULT NULL COMMENT '接收人id',
    `receiver_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '接收人用户名',
    `create_time`       datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`       datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `deleted`           tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_receiver_classify` (`receiver_id`, `classify`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '系统消息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '角色名称',
    `code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '角色编码',
    `status`      tinyint(0)                                                    NULL DEFAULT 1 COMMENT '状态 0停用 1启用',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `role_id`     bigint(0)                                                    NOT NULL COMMENT '角色id',
    `menu_id`     bigint(0)                                                    NOT NULL COMMENT '菜单id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_role` (`role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '角色菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_setting
-- ----------------------------
DROP TABLE IF EXISTS `system_setting`;
CREATE TABLE `system_setting`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `type`        char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '配置类型',
    `config_key`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置key',
    `value`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci        NULL COMMENT '配置值',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_key` (`config_key`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '系统设置'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`
(
    `id`                     bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '用户名',
    `password`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '密码',
    `nickname`               varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '花名',
    `avatar`                 varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像地址',
    `mobile`                 varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '手机号',
    `email`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '邮箱',
    `status`                 tinyint(0)                                                    NULL DEFAULT 1 COMMENT '用户状态 0停用 1启用',
    `update_password_status` tinyint(0)                                                    NULL DEFAULT 0 COMMENT '修改密码状态 0无需修改 1需要修改',
    `update_password_reason` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '修改密码原因',
    `update_password_time`   datetime(0)                                                   NULL DEFAULT NULL COMMENT '修改密码时间',
    `last_login_time`        datetime(0)                                                   NULL DEFAULT NULL COMMENT '最后登录时间',
    `description`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户描述',
    `create_time`            datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`            datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`                tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_username` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NOT NULL COMMENT '用户id',
    `role_id`     bigint(0)                                                    NOT NULL COMMENT '角色id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '标签名称',
    `type`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '标签类型',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_type` (`type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '数据标签'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tag_rel
-- ----------------------------
DROP TABLE IF EXISTS `tag_rel`;
CREATE TABLE `tag_rel`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `tag_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '标签id',
    `tag_name`    char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '标签名称',
    `tag_type`    char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL DEFAULT NULL COMMENT '标签类型',
    `rel_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '关联id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_tag` (`tag_id`) USING BTREE,
    INDEX `idx_type_rel` (`tag_type`, `rel_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '标签关联'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for terminal_connect_log
-- ----------------------------
DROP TABLE IF EXISTS `terminal_connect_log`;
CREATE TABLE `terminal_connect_log`
(
    `id`           bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`      bigint(0)                                                     NULL DEFAULT NULL COMMENT '用户id',
    `username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '用户名',
    `host_id`      bigint(0)                                                     NULL DEFAULT NULL COMMENT '主机id',
    `host_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '主机名称',
    `host_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主机地址',
    `type`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '类型',
    `status`       varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '状态',
    `session_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT 'sessionId',
    `start_time`   datetime(6)                                                   NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`     datetime(6)                                                   NULL DEFAULT NULL COMMENT '结束时间',
    `extra_info`   json                                                          NULL COMMENT '额外信息',
    `create_time`  datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`  datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `deleted`      tinyint(0)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_host_type` (`host_id`, `type`) USING BTREE,
    INDEX `idx_session_id` (`session_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '终端连接日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for upload_task
-- ----------------------------
DROP TABLE IF EXISTS `upload_task`;
CREATE TABLE `upload_task`
(
    `id`          bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                      NULL DEFAULT NULL COMMENT '用户id',
    `username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '用户名',
    `remote_path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '远程路径',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '描述',
    `status`      char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '状态',
    `file_count`  int(0)                                                         NULL DEFAULT NULL COMMENT '文件数量',
    `host_count`  int(0)                                                         NULL DEFAULT NULL COMMENT '主机数量',
    `extra_info`  json                                                           NULL COMMENT '额外信息',
    `start_time`  datetime(6)                                                    NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`    datetime(6)                                                    NULL DEFAULT NULL COMMENT '结束时间',
    `create_time` datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(0)                                                     NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '上传任务'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for upload_task_file
-- ----------------------------
DROP TABLE IF EXISTS `upload_task_file`;
CREATE TABLE `upload_task_file`
(
    `id`             bigint(0)                                                      NOT NULL AUTO_INCREMENT COMMENT 'id',
    `task_id`        bigint(0)                                                      NULL DEFAULT NULL COMMENT '用户id',
    `host_id`        bigint(0)                                                      NULL DEFAULT NULL COMMENT '主机id',
    `file_id`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '文件id',
    `file_path`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件路径',
    `real_file_path` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '实际文件路径',
    `file_size`      bigint(0)                                                      NULL DEFAULT NULL COMMENT '文件大小',
    `status`         char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      NULL DEFAULT NULL COMMENT '状态',
    `start_time`     datetime(6)                                                    NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`       datetime(6)                                                    NULL DEFAULT NULL COMMENT '结束时间',
    `error_message`  varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '错误信息',
    `create_time`    datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`    datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '创建人',
    `updater`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL COMMENT '更新人',
    `deleted`        tinyint(0)                                                     NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_task_id` (`task_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '上传任务文件'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
