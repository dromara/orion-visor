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
package com.orion.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.PathBookmarkDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 路径标签 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Mapper
public interface PathBookmarkDAO extends IMapper<PathBookmarkDO> {

    /**
     * 设置 groupId 为 null
     *
     * @param groupId groupId
     * @return effect
     */
    default int setGroupIdWithNull(Long groupId) {
        LambdaUpdateWrapper<PathBookmarkDO> wrapper = Wrappers.<PathBookmarkDO>lambdaUpdate()
                .set(PathBookmarkDO::getGroupId, null)
                .eq(PathBookmarkDO::getGroupId, groupId);
        return this.update(null, wrapper);
    }

    /**
     * 通过 groupId 删除
     *
     * @param groupId groupId
     * @return effect
     */
    default int deleteByGroupId(Long groupId) {
        LambdaQueryWrapper<PathBookmarkDO> wrapper = this.lambda()
                .eq(PathBookmarkDO::getGroupId, groupId);
        return this.delete(wrapper);
    }

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    default int deleteByUserIdList(List<Long> userIdList) {
        LambdaQueryWrapper<PathBookmarkDO> wrapper = this.lambda()
                .in(PathBookmarkDO::getUserId, userIdList);
        return this.delete(wrapper);
    }

}
