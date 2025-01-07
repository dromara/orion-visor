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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Valid;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.SqlUtils;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.infra.convert.OperatorLogConvert;
import org.dromara.visor.module.infra.dao.OperatorLogDAO;
import org.dromara.visor.module.infra.define.operator.AuthenticationOperatorType;
import org.dromara.visor.module.infra.entity.domain.OperatorLogDO;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogClearRequest;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import org.dromara.visor.module.infra.entity.vo.LoginHistoryVO;
import org.dromara.visor.module.infra.entity.vo.OperatorLogVO;
import org.dromara.visor.module.infra.service.OperatorLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Slf4j
@Service
public class OperatorLogServiceImpl implements OperatorLogService {

    @Resource
    private OperatorLogDAO operatorLogDAO;

    @Override
    public void addOperatorLog(OperatorLogModel model) {
        // 转换
        OperatorLogDO record = OperatorLogConvert.MAPPER.to(model);
        // 插入
        operatorLogDAO.insert(record);
    }

    @Override
    public DataGrid<OperatorLogVO> getOperatorLogPage(OperatorLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.buildQueryWrapper(request)
                .orderByDesc(OperatorLogDO::getId);
        // 查询
        return operatorLogDAO.of(wrapper)
                .page(request)
                .dataGrid(OperatorLogConvert.MAPPER::to);
    }

    @Override
    public Integer deleteOperatorLog(List<Long> idList) {
        log.info("OperatorLogService.deleteOperatorLog start {}", JSON.toJSONString(idList));
        int effect = operatorLogDAO.deleteBatchIds(idList);
        log.info("OperatorLogService.deleteOperatorLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public Long getOperatorLogCount(OperatorLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return operatorLogDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    public Integer clearOperatorLog(OperatorLogClearRequest request) {
        log.info("OperatorLogService.clearOperatorLog start {}", JSON.toJSONString(request));
        // 删除参数
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.buildQueryWrapper(request)
                .orderByAsc(OperatorLogDO::getId)
                .last(SqlUtils.limit(request.getLimit()));
        // 删除
        int effect = operatorLogDAO.delete(wrapper);
        log.info("OperatorLogService.clearOperatorLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public List<LoginHistoryVO> getLoginHistory(String username, Integer count) {
        Valid.inRange(count, 0, 100, ErrorMessage.PARAM_ERROR);
        // 查询
        return operatorLogDAO.of()
                .createWrapper()
                .eq(OperatorLogDO::getUsername, username)
                .eq(OperatorLogDO::getType, AuthenticationOperatorType.LOGIN)
                .orderByDesc(OperatorLogDO::getId)
                .then()
                .limit(count)
                .list(OperatorLogConvert.MAPPER::toLoginHistory);
    }

    @Override
    public LambdaQueryWrapper<OperatorLogDO> buildQueryWrapper(OperatorLogQueryRequest request) {
        return operatorLogDAO.wrapper()
                .eq(OperatorLogDO::getUserId, request.getUserId())
                .eq(OperatorLogDO::getUsername, request.getUsername())
                .eq(OperatorLogDO::getRiskLevel, request.getRiskLevel())
                .eq(OperatorLogDO::getModule, request.getModule())
                .eq(OperatorLogDO::getType, request.getType())
                .eq(OperatorLogDO::getResult, request.getResult())
                .ge(OperatorLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(OperatorLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1));
    }

}
