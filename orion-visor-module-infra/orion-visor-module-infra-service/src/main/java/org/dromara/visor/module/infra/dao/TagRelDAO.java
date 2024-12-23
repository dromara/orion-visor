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
package org.dromara.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.infra.entity.domain.TagRelDO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签引用 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
@Mapper
public interface TagRelDAO extends IMapper<TagRelDO> {

    /**
     * 查询 tag 关联的所有 id
     *
     * @param tagId tagId
     * @return rel
     */
    default List<Long> selectRelIdByTagId(Long tagId) {
        LambdaQueryWrapper<TagRelDO> wrapper = this.lambda()
                .select(TagRelDO::getRelId)
                .eq(TagRelDO::getTagId, tagId);
        return this.selectList(wrapper)
                .stream()
                .map(TagRelDO::getRelId)
                .collect(Collectors.toList());
    }

    /**
     * 查询 tag 关联的所有 id
     *
     * @param tagIdList tagIdList
     * @return rel
     */
    default List<Long> selectRelIdByTagId(List<Long> tagIdList) {
        LambdaQueryWrapper<TagRelDO> wrapper = this.lambda()
                .select(TagRelDO::getRelId)
                .in(TagRelDO::getTagId, tagIdList);
        return this.selectList(wrapper)
                .stream()
                .map(TagRelDO::getRelId)
                .collect(Collectors.toList());
    }

}
