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
package org.dromara.visor.framework.storage.core.client;

import cn.orionsec.kit.lang.utils.time.Dates;
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
    protected boolean dateDirectory;

    /**
     * 时间文件夹格式
     */
    protected String datePattern;

    public FileClientConfig() {
        this.datePattern = Dates.YMD;
    }

}
