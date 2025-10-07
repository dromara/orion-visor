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

import org.apache.ibatis.annotations.Mapper;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.module.asset.entity.domain.HostDO;

import java.util.Date;
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

    /**
     * 通过类型查询 hostId
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
                .eq(HostDO::getStatus, status)
                .like(HostDO::getTypes, type)
                .then()
                .list(HostDO::getId);
    }

    /**
     * 通过 agentKey 查询 id
     *
     * @param agentKey agentKey
     * @return id
     */
    default Long selectIdByAgentKey(String agentKey) {
        return this.of()
                .createWrapper()
                .select(HostDO::getId)
                .eq(HostDO::getAgentKey, agentKey)
                .then()
                .getOne(HostDO::getId);
    }

    /**
     * 通过 agentKey 查询 id
     *
     * @param agentKeys agentKeys
     * @return id
     */
    default List<HostDO> selectIdByAgentKeys(List<String> agentKeys) {
        return this.of()
                .createWrapper()
                .select(HostDO::getId, HostDO::getAgentKey)
                .in(HostDO::getAgentKey, agentKeys)
                .then()
                .list();
    }

    /**
     * 更新探针信息
     *
     * @param agentKeys agentKeys
     * @param update    update
     * @return effect
     */
    default int updateByAgentKeys(List<String> agentKeys, HostDO update) {
        update.setUpdateTime(new Date());
        // 更新
        return this.update(update, Conditions.in(HostDO::getAgentKey, agentKeys));
    }

}
