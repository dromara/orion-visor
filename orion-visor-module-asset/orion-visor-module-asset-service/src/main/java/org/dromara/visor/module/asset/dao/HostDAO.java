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
package org.dromara.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.asset.entity.domain.HostDO;

import java.util.Arrays;
import java.util.List;

/**
 * 主机 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper
public interface HostDAO extends IMapper<HostDO> {

    List<SFunction<HostDO, ?>> BASE_COLUMN = Arrays.asList(
            HostDO::getId,
            HostDO::getType,
            HostDO::getOsType,
            HostDO::getName,
            HostDO::getCode,
            HostDO::getAddress,
            HostDO::getPort,
            HostDO::getStatus,
            HostDO::getCreateTime,
            HostDO::getUpdateTime,
            HostDO::getCreator,
            HostDO::getUpdater
    );

    /**
     * 通过 id 查询基本信息
     *
     * @param id id
     * @return id
     */
    default HostDO selectBaseById(Long id) {
        return this.of()
                .createWrapper()
                .select(BASE_COLUMN)
                .eq(HostDO::getId, id)
                .then()
                .getOne();
    }

    /**
     * 通过 id 查询基本信息
     *
     * @param idList idList
     * @return id
     */
    default List<HostDO> selectBaseByIdList(List<Long> idList) {
        return this.of()
                .createWrapper()
                .select(BASE_COLUMN)
                .in(HostDO::getId, idList)
                .then()
                .list();
    }

    /**
     * 获取的 hostId
     *
     * @param hostIdList hostIdList
     * @param type       type
     * @param status     status
     * @return hostId
     */
    default List<Long> getHostIdList(List<Long> hostIdList, String type, String status) {
        return this.of()
                .createWrapper(true)
                .select(HostDO::getId)
                .in(HostDO::getId, hostIdList)
                .eq(HostDO::getType, type)
                .eq(HostDO::getStatus, status)
                .then()
                .list(HostDO::getId);
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

}
