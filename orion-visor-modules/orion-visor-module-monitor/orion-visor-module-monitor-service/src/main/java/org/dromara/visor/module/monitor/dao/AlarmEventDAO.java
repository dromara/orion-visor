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
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.monitor.entity.domain.AlarmEventDO;
import org.dromara.visor.module.monitor.entity.po.AlarmEventCountPO;

import java.util.Date;
import java.util.List;

/**
 * 监控告警记录 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-17 21:31
 */
@Mapper
public interface AlarmEventDAO extends IMapper<AlarmEventDO> {

    /**
     * 查询策略告警数量
     *
     * @param policyIdList    policyIdList
     * @param createTimeStart createTimeStart
     * @param createTimeEnd   createTimeEnd
     * @return count
     */
    List<AlarmEventCountPO> selectPolicyEventCount(@Param("policyIdList") List<Long> policyIdList,
                                                   @Param("createTimeStart") Date createTimeStart,
                                                   @Param("createTimeEnd") Date createTimeEnd);

}
