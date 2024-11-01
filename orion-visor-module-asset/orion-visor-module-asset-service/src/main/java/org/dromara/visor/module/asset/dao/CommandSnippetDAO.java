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
package org.dromara.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.asset.entity.domain.CommandSnippetDO;

import java.util.List;

/**
 * 命令片段 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Mapper
public interface CommandSnippetDAO extends IMapper<CommandSnippetDO> {

    /**
     * 设置 groupId 为 null
     *
     * @param groupId groupId
     * @return effect
     */
    default int setGroupIdWithNull(Long groupId) {
        LambdaUpdateWrapper<CommandSnippetDO> wrapper = Wrappers.<CommandSnippetDO>lambdaUpdate()
                .set(CommandSnippetDO::getGroupId, null)
                .eq(CommandSnippetDO::getGroupId, groupId);
        return this.update(null, wrapper);
    }

    /**
     * 通过 groupId 删除
     *
     * @param groupId groupId
     * @return effect
     */
    default int deleteByGroupId(Long groupId) {
        LambdaQueryWrapper<CommandSnippetDO> wrapper = this.lambda()
                .eq(CommandSnippetDO::getGroupId, groupId);
        return this.delete(wrapper);
    }

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    default int deleteByUserIdList(List<Long> userIdList) {
        LambdaQueryWrapper<CommandSnippetDO> wrapper = this.lambda()
                .in(CommandSnippetDO::getUserId, userIdList);
        return this.delete(wrapper);
    }

}
