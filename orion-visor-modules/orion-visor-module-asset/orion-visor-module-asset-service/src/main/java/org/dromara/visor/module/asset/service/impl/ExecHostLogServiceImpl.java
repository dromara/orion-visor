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
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.asset.convert.ExecHostLogConvert;
import org.dromara.visor.module.asset.dao.ExecHostLogDAO;
import org.dromara.visor.module.asset.entity.domain.ExecHostLogDO;
import org.dromara.visor.module.asset.entity.vo.ExecHostLogVO;
import org.dromara.visor.module.asset.handler.host.exec.command.handler.IExecCommandHandler;
import org.dromara.visor.module.asset.handler.host.exec.command.handler.IExecTaskHandler;
import org.dromara.visor.module.asset.handler.host.exec.command.manager.ExecTaskManager;
import org.dromara.visor.module.asset.service.ExecHostLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 批量执行主机日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
@Slf4j
@Service
public class ExecHostLogServiceImpl implements ExecHostLogService {

    @Resource
    private ExecHostLogDAO execHostLogDAO;

    @Resource
    private ExecTaskManager execTaskManager;

    @Override
    public List<ExecHostLogVO> getExecHostLogList(Long logId) {
        return execHostLogDAO.of()
                .createWrapper()
                .eq(ExecHostLogDO::getLogId, logId)
                .then()
                .list(ExecHostLogConvert.MAPPER::to);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExecHostLogByLogId(List<Long> logIdList) {
        log.info("ExecHostLogService-deleteExecHostLogByLogId logIdList: {}", logIdList);
        int effect = 0;
        if (Lists.isEmpty(logIdList)) {
            return effect;
        }
        // 分批次删除
        List<List<Long>> partitions = Lists.partition(logIdList, 500);
        for (List<Long> batch : partitions) {
            LambdaQueryWrapper<ExecHostLogDO> wrapper = execHostLogDAO.wrapper()
                    .in(ExecHostLogDO::getLogId, batch);
            effect += execHostLogDAO.delete(wrapper);
        }
        log.info("ExecHostLogService-deleteExecHostLogByLogId effect: {}", logIdList);
        return effect;
    }

    @Override
    public Integer deleteExecHostLogById(Long id) {
        log.info("ExecHostLogService-deleteExecHostLogById id: {}", id);
        // 检查数据是否存在
        ExecHostLogDO record = execHostLogDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 中断
        Optional.ofNullable(record.getLogId())
                .map(execTaskManager::getTask)
                .map(IExecTaskHandler::getHandlers)
                .flatMap(s -> s.stream()
                        .filter(h -> h.getHostId().equals(record.getHostId()))
                        .findFirst())
                .ifPresent(IExecCommandHandler::interrupt);
        // 删除
        int effect = execHostLogDAO.deleteById(id);
        log.info("ExecHostLogService-deleteExecHostLogById id: {}, effect: {}", id, effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.LOG_ID, record.getLogId());
        OperatorLogs.add(OperatorLogs.HOST_NAME, record.getHostName());
        return effect;
    }

}
