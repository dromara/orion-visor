/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.module.asset.handler.host.upload.task;

import cn.orionsec.kit.lang.able.SafeCloseable;
import org.dromara.visor.module.asset.handler.host.upload.uploader.IFileUploader;

import java.util.List;

/**
 * 上传任务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 12:28
 */
public interface IFileUploadTask extends Runnable, SafeCloseable {

    /**
     * 取消上传
     */
    void cancel();

    /**
     * 获取上传器
     *
     * @return uploader
     */
    List<IFileUploader> getUploaderList();

}
