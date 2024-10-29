/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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

/**
 * 上传任务文件状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 22:21
 */
public enum UploadTaskFileStatusEnum {

    /**
     * 等待中
     */
    WAITING,

    /**
     * 上传中
     */
    UPLOADING,

    /**
     * 已完成
     */
    FINISHED,

    /**
     * 已失败
     */
    FAILED,

    /**
     * 已取消
     */
    CANCELED,

    ;

    public static UploadTaskFileStatusEnum of(String status) {
        if (status == null) {
            return null;
        }
        for (UploadTaskFileStatusEnum value : values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
