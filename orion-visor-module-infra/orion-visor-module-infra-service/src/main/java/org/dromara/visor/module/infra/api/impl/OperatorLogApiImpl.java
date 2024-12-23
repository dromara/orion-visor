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
package org.dromara.visor.module.infra.api.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.infra.api.OperatorLogApi;
import org.dromara.visor.module.infra.convert.OperatorLogProviderConvert;
import org.dromara.visor.module.infra.dao.OperatorLogDAO;
import org.dromara.visor.module.infra.entity.domain.OperatorLogDO;
import org.dromara.visor.module.infra.entity.dto.operator.OperatorLogDTO;
import org.dromara.visor.module.infra.entity.dto.operator.OperatorLogQueryDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/4 23:18
 */
@Slf4j
@Service
public class OperatorLogApiImpl implements OperatorLogApi {

    @Resource
    private OperatorLogDAO operatorLogDAO;

    @Override
    public DataGrid<OperatorLogDTO> getOperatorLogPage(OperatorLogQueryDTO request) {
        Valid.valid(request);
        // 条件
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return operatorLogDAO.of()
                .page(request)
                .wrapper(wrapper)
                .dataGrid(OperatorLogProviderConvert.MAPPER::to);
    }

    @Override
    public Integer deleteOperatorLog(List<Long> idList) {
        return operatorLogDAO.deleteBatchIds(idList);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<OperatorLogDO> buildQueryWrapper(OperatorLogQueryDTO request) {
        return operatorLogDAO.wrapper()
                .eq(OperatorLogDO::getId, request.getId())
                .eq(OperatorLogDO::getUserId, request.getUserId())
                .eq(OperatorLogDO::getRiskLevel, request.getRiskLevel())
                .eq(OperatorLogDO::getModule, request.getModule())
                .eq(OperatorLogDO::getType, request.getType())
                .in(OperatorLogDO::getType, request.getTypeList())
                .eq(OperatorLogDO::getResult, request.getResult())
                .like(OperatorLogDO::getExtra, request.getExtra())
                .ge(OperatorLogDO::getStartTime, request.getStartTimeStart())
                .le(OperatorLogDO::getStartTime, request.getStartTimeEnd())
                .orderByDesc(OperatorLogDO::getId);
    }

}
