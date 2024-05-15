package com.orion.visor.module.infra.entity.request.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统消息 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemMessageQueryRequest", description = "系统消息 查询请求对象")
public class SystemMessageQueryRequest {

    @Schema(description = "大小")
    private Integer limit;

    @Schema(description = "maxId")
    private Long maxId;

    @Schema(description = "消息分类")
    private String classify;

    @Schema(description = "是否查询未读消息")
    private Boolean queryUnread;

}
