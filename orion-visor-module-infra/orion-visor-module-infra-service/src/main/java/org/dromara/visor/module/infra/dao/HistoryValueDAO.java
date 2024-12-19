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
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.infra.entity.domain.HistoryValueDO;

import java.util.List;

/**
 * 历史归档 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Mapper
public interface HistoryValueDAO extends IMapper<HistoryValueDO> {

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    default int deleteByRelId(String type, Long relId) {
        LambdaQueryWrapper<HistoryValueDO> wrapper = this.lambda()
                .eq(HistoryValueDO::getType, type)
                .eq(HistoryValueDO::getRelId, relId);
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
        LambdaQueryWrapper<HistoryValueDO> wrapper = this.lambda()
                .eq(HistoryValueDO::getType, type)
                .in(HistoryValueDO::getRelId, relIdList);
        return this.delete(wrapper);
    }

}
