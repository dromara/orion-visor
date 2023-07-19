package com.orion.ops.framework.storage.core.client;

import com.orion.lang.utils.time.Dates;
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
     * 是否为默认客户端
     */
    protected boolean primary;

    /**
     * 是否启用
     */
    protected boolean enabled;

    /**
     * 是否使用时间戳作为文件名称前缀
     */
    protected boolean timestampPrefix;

    /**
     * 是否拼接时间作为文件夹
     */
    protected boolean dateDirectory = true;

    /**
     * 时间文件夹格式
     */
    protected String datePattern = Dates.YMD;

}
