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
package org.dromara.visor.module.asset.handler.host.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 传输类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/12 12:41
 */
@Getter
@AllArgsConstructor
public enum TransferType {

    /**
     * 上传
     */
    UPLOAD("upload"),

    /**
     * 下载
     */
    DOWNLOAD("download"),

    ;

    private final String type;

    public static TransferType of(String type) {
        if (type == null) {
            return null;
        }
        for (TransferType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
