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
package org.dromara.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.infra.entity.domain.DataExtraDO;

import java.util.List;

/**
 * 数据拓展信息 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Mapper
public interface DataExtraDAO extends IMapper<DataExtraDO> {

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    default int deleteByUserId(Long userId) {
        LambdaQueryWrapper<DataExtraDO> wrapper = this.lambda()
                .eq(DataExtraDO::getUserId, userId);
        return this.delete(wrapper);
    }

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    default int deleteByUserIdList(List<Long> userIdList) {
        LambdaQueryWrapper<DataExtraDO> wrapper = this.lambda()
                .in(DataExtraDO::getUserId, userIdList);
        return this.delete(wrapper);
    }

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    default int deleteByRelId(String type, Long relId) {
        LambdaQueryWrapper<DataExtraDO> wrapper = this.lambda()
                .eq(DataExtraDO::getType, type)
                .eq(DataExtraDO::getRelId, relId);
        return this.delete(wrapper);
    }

    /**
     * 通过 relId 删除
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    default int deleteByRelIdList(String type, List<Long> relIdList) {
        LambdaQueryWrapper<DataExtraDO> wrapper = this.lambda()
                .eq(DataExtraDO::getType, type)
                .in(DataExtraDO::getRelId, relIdList);
        return this.delete(wrapper);
    }

    /**
     * 删除主机密钥
     *
     * @param keyIdList keyIdList
     * @return effect
     */
    int deleteHostKey(@Param("keyIdList") List<Long> keyIdList);

    /**
     * 删除主机身份
     *
     * @param identityIdList identityIdList
     * @return effect
     */
    int deleteHostIdentity(@Param("identityIdList") List<Long> identityIdList);

}
