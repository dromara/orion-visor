-- 创建默认表
CREATE TABLE `table` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0未删除 1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='table';


-- 添加默认列
ALTER TABLE `table` 
ADD COLUMN `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER type,
ADD COLUMN `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER create_time,
ADD COLUMN `creator` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人' AFTER update_time,
ADD COLUMN `updater` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人' AFTER creator,
ADD COLUMN `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0未删除 1已删除' AFTER updater;

-- 删除已删除的元数据
DELETE FROM dict_key WHERE deleted = 1;
DELETE FROM dict_value WHERE deleted = 1;
DELETE FROM system_menu WHERE deleted = 1;
