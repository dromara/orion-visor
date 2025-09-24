package org.dromara.visor.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 推送用户
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/18 21:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PushUser", description = "推送用户")
public class PushUser implements Serializable {

    @NotNull
    @Schema(description = "用户id")
    private Long id;

    @NotNull
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "花名")
    private String nickname;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

}
