SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`  varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `BLOB_DATA`     blob                                                          NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    INDEX `SCHED_NAME` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `CALENDAR`      blob                                                          NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`
(
    `SCHED_NAME`      varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`    varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP`   varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TIME_ZONE_ID`    varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`
(
    `SCHED_NAME`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `ENTRY_ID`          varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `TRIGGER_NAME`      varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP`     varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `INSTANCE_NAME`     varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `FIRED_TIME`        bigint(0)                                                     NOT NULL,
    `SCHED_TIME`        bigint(0)                                                     NOT NULL,
    `PRIORITY`          int(0)                                                        NOT NULL,
    `STATE`             varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `JOB_NAME`          varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `JOB_GROUP`         varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `IS_NONCONCURRENT`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL,
    `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
    INDEX `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
    INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
    INDEX `IDX_QRTZ_FT_J_G` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_FT_JG` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_FT_T_G` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_FT_TG` (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`
(
    `SCHED_NAME`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `JOB_NAME`          varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `JOB_GROUP`         varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `DESCRIPTION`       varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `JOB_CLASS_NAME`    varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `IS_DURABLE`        varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `IS_NONCONCURRENT`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `IS_UPDATE_DATA`    varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `JOB_DATA`          blob                                                          NULL,
    PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
    INDEX `IDX_QRTZ_J_GRP` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`
(
    `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `LOCK_NAME`  varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`
(
    `SCHED_NAME`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `INSTANCE_NAME`     varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `LAST_CHECKIN_TIME` bigint(0)                                                     NOT NULL,
    `CHECKIN_INTERVAL`  bigint(0)                                                     NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`
(
    `SCHED_NAME`      varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`    varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP`   varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `REPEAT_COUNT`    bigint(0)                                                     NOT NULL,
    `REPEAT_INTERVAL` bigint(0)                                                     NOT NULL,
    `TIMES_TRIGGERED` bigint(0)                                                     NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`  varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `STR_PROP_1`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `STR_PROP_2`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `STR_PROP_3`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `INT_PROP_1`    int(0)                                                        NULL DEFAULT NULL,
    `INT_PROP_2`    int(0)                                                        NULL DEFAULT NULL,
    `LONG_PROP_1`   bigint(0)                                                     NULL DEFAULT NULL,
    `LONG_PROP_2`   bigint(0)                                                     NULL DEFAULT NULL,
    `DEC_PROP_1`    decimal(13, 4)                                                NULL DEFAULT NULL,
    `DEC_PROP_2`    decimal(13, 4)                                                NULL DEFAULT NULL,
    `BOOL_PROP_1`   varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL,
    `BOOL_PROP_2`   varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`
(
    `SCHED_NAME`     varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`   varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP`  varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `JOB_NAME`       varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `JOB_GROUP`      varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `DESCRIPTION`    varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `NEXT_FIRE_TIME` bigint(0)                                                     NULL DEFAULT NULL,
    `PREV_FIRE_TIME` bigint(0)                                                     NULL DEFAULT NULL,
    `PRIORITY`       int(0)                                                        NULL DEFAULT NULL,
    `TRIGGER_STATE`  varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `TRIGGER_TYPE`   varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `START_TIME`     bigint(0)                                                     NOT NULL,
    `END_TIME`       bigint(0)                                                     NULL DEFAULT NULL,
    `CALENDAR_NAME`  varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `MISFIRE_INSTR`  smallint(0)                                                   NULL DEFAULT NULL,
    `JOB_DATA`       blob                                                          NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_T_J` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_T_JG` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_T_C` (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
    INDEX `IDX_QRTZ_T_G` (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_T_STATE` (`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
    INDEX `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
    INDEX `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
    INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
    INDEX `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
    INDEX `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
    INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
    INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
    CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for _copy
-- ----------------------------
DROP TABLE IF EXISTS `_copy`;
CREATE TABLE `_copy`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
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
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `prefix`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '触发前缀',
    `command`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL COMMENT '代码片段',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '命令片段'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for command_snippet_group
-- ----------------------------
DROP TABLE IF EXISTS `command_snippet_group`;
CREATE TABLE `command_snippet_group`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组名称',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '命令片段分组'
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
    `type`        char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '数据类型',
    `item`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置项',
    `value`       json                                                         NULL COMMENT '配置值',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_type_rel_id` (`type`, `rel_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '数据拓展信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_group
-- ----------------------------
DROP TABLE IF EXISTS `data_group`;
CREATE TABLE `data_group`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `parent_id`   bigint(0)                                                    NULL DEFAULT NULL COMMENT '父id',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组名称',
    `type`        char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '组类型',
    `sort`        int(0)                                                       NULL DEFAULT 10 COMMENT '排序',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_type` (`type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '数据分组'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_group_rel
-- ----------------------------
DROP TABLE IF EXISTS `data_group_rel`;
CREATE TABLE `data_group_rel`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `group_id`    bigint(0)                                                    NULL DEFAULT NULL COMMENT '组id',
    `rel_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '引用id',
    `type`        char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '组类型',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_group_rel` (`group_id`, `rel_id`) USING BTREE,
    INDEX `idx_type` (`type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '数据分组关联'
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
    `type`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_role_id` (`role_id`) USING BTREE,
    INDEX `idx_type_rel` (`type`, `rel_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dict_key
-- ----------------------------
DROP TABLE IF EXISTS `dict_key`;
CREATE TABLE `dict_key`
(
    `id`           bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `key_name`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置项',
    `value_type`   char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '配置值定义',
    `extra_schema` json                                                         NULL COMMENT '额外配置定义',
    `description`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置描述',
    `create_time`  datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`      tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_key` (`key_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典配置项'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dict_value
-- ----------------------------
DROP TABLE IF EXISTS `dict_value`;
CREATE TABLE `dict_value`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `key_id`      bigint(0)                                                     NOT NULL COMMENT '配置项id',
    `key_name`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '配置项',
    `value`       varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置值',
    `label`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '配置描述',
    `extra`       json                                                          NULL COMMENT '额外参数',
    `sort`        int(0)                                                        NULL DEFAULT NULL COMMENT '排序',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_key_id` (`key_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典配置值'
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

-- ----------------------------
-- Table structure for exec_job
-- ----------------------------
DROP TABLE IF EXISTS `exec_job`;
CREATE TABLE `exec_job`
(
    `id`               bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '任务名称',
    `exec_seq`         int(0)                                                        NULL DEFAULT 0 COMMENT '执行序列',
    `expression`       varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron 表达式',
    `timeout`          int(0)                                                        NULL DEFAULT 0 COMMENT '超时时间',
    `command`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '执行命令',
    `parameter_schema` json                                                          NULL COMMENT '命令参数',
    `status`           tinyint(0)                                                    NULL DEFAULT 1 COMMENT '任务状态',
    `recent_log_id`    bigint(0)                                                     NULL DEFAULT NULL COMMENT '最近执行id',
    `create_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`          tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '计划任务'
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
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '计划任务主机'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exec_log
-- ----------------------------
DROP TABLE IF EXISTS `exec_log`;
CREATE TABLE `exec_log`
(
    `id`               bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`          bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行用户id',
    `username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '执行用户名',
    `source`           char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT NULL COMMENT '执行来源',
    `source_id`        bigint(0)                                                     NULL DEFAULT NULL COMMENT '执行来源id',
    `description`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行描述',
    `exec_seq`         int(0)                                                        NULL DEFAULT 0 COMMENT '执行序列',
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

-- ----------------------------
-- Table structure for exec_template
-- ----------------------------
DROP TABLE IF EXISTS `exec_template`;
CREATE TABLE `exec_template`
(
    `id`               bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `command`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL COMMENT '命令',
    `timeout`          int(0)                                                       NULL DEFAULT 0 COMMENT '超时时间秒 0不超时',
    `parameter_schema` json                                                         NULL COMMENT '参数定义',
    `create_time`      datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`          tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '执行模板'
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
    `type`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '收藏类型',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_type_user` (`type`, `user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '收藏关联'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for history_value
-- ----------------------------
DROP TABLE IF EXISTS `history_value`;
CREATE TABLE `history_value`
(
    `id`           bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `rel_id`       bigint(0)                                                    NULL DEFAULT NULL COMMENT '引用id',
    `type`         char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '类型',
    `before_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL COMMENT '修改前',
    `after_value`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL COMMENT '修改后',
    `create_time`  datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `deleted`      tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '历史归档表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host
-- ----------------------------
DROP TABLE IF EXISTS `host`;
CREATE TABLE `host`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '主机名称',
    `code`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '主机编码',
    `address`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机地址',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主机'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host_config
-- ----------------------------
DROP TABLE IF EXISTS `host_config`;
CREATE TABLE `host_config`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `host_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '主机id',
    `type`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置类型',
    `status`      tinyint(0)                                                   NULL DEFAULT 1 COMMENT '状态 0停用 1启用',
    `config`      json                                                         NULL COMMENT '配置详情',
    `version`     int(0)                                                       NULL DEFAULT 0 COMMENT '配置版本号',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_host_type` (`host_id`, `type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主机配置'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host_connect_log
-- ----------------------------
DROP TABLE IF EXISTS `host_connect_log`;
CREATE TABLE `host_connect_log`
(
    `id`           bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`      bigint(0)                                                     NULL DEFAULT NULL COMMENT '用户id',
    `username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '用户名',
    `host_id`      bigint(0)                                                     NULL DEFAULT NULL COMMENT '主机id',
    `host_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '主机名称',
    `host_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机地址',
    `type`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '类型',
    `status`       varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '状态',
    `token`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'token',
    `start_time`   datetime(3)                                                   NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`     datetime(3)                                                   NULL DEFAULT NULL COMMENT '结束时间',
    `extra_info`   json                                                          NULL COMMENT '额外信息',
    `create_time`  datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `deleted`      tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_host_type` (`host_id`, `type`) USING BTREE,
    INDEX `idx_token` (`token`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主机连接日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host_identity
-- ----------------------------
DROP TABLE IF EXISTS `host_identity`;
CREATE TABLE `host_identity`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '名称',
    `username`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
    `password`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户密码',
    `key_id`      bigint(0)                                                     NULL DEFAULT NULL COMMENT '秘钥id',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主机身份'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for host_key
-- ----------------------------
DROP TABLE IF EXISTS `host_key`;
CREATE TABLE `host_key`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '名称',
    `public_key`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '公钥文本',
    `private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '私钥文本',
    `password`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主机秘钥'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for operator_log
-- ----------------------------
DROP TABLE IF EXISTS `operator_log`;
CREATE TABLE `operator_log`
(
    `id`            bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`       bigint(0)                                                     NULL DEFAULT NULL COMMENT '用户id',
    `username`      char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT NULL COMMENT '用户名',
    `trace_id`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'traceId',
    `address`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '请求ip',
    `location`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '请求地址',
    `user_agent`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'userAgent',
    `risk_level`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '风险等级',
    `module`        char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT NULL COMMENT '模块',
    `type`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '操作类型',
    `log_info`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '日志',
    `extra`         json                                                          NULL COMMENT '参数',
    `result`        int(0)                                                        NULL DEFAULT NULL COMMENT '操作结果 0失败 1成功',
    `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
    `return_value`  json                                                          NULL COMMENT '返回值',
    `duration`      int(0)                                                        NULL DEFAULT NULL COMMENT '操作时间',
    `start_time`    datetime(3)                                                   NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`      datetime(3)                                                   NULL DEFAULT NULL COMMENT '结束时间',
    `create_time`   datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted`       tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    INDEX `idx_type` (`type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '操作日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for preference
-- ----------------------------
DROP TABLE IF EXISTS `preference`;
CREATE TABLE `preference`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`     bigint(0)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `type`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '偏好类型',
    `item`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置项',
    `value`       json                                                         NULL COMMENT '配置值',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_type` (`user_id`, `type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户偏好'
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
    `create_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
    `code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
    `status`      tinyint(0)                                                   NULL DEFAULT 1 COMMENT '状态 0停用 1启用',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
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
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_role` (`role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '角色菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`
(
    `id`              bigint(0)                                                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '用户名',
    `password`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '密码',
    `nickname`        varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '花名',
    `avatar`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像地址',
    `mobile`          varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '手机号',
    `email`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '邮箱',
    `status`          tinyint(0)                                                    NULL DEFAULT 1 COMMENT '用户状态 0停用 1启用 2锁定',
    `last_login_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '最后登录时间',
    `create_time`     datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime(0)                                                   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `updater`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `deleted`         tinyint(1)                                                    NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
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
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
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
    `name`        char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '标签名称',
    `type`        char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '标签类型',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_type` (`type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '标签枚举'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tag_rel
-- ----------------------------
DROP TABLE IF EXISTS `tag_rel`;
CREATE TABLE `tag_rel`
(
    `id`          bigint(0)                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `tag_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '标签id',
    `tag_name`    char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '标签名称',
    `tag_type`    char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '标签类型',
    `rel_id`      bigint(0)                                                    NULL DEFAULT NULL COMMENT '关联id',
    `create_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_tag` (`tag_id`) USING BTREE,
    INDEX `idx_type_rel` (`tag_type`, `rel_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '标签关联'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
