package com.orion.visor.module.asset.handler.host.terminal.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 列表请求 实体对象
 * <p>
 * i|eff00a1|path
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/6 13:31
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SftpListRequest extends SftpBaseRequest {

    /**
     * 是否显示隐藏文件
     */
    private Integer showHiddenFile;

}
