package com.orion.visor.module.asset.handler.host.exec.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量执行启动对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecCommandDTO {

    /**
     * logId
     */
    private Long logId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 执行描述
     */
    private String description;

    /**
     * 执行序列
     */
    private Integer execSeq;

    /**
     * 超时时间
     */
    private Integer timeout;

    /**
     * 是否使用脚本执行
     */
    private Boolean scriptExec;

    /**
     * 执行主机
     */
    private List<ExecCommandHostDTO> hosts;

}
