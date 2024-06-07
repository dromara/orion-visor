package com.orion.visor.module.asset.handler.host.terminal.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 列表响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/6 16:20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SftpListResponse extends SftpBaseResponse {

    /**
     * path
     */
    private String path;

    /**
     * body
     */
    private String body;

}
