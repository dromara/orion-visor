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
package org.dromara.visor.framework.biz.push.core.message;

import org.dromara.visor.common.entity.PushUser;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 推送消息接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/18 18:42
 */
public interface PushMessage extends Serializable {

    /**
     * 获取模板
     *
     * @return 模板
     */
    String getTemplate();

    /**
     * 设置模板
     *
     * @param template template
     */
    void setTemplate(String template);

    /**
     * 获取参数
     *
     * @return 参数
     */
    Map<String, Object> getParams();

    /**
     * 设置参数
     *
     * @param params params
     */
    void setParams(Map<String, Object> params);

    /**
     * 获取推送用户
     *
     * @return 推送用户
     */
    List<PushUser> getPushUsers();

    /**
     * 设置推送用户列表
     *
     * @param pushUsers pushUsers
     */
    void setPushUsers(List<PushUser> pushUsers);

    /**
     * 获取推送渠道
     *
     * @return 推送渠道
     */
    PushChannelEnum getChannel();

}
