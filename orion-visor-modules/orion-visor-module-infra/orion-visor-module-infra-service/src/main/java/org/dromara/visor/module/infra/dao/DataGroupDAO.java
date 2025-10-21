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
package org.dromara.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.infra.entity.domain.DataGroupDO;

import java.util.List;

/**
 * 数据分组 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupDAO extends IMapper<DataGroupDO> {

    /**
     * 查询最大排序
     *
     * @param parentId parentId
     * @param type     type
     * @param userId   userId
     * @return max(sort)
     */
    Integer selectMaxSort(@Param("parentId") Long parentId,
                          @Param("type") String type,
                          @Param("userId") Long userId);

    /**
     * 修改排序
     *
     * @param parentId  parentId
     * @param type      type
     * @param userId    userId
     * @param condition 条件
     * @param referSort 对比值
     * @param addition  自增步长
     * @return effect
     */
    Integer updateSort(@Param("parentId") Long parentId,
                       @Param("type") String type,
                       @Param("userId") Long userId,
                       @Param("condition") String condition,
                       @Param("referSort") Integer referSort,
                       @Param("addition") Integer addition);

    /**
     * 通过 parentId 查询
     *
     * @param parentIdList parentIdList
     * @return rows
     */
    default List<DataGroupDO> selectByParentId(List<Long> parentIdList) {
        LambdaQueryWrapper<DataGroupDO> wrapper = this.lambda()
                .in(DataGroupDO::getParentId, parentIdList);
        return this.selectList(wrapper);
    }

}
