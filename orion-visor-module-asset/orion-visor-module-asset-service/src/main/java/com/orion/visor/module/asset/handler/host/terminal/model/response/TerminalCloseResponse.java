package com.orion.visor.module.asset.handler.host.terminal.model.response;

import com.orion.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 主机连接关闭响应 实体对象
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
public class TerminalCloseResponse extends TerminalBasePayload {

    /**
     * 是否为强制关闭
     */
    private Integer forceClose;

    /**
     * 关闭信息
     */
    private String msg;

}
