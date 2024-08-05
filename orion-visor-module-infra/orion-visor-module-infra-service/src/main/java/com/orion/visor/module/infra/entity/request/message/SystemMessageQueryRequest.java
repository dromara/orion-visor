package com.orion.visor.module.infra.entity.request.message;

import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SystemMessageQueryRequest", description = "系统消息 查询请求对象")
public class SystemMessageQueryRequest extends PageRequest {

    @Schema(description = "maxId")
    private Long maxId;

    @Schema(description = "消息分类")
    private String classify;

    @Schema(description = "是否查询未读消息")
    private Boolean queryUnread;

}
