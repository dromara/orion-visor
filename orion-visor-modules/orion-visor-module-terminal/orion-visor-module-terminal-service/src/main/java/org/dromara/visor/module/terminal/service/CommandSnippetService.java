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
package org.dromara.visor.module.terminal.service;

import org.dromara.visor.module.terminal.entity.request.snippet.CommandSnippetCreateRequest;
import org.dromara.visor.module.terminal.entity.request.snippet.CommandSnippetUpdateRequest;
import org.dromara.visor.module.terminal.entity.vo.CommandSnippetVO;
import org.dromara.visor.module.terminal.entity.vo.CommandSnippetWrapperVO;

import java.util.List;

/**
 * 命令片段 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
public interface CommandSnippetService {

    /**
     * 创建命令片段
     *
     * @param request request
     * @return id
     */
    Long createCommandSnippet(CommandSnippetCreateRequest request);

    /**
     * 更新命令片段
     *
     * @param request request
     * @return effect
     */
    Integer updateCommandSnippetById(CommandSnippetUpdateRequest request);

    /**
     * 查询命令片段
     *
     * @return rows
     */
    CommandSnippetWrapperVO getCommandSnippet();

    /**
     * 查询全部命令片段
     *
     * @return rows
     */
    List<CommandSnippetVO> getCommandSnippetList();

    /**
     * 设置分组为 null
     *
     * @param userId  userId
     * @param groupId groupId
     * @return effect
     */
    Integer setGroupNull(Long userId, Long groupId);

    /**
     * 删除命令片段
     *
     * @param id id
     * @return effect
     */
    Integer deleteCommandSnippetById(Long id);

    /**
     * 通过 groupId 删除
     *
     * @param userId  userId
     * @param groupId groupId
     * @return effect
     */
    Integer deleteByGroupId(Long userId, Long groupId);

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    Integer deleteByUserIdList(List<Long> userIdList);

}
