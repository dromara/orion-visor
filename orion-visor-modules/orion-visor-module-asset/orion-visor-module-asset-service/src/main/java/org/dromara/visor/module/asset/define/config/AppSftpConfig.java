/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.asset.define.config;

import org.dromara.visor.common.config.ConfigRef;
import org.dromara.visor.common.config.ConfigStore;
import org.dromara.visor.common.constant.ConfigKeys;
import org.springframework.stereotype.Component;

/**
 * 应用 sftp 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 22:00
 */
@Component
public class AppSftpConfig {

    /**
     * 文件预览大小
     */
    private final ConfigRef<Integer> previewSize;

    /**
     * 重复文件备份
     */
    private final ConfigRef<Boolean> uploadPresentBackup;

    /**
     * 备份文件名称
     */
    private final ConfigRef<String> uploadBackupFileName;

    public AppSftpConfig(ConfigStore configStore) {
        this.previewSize = configStore.int32(ConfigKeys.SFTP_PREVIEW_SIZE);
        this.uploadPresentBackup = configStore.bool(ConfigKeys.SFTP_UPLOAD_PRESENT_BACKUP);
        this.uploadBackupFileName = configStore.string(ConfigKeys.SFTP_UPLOAD_BACKUP_FILE_NAME);
    }

    public Integer getPreviewSize() {
        return previewSize.value;
    }

    public Boolean getUploadPresentBackup() {
        return uploadPresentBackup.value;
    }

    public String getUploadBackupFileName() {
        return uploadBackupFileName.value;
    }

}
