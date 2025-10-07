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
package org.dromara.visor.module.monitor.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.module.monitor.entity.domain.MonitorHostDO;
import org.dromara.visor.module.monitor.entity.po.MonitorHostCountPO;

import java.util.List;

/**
 * 监控主机 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Mapper
public interface MonitorHostDAO extends IMapper<MonitorHostDO> {

    /**
     * 通过 hostIdList 查询
     *
     * @param hostIdList hostIdList
     * @return rows
     */
    default List<MonitorHostDO> selectByHostIdList(List<Long> hostIdList) {
        return this.of()
                .createWrapper()
                .in(MonitorHostDO::getHostId, hostIdList)
                .then()
                .list();
    }

    /**
     * 通过 agentKey 查询
     *
     * @param agentKey agentKey
     * @return row
     */
    default MonitorHostDO selectByAgentKey(String agentKey) {
        return this.of()
                .createWrapper()
                .eq(MonitorHostDO::getAgentKey, agentKey)
                .then()
                .getOne();
    }

    /**
     * 通过 hostId 查询
     *
     * @param hostId hostId
     * @return row
     */
    default MonitorHostDO selectByHostId(Long hostId) {
        return this.of()
                .createWrapper()
                .eq(MonitorHostDO::getHostId, hostId)
                .then()
                .getOne();
    }

    /**
     * 通过 hostIdList 删除
     *
     * @param hostIdList hostIdList
     * @return effect
     */
    default int deleteByHostIdList(List<Long> hostIdList) {
        return this.delete(Conditions.in(MonitorHostDO::getHostId, hostIdList));
    }

    /**
     * 通过 policyId 查询
     *
     * @param policyId policyId
     * @return row
     */
    default List<MonitorHostDO> selectByPolicyId(Long policyId) {
        return this.of()
                .createWrapper()
                .eq(MonitorHostDO::getPolicyId, policyId)
                .then()
                .list();
    }

    /**
     * 设置 policyId 为 null
     *
     * @param policyId policyId
     * @return effect
     */
    default int setPolicyIdWithNull(Long policyId) {
        LambdaUpdateWrapper<MonitorHostDO> updateWrapper = Wrappers.<MonitorHostDO>lambdaUpdate()
                .set(MonitorHostDO::getPolicyId, null)
                .eq(MonitorHostDO::getPolicyId, policyId);
        return this.update(null, updateWrapper);
    }

    /**
     * 设置 policyId 为 null
     *
     * @param id id
     * @return effect
     */
    default int setPolicyIdWithNullById(Long id) {
        LambdaUpdateWrapper<MonitorHostDO> updateWrapper = Wrappers.<MonitorHostDO>lambdaUpdate()
                .set(MonitorHostDO::getPolicyId, null)
                .eq(MonitorHostDO::getId, id);
        return this.update(null, updateWrapper);
    }

    /**
     * 查询告警主机数量
     *
     * @param policyIdList policyIdList
     * @return rows
     */
    List<MonitorHostCountPO> selectPolicyHostCount(@Param("policyIdList") List<Long> policyIdList);

}
