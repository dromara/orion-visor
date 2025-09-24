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
import org.dromara.visor.module.monitor.entity.domain.AlarmEventDO;
import org.dromara.visor.module.monitor.entity.dto.AlarmPolicyAlarmCountDTO;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventClearRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventHandleRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventQueryRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventSetFalseRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmEventVO;

import java.util.List;

/**
 * 监控告警记录 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-17 21:31
 */
public interface AlarmEventService {

    /**
     * 创建监控告警记录
     *
     * @param record record
     */
    void createAlarmEvent(AlarmEventDO record);

    /**
     * 处理告警记录
     *
     * @param request request
     * @return effect
     */
    Integer handleAlarmEvent(AlarmEventHandleRequest request);

    /**
     * 设置为误报
     *
     * @param request request
     * @return effect
     */
    Integer setAlarmEventFalse(AlarmEventSetFalseRequest request);

    /**
     * 分页查询监控告警记录
     *
     * @param request request
     * @return rows
     */
    DataGrid<AlarmEventVO> getAlarmEventPage(AlarmEventQueryRequest request);

    /**
     * 查询监控告警记录数量
     *
     * @param request request
     * @return count
     */
    Long getAlarmEventCount(AlarmEventQueryRequest request);

    /**
     * 删除监控告警记录
     *
     * @param id id
     * @return effect
     */
    Integer deleteAlarmEventById(Long id);

    /**
     * 批量删除监控告警记录
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteAlarmEventByIdList(List<Long> idList);

    /**
     * 清理监控告警记录
     *
     * @param request request
     * @return effect
     */
    Integer clearAlarmEvent(AlarmEventClearRequest request);

    /**
     * 获取策略告警记录数量
     *
     * @param policyIdList policyIdList
     * @param startDay     startDay
     * @param endDay       endDay
     * @return count
     */
    List<AlarmPolicyAlarmCountDTO> getPolicyEventCount(List<Long> policyIdList, int startDay, int endDay);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<AlarmEventDO> buildQueryWrapper(AlarmEventQueryRequest request);

}
