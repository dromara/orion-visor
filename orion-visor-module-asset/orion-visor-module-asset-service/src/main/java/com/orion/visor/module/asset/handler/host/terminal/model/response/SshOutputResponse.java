package com.orion.visor.module.asset.handler.host.terminal.model.response;

import com.orion.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ssh 输出响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 16:20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SshOutputResponse extends TerminalBasePayload {

    /**
     * body
     */
    private String body;

}
