package com.orion.ops.module.infra.entity.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;

/**
 * 系统消息 请求业务对象
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemMessageDTO", description = "系统消息 请求业务对象")
public class SystemMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "消息关联")
    private String relKey;

    @NotNull
    @Schema(description = "接收人id")
    private Long receiverId;

    @Schema(description = "接收人用户名")
    private String receiverUsername;

    @Schema(description = "参数")
    private Map<String, Object> params;

}
