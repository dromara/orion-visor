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

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.asset.entity.dto.host.HostRdpConfigDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostSshConfigDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostVncConfigDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 主机类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/12 18:12
 */
@AllArgsConstructor
public enum HostTypeEnum {

    /**
     * SSH
     */
    SSH(HostSshConfigDTO.class),

    /**
     * RDP
     */
    RDP(HostRdpConfigDTO.class),

    /**
     * VNC
     */
    VNC(HostVncConfigDTO.class),

    ;

    private final Class<?> clazz;

    public static HostTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取类型
     *
     * @param types types
     * @return types
     */
    public static List<String> split(String types) {
        if (types == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(types.split(Const.COMMA))
                .map(HostTypeEnum::of)
                .filter(Objects::nonNull)
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * 是否包含此类型
     *
     * @param types types
     * @return contains
     */
    public boolean contains(String types) {
        return split(types).contains(this.name());
    }

    @SuppressWarnings("unchecked")
    public <T> T parse(String config) {
        return (T) JSON.parseObject(config, this.clazz);
    }

}
