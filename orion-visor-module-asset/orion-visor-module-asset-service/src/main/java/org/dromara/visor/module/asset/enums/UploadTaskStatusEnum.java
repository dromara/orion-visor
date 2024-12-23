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
package org.dromara.visor.module.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传任务状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 22:21
 */
@Getter
@AllArgsConstructor
public enum UploadTaskStatusEnum {

    /**
     * 等待中
     */
    WAITING(true),

    /**
     * 上传中
     */
    UPLOADING(true),

    /**
     * 已完成
     */
    FINISHED(false),

    /**
     * 已失败
     */
    FAILED(false),

    /**
     * 已取消
     */
    CANCELED(false),

    ;

    private final boolean cancelable;

    public static UploadTaskStatusEnum of(String status) {
        if (status == null) {
            return null;
        }
        for (UploadTaskStatusEnum value : values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
