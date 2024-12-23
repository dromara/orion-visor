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
import org.dromara.visor.module.infra.entity.domain.FavoriteDO;

import java.util.List;

/**
 * 收藏 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Mapper
public interface FavoriteDAO extends IMapper<FavoriteDO> {

    /**
     * 通过 relId 删除收藏
     *
     * @param type   type
     * @param userId userId
     * @param relId  relId
     * @return effect
     */
    default int deleteFavorite(String type, Long userId, Long relId) {
        LambdaQueryWrapper<FavoriteDO> wrapper = this.lambda()
                .eq(FavoriteDO::getType, type)
                .eq(FavoriteDO::getUserId, userId)
                .eq(FavoriteDO::getRelId, relId);
        return this.delete(wrapper);
    }

    /**
     * 通过 relId 删除收藏
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    default int deleteFavoriteByRelId(String type, Long relId) {
        LambdaQueryWrapper<FavoriteDO> wrapper = this.lambda()
                .eq(FavoriteDO::getType, type)
                .eq(FavoriteDO::getRelId, relId);
        return this.delete(wrapper);
    }

    /**
     * 通过 relId 删除收藏
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    default int deleteFavoriteByRelIdList(String type, List<Long> relIdList) {
        LambdaQueryWrapper<FavoriteDO> wrapper = this.lambda()
                .eq(FavoriteDO::getType, type)
                .in(FavoriteDO::getRelId, relIdList);
        return this.delete(wrapper);
    }

    /**
     * 通过 userId 删除收藏
     *
     * @param userId userId
     * @return effect
     */
    default int deleteFavoriteByUserId(Long userId) {
        LambdaQueryWrapper<FavoriteDO> wrapper = this.lambda()
                .eq(FavoriteDO::getUserId, userId);
        return this.delete(wrapper);
    }

    /**
     * 通过 userId 删除收藏
     *
     * @param type       type
     * @param userIdList userIdList
     * @return effect
     */
    default int deleteFavoriteByUserIdList(List<Long> userIdList) {
        LambdaQueryWrapper<FavoriteDO> wrapper = this.lambda()
                .in(FavoriteDO::getUserId, userIdList);
        return this.delete(wrapper);
    }

}
