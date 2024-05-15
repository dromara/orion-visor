package com.orion.visor.module.infra.entity.request.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 系统消息 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemMessageCreateRequest", description = "系统消息 创建请求对象")
public class SystemMessageCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 10)
    @Schema(description = "消息分类")
    private String classify;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "消息类型")
    private String type;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "消息关联")
    private String relKey;

    @NotBlank
    @Size(max = 128)
    @Schema(description = "标题")
    private String title;

    @NotBlank
    @Schema(description = "消息内容")
    private String content;

    @NotNull
    @Schema(description = "接收人id")
    private Long receiverId;

    @Size(max = 32)
    @Schema(description = "接收人用户名")
    private String receiverUsername;

}
