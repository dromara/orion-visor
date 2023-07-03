package com.orion.ops.framework.storage.core.client;

import lombok.Data;

/**
 * 文件客户端配置 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 17:21
 */
@Data
public class FileClientConfig {

    /**
     * 是否自动拼接 traceId 前缀. 没有则使用 UUID
     */
    protected boolean nameAppendTraceId;

}
