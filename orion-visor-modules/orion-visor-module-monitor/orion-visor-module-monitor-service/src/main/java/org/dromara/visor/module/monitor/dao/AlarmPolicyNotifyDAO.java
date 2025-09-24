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

import org.apache.ibatis.annotations.Mapper;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyNotifyDO;

import java.util.List;

/**
 * 监控告警策略通知 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 10:04
 */
@Mapper
public interface AlarmPolicyNotifyDAO extends IMapper<AlarmPolicyNotifyDO> {

    /**
     * 根据 policyId 查询
     *
     * @param policyId policyId
     * @return rules
     */
    default List<AlarmPolicyNotifyDO> selectByPolicyId(Long policyId) {
        return this.selectList(Conditions.eq(AlarmPolicyNotifyDO::getPolicyId, policyId));
    }

    /**
     * 通过 policyId 删除告警策略通知
     *
     * @param policyId policyId
     */
    default void deleteByPolicyId(Long policyId) {
        this.delete(Conditions.eq(AlarmPolicyNotifyDO::getPolicyId, policyId));
    }

    /**
     * 通过 notifyId 告警策略通知
     *
     * @param notifyId notifyId
     */
    default void deleteByNotifyId(Long notifyId) {
        this.delete(Conditions.eq(AlarmPolicyNotifyDO::getNotifyId, notifyId));
    }

}
