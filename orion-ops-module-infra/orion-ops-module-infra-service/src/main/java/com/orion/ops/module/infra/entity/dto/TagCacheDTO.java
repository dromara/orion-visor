package com.orion.ops.module.infra.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/5 15:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(name = "TagCacheDTO", description = "菜单 缓存业务对象")
public class TagCacheDTO {

    @EqualsAndHashCode.Include
    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "标签名称")
    @TableField("name")
    private String name;

}
