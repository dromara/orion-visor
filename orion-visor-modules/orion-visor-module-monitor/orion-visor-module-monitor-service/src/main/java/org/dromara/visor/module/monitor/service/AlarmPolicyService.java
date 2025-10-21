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
package org.dromara.visor.module.monitor.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyDO;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyCopyRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyCreateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyQueryRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmPolicyVO;

import java.util.List;

/**
 * 监控告警策略 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 00:12
 */
public interface AlarmPolicyService {

    /**
     * 创建监控告警策略
     *
     * @param request request
     * @return id
     */
    Long createAlarmPolicy(AlarmPolicyCreateRequest request);

    /**
     * 复制监控告警策略
     *
     * @param request request
     * @return id
     */
    Long copyAlarmPolicy(AlarmPolicyCopyRequest request);

    /**
     * 更新监控告警策略
     *
     * @param request request
     * @return effect
     */
    Integer updateAlarmPolicyById(AlarmPolicyUpdateRequest request);

    /**
     * 查询监控告警策略
     *
     * @param id id
     * @return row
     */
    AlarmPolicyVO getAlarmPolicyById(Long id);

    /**
     * 通过缓存查询监控告警策略
     *
     * @param type type
     * @return rows
     */
    List<AlarmPolicyVO> getAlarmPolicyListByCache(String type);

    /**
     * 分页查询监控告警策略
     *
     * @param request request
     * @return rows
     */
    DataGrid<AlarmPolicyVO> getAlarmPolicyPage(AlarmPolicyQueryRequest request);

    /**
     * 删除监控告警策略
     *
     * @param id id
     * @return effect
     */
    Integer deleteAlarmPolicyById(Long id);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<AlarmPolicyDO> buildQueryWrapper(AlarmPolicyQueryRequest request);

}
