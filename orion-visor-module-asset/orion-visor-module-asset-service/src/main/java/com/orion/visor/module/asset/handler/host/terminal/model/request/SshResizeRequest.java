package com.orion.visor.module.asset.handler.host.terminal.model.request;

import com.orion.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ssh 修改大小请求 实体对象
 * <p>
 * rs|eff00a1|100|20
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
public class SshResizeRequest extends TerminalBasePayload {

    /**
     * 列数
     */
    private Integer cols;

    /**
     * 行数
     */
    private Integer rows;

}
