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
package org.dromara.visor.module.exec.handler.exec.job;

import cn.orionsec.kit.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.FieldConst;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.exec.dao.ExecJobDAO;
import org.dromara.visor.module.exec.entity.domain.ExecJobDO;
import org.dromara.visor.module.exec.entity.request.exec.ExecJobTriggerRequest;
import org.dromara.visor.module.exec.enums.ExecModeEnum;
import org.dromara.visor.module.exec.service.ExecJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 计划命令任务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/9 18:33
 */
@Slf4j
public class ExecCommandJob implements Job {

    private static final ExecJobDAO execJobDAO = SpringHolder.getBean(ExecJobDAO.class);

    private static final ExecJobService execJobService = SpringHolder.getBean(ExecJobService.class);

    @Override
    public void execute(JobExecutionContext context) {
        long id = context.getMergedJobDataMap().getLong(FieldConst.KEY);
        log.info("ExecCommandJob.execute id: {}", id);
        // 查询任务
        ExecJobDO job = execJobDAO.selectById(id);
        if (job == null) {
            log.info("ExecCommandJob.execute absent id: {}", id);
            return;
        }
        // 执行命令
        ExecJobTriggerRequest request = ExecJobTriggerRequest.builder()
                .id(id)
                .userId(job.getExecUserId())
                .username(job.getExecUsername())
                .execMode(ExecModeEnum.JOB.name())
                .build();
        execJobService.triggerExecJob(request, job);
        // 清理日志上下文
        OperatorLogs.clear();
    }

}
