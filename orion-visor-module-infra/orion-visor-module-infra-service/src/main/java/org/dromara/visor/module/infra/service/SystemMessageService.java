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
package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.request.message.SystemMessageCreateRequest;
import org.dromara.visor.module.infra.entity.request.message.SystemMessageQueryRequest;
import org.dromara.visor.module.infra.entity.vo.SystemMessageVO;

import java.util.List;
import java.util.Map;

/**
 * 系统消息 服务类
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
public interface SystemMessageService {

    /**
     * 创建系统消息
     *
     * @param request request
     * @return id
     */
    Long createSystemMessage(SystemMessageCreateRequest request);

    /**
     * 查询系统消息列表
     *
     * @param request request
     * @return rows
     */
    List<SystemMessageVO> getSystemMessageList(SystemMessageQueryRequest request);

    /**
     * 查询系统消息数量
     *
     * @param queryUnread queryUnread
     * @return rows
     */
    Map<String, Integer> getSystemMessageCount(Boolean queryUnread);

    /**
     * 查询是否有未读消息
     *
     * @return has
     */
    Boolean checkHasUnreadMessage();

    /**
     * 更新系统消息为已读
     *
     * @param id id
     * @return effect
     */
    Integer readSystemMessage(Long id);

    /**
     * 更新全部系统消息为已读
     *
     * @param classify classify
     * @return effect
     */
    Integer readAllSystemMessage(String classify);

    /**
     * 删除系统消息
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemMessageById(Long id);

    /**
     * 清理已读的系统消息
     *
     * @param classify classify
     * @return effect
     */
    Integer clearSystemMessage(String classify);

}
