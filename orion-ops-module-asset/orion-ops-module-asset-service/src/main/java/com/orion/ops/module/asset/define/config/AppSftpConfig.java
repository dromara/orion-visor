package com.orion.ops.module.asset.define.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用 sftp 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 22:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.sftp")
public class AppSftpConfig {

    /**
     * 上传文件时 文件存在是否备份
     */
    private Boolean uploadPresentBackup;

    /**
     * 备份文件名称
     */
    private String backupFileName;

    public AppSftpConfig() {
        this.uploadPresentBackup = true;
        this.backupFileName = "bk_${fileName}_${timestamp}";
    }

}
