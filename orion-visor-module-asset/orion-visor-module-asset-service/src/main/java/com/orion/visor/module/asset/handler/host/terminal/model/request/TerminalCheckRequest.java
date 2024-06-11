package com.orion.visor.module.asset.handler.host.terminal.model.request;

import com.orion.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 主机连接检查请求 实体对象
 * <p>
 * ck|eff00a1|1
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
public class TerminalCheckRequest extends TerminalBasePayload {

    /**
     * 主机id
     */
    private Long hostId;

    /**
     * 连接类型
     */
    private String connectType;

}
