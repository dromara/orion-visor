/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.utils;

import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.sftp.SftpFile;
import com.alibaba.fastjson.JSON;
import org.dromara.visor.module.asset.define.config.AppSftpConfig;
import org.dromara.visor.module.asset.handler.host.transfer.model.SftpFileBackupParams;

/**
 * sftp 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 16:17
 */
public class SftpUtils {

    private SftpUtils() {
    }

    /**
     * 检查上传文件是否存在 并且执行响应策略
     *
     * @param config   config
     * @param executor executor
     * @param path     path
     */
    public static void checkUploadFilePresent(AppSftpConfig config, SftpExecutor executor, String path) {
        // 重复不备份
        if (!Booleans.isTrue(config.getUploadPresentBackup())) {
            return;
        }
        // 检查文件是否存在
        SftpFile file = executor.getFile(path);
        if (file != null) {
            // 文件存在则备份
            SftpFileBackupParams backupParams = new SftpFileBackupParams(file.getName(), System.currentTimeMillis());
            String target = Strings.format(config.getBackupFileName(), JSON.parseObject(JSON.toJSONString(backupParams)));
            // 移动
            executor.move(path, target);
        }
    }

}
