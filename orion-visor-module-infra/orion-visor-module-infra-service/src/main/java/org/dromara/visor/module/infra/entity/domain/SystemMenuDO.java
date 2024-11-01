/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

/**
 * 菜单 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-18 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "system_menu", autoResultMap = true)
@Schema(name = "SystemMenuDO", description = "菜单 实体对象")
public class SystemMenuDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "父id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "菜单名称")
    @TableField("name")
    private String name;

    @Schema(description = "菜单权限")
    @TableField("permission")
    private String permission;

    @Schema(description = "菜单类型 1父菜单 2子菜单 3功能")
    @TableField("type")
    private Integer type;

    @Schema(description = "排序")
    @TableField("sort")
    private Integer sort;

    @Schema(description = "是否可见 0不可见 1可见")
    @TableField("visible")
    private Integer visible;

    @Schema(description = "菜单状态 0停用 1启用")
    @TableField("status")
    private Integer status;

    @Schema(description = "菜单缓存 0不缓存 1缓存")
    @TableField("cache")
    private Integer cache;

    @Schema(description = "新窗口打开 0关闭 1开启")
    @TableField("new_window")
    private Integer newWindow;

    @Schema(description = "菜单图标")
    @TableField("icon")
    private String icon;

    @Schema(description = "链接地址")
    @TableField("path")
    private String path;

    @Schema(description = "组件名称")
    @TableField("component")
    private String component;

}
