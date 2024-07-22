package com.orion.visor.module.asset.handler.host.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件操作响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOperatorResponse {

    /**
     * channelId
     */
    private String channelId;

    /**
     * type
     */
    private String type;

    /**
     * 主机id
     */
    private Long hostId;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 传输的大小
     */
    private Long currentSize;

    /**
     * 文件总大小
     */
    private Long totalSize;

    /**
     * transferToken
     */
    private String transferToken;

    /**
     * 消息
     */
    private String msg;

}
