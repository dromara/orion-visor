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
package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.request.tag.TagCreateRequest;
import org.dromara.visor.module.infra.entity.vo.TagVO;

import java.util.List;

/**
 * 标签枚举 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
public interface TagService {

    /**
     * 创建标签枚举
     *
     * @param request request
     * @return id
     */
    Long createTag(TagCreateRequest request);

    /**
     * 查询标签枚举
     *
     * @param type type
     * @return rows
     */
    List<TagVO> getTagList(String type);

    /**
     * 通过 id 删除标签枚举
     *
     * @param id id
     * @return effect
     */
    Integer deleteTagById(Long id);

    /**
     * 通过 id 删除标签枚举
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteTagByIdList(List<Long> idList);

    /**
     * 清理未使用的 tag
     */
    void clearUnusedTag();

}
