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
package org.dromara.visor.module.asset.handler.host.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 传输响应类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:03
 */
@Getter
@AllArgsConstructor
public enum TransferReceiver {

    /**
     * 请求下一分片
     */
    NEXT_PART("nextPart"),

    /**
     * 开始
     */
    START("start"),

    /**
     * 进度
     */
    PROGRESS("progress"),

    /**
     * 完成
     */
    FINISH("finish"),

    /**
     * 失败
     */
    ERROR("error"),

    /**
     * 关闭
     */
    ABORT("abort"),

    ;

    private final String type;

    public static TransferReceiver of(String type) {
        if (type == null) {
            return null;
        }
        for (TransferReceiver value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
