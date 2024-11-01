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
package org.dromara.visor.module.infra.define.message;

import lombok.Getter;
import org.dromara.visor.module.infra.define.SystemMessageDefine;
import org.dromara.visor.module.infra.enums.MessageClassifyEnum;

/**
 * 用户 系统消息定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/14 17:23
 */
@Getter
public enum SystemUserMessageDefine implements SystemMessageDefine {

    /**
     * 登录失败
     */
    LOGIN_FAILED(MessageClassifyEnum.NOTICE,
            "登录失败",
            "您的账号在 <sb>${time}</sb> 登录系统时身份认证失败, 您的密码可能已经泄漏。如非本人操作请尽快修改密码。(<sb>${address} - ${location}</sb>)"),

    ;

    SystemUserMessageDefine(MessageClassifyEnum classify, String title, String content) {
        this.classify = classify;
        this.type = this.name();
        this.title = title;
        this.content = content;
    }

    /**
     * 消息分类
     */
    private final MessageClassifyEnum classify;

    /**
     * 消息类型
     */
    private final String type;

    /**
     * 标题
     */
    private final String title;

    /**
     * 内容
     */
    private final String content;

}
