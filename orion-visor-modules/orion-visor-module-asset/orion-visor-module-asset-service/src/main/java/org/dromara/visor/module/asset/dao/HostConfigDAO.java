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
package org.dromara.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.module.asset.entity.domain.HostConfigDO;
import org.dromara.visor.module.asset.entity.po.HostTypeCountPO;

import java.util.List;

/**
 * 主机配置 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-3-6 10:59
 */
@Mapper
public interface HostConfigDAO extends IMapper<HostConfigDO> {

    /**
     * 通过 hostId 和 type 查询
     *
     * @param hostId hostId
     * @param type   type
     * @return config
     */
    default HostConfigDO selectByHostIdType(Long hostId, String type) {
        return this.of()
                .createWrapper()
                .eq(HostConfigDO::getHostId, hostId)
                .eq(HostConfigDO::getType, type)
                .then()
                .getOne();
    }

    /**
     * 通过 hostId 查询
     *
     * @param hostId hostId
     * @return config
     */
    default List<HostConfigDO> selectByHostId(Long hostId) {
        return this.of()
                .createWrapper()
                .eq(HostConfigDO::getHostId, hostId)
                .then()
                .list();
    }

    /**
     * 更新配置状态
     *
     * @param hostId hostId
     * @param types  types
     * @param status status
     */
    default void updateConfigStatus(Long hostId, List<String> types, String status) {
        HostConfigDO update = HostConfigDO.builder()
                .status(status)
                .build();
        LambdaQueryWrapper<HostConfigDO> condition = this.wrapper()
                .eq(HostConfigDO::getHostId, hostId)
                .in(HostConfigDO::getType, types)
                .ne(HostConfigDO::getStatus, status);
        this.update(update, condition);
    }

    /**
     * 更新配置状态
     *
     * @param hostId   hostId
     * @param notTypes notTypes
     * @param status   status
     */
    default void updateConfigInvertStatus(Long hostId, List<String> notTypes, String status) {
        HostConfigDO update = HostConfigDO.builder()
                .status(status)
                .build();
        LambdaQueryWrapper<HostConfigDO> condition = this.wrapper()
                .eq(HostConfigDO::getHostId, hostId)
                .notIn(HostConfigDO::getType, notTypes)
                .ne(HostConfigDO::getStatus, status);
        this.update(update, condition);
    }

    /**
     * 通过 hostId 删除
     *
     * @param hostIdList hostIdList
     * @return effect
     */
    default int deleteByHostIdList(List<Long> hostIdList) {
        return this.delete(Conditions.in(HostConfigDO::getHostId, hostIdList));
    }

    /**
     * 设置 keyId 为 NULL
     *
     * @param keyIdList keyIdList
     * @return effect
     */
    int setKeyIdWithNull(@Param("keyIdList") List<Long> keyIdList);

    /**
     * 设置 identityId 为 NULL
     *
     * @param identityIdList identityIdList
     * @return effect
     */
    int setIdentityIdWithNull(@Param("identityIdList") List<Long> identityIdList);

    /**
     * 查询启用的主机类型数量
     *
     * @return count
     */
    List<HostTypeCountPO> selectEnabledTypeCount();

}
