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
package org.dromara.visor.module.infra.enums;

/**
 * 收藏类型 枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 10:53
 */
public enum FavoriteTypeEnum {

    /**
     * 主机
     */
    HOST,

    ;

    public static FavoriteTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (FavoriteTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
