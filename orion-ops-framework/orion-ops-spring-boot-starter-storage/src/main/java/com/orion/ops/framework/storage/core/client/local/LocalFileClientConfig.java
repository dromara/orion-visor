package com.orion.ops.framework.storage.core.client.local;

import com.orion.ops.framework.storage.core.client.FileClientConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 本地文件客户端 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 17:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocalFileClientConfig extends FileClientConfig {

    /**
     * 存储路径
     * <p>
     * 无需 / 结尾
     */
    private String storagePath;

    /**
     * 基础路径
     * <p>
     * 无需 / 结尾
     */
    private String basePath;

    public LocalFileClientConfig() {
        this.storagePath = "";
        this.basePath = "";
    }

}
