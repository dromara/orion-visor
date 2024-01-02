package com.orion.ops.module.asset.handler.host.terminal.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息体包装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 16:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MessageWrapper", description = "消息体包装")
public class Message<T> {

    @JSONField(name = "s")
    @Schema(description = "会话id")
    private String session;

    @JSONField(name = "t")
    @Schema(description = "消息类型")
    private String type;

    @JSONField(name = "b")
    @Schema(description = "消息体")
    private T body;

}
