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
package org.dromara.visor.module.infra.enums;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.infra.handler.setting.model.EncryptSystemSettingModel;
import org.dromara.visor.module.infra.handler.setting.model.SftpSystemSettingModel;

import java.util.Map;

/**
 * 系统设置类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/9/27 19:07
 */
@Getter
@AllArgsConstructor
public enum SystemSettingTypeEnum {

    /**
     * SFTP 配置
     */
    SFTP("sftp", SftpSystemSettingModel.class),

    /**
     * 加密配置
     */
    ENCRYPT("encrypt", EncryptSystemSettingModel.class),

    ;

    private final String prefix;

    private final Class<?> modelClass;

    /**
     * 解析
     *
     * @param settings settings
     * @return model
     */
    @SuppressWarnings("unchecked")
    public <T> T parseModel(Map<String, String> settings) {
        return (T) JSON.parseObject(JSON.toJSONString(settings)).toJavaObject(modelClass);
    }

    public static SystemSettingTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (SystemSettingTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取 key
     *
     * @param item item
     * @return key
     */
    public String getConfigKey(String item) {
        return prefix + Const.DOT + item;
    }

}
