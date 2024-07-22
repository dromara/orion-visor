package com.orion.visor.module.asset.handler.host.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件操作请求 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 21:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOperatorRequest {

    /**
     * 文件路径
     */
    private String path;

    /**
     * type
     */
    private String type;

    /**
     * operator
     */
    private String operator;

    /**
     * 主机id
     */
    private Long hostId;

    /**
     * 错误信息 后端赋值
     */
    private String errorMessage;

}
