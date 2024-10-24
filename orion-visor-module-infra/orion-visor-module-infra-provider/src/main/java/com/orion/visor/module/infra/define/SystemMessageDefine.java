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
package com.orion.visor.module.infra.define;

import com.orion.lang.utils.Strings;
import com.orion.visor.module.infra.enums.MessageClassifyEnum;

import java.util.Map;

/**
 * 系统消息定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/14 17:06
 */
public interface SystemMessageDefine {

    /**
     * @return 消息分类
     */
    MessageClassifyEnum getClassify();

    /**
     * @return 消息类型
     */
    String getType();

    /**
     * @return 标题
     */
    String getTitle();

    /**
     * @return 内容
     */
    String getContent();

    /**
     * 格式化内容
     *
     * @param params params
     * @return content
     */
    default String formatContent(Map<String, Object> params) {
        return Strings.format(this.getContent(), params);
    }

}
