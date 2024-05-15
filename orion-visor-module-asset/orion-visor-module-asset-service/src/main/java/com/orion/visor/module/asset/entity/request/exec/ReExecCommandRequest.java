package com.orion.visor.module.asset.entity.request.exec;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 重新执行命令 请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 11:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReExecCommandRequest", description = "重新执行命令 请求对象")
public class ReExecCommandRequest {

    @NonNull
    @Schema(description = "logId")
    private Long logId;

}
