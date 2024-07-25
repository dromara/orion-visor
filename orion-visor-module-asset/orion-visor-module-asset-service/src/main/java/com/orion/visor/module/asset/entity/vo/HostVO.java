package com.orion.visor.module.asset.entity.vo;

import com.orion.visor.module.infra.entity.dto.tag.TagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 主机 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostVO", description = "主机 视图响应对象")
public class HostVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "主机类型")
    private String type;

    @Schema(description = "主机名称")
    private String name;

    @Schema(description = "主机编码")
    private String code;

    @Schema(description = "主机地址")
    private String address;

    @Schema(description = "主机端口")
    private Integer port;

    @Schema(description = "主机状态")
    private String status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "修改人")
    private String updater;

    @Schema(description = "是否收藏")
    private Boolean favorite;

    @Schema(description = "tags")
    private List<TagDTO> tags;

    @Schema(description = "分组 id")
    private Set<Long> groupIdList;

    @Schema(description = "别名")
    private String alias;

    @Schema(description = "颜色")
    private String color;

}
