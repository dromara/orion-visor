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
package com.orion.visor.module.infra.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.framework.mybatis.core.query.Conditions;
import com.orion.visor.module.infra.entity.domain.PreferenceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户偏好 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
@Mapper
public interface PreferenceDAO extends IMapper<PreferenceDO> {

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    default int deleteByUserId(Long userId) {
        return this.delete(Conditions.eq(PreferenceDO::getUserId, userId));
    }

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    default int deleteByUserIdList(List<Long> userIdList) {
        return this.delete(Conditions.in(PreferenceDO::getUserId, userIdList));
    }

}
