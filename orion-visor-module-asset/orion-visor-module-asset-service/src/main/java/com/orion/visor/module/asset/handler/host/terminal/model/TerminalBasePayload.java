package com.orion.visor.module.asset.handler.host.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 终端基础 payload
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 21:51
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalBasePayload {

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 消息类型
     */
    private String type;

}
