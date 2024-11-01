/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package org.dromara.visor.module.asset.handler.host.exec.job;

import cn.orionsec.kit.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.constant.FieldConst;
import org.dromara.visor.module.asset.entity.request.exec.ExecJobTriggerRequest;
import org.dromara.visor.module.asset.service.ExecJobService;
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

    private static final ExecJobService execJobService = SpringHolder.getBean(ExecJobService.class);

    @Override
    public void execute(JobExecutionContext context) {
        long id = context.getMergedJobDataMap().getLong(FieldConst.KEY);
        log.info("ExecCommandJob.execute id: {}", id);
        // 执行命令
        ExecJobTriggerRequest request = ExecJobTriggerRequest.builder()
                .id(id)
                .userId(Const.SYSTEM_USER_ID)
                .username(Const.SYSTEM_USERNAME)
                .build();
        execJobService.triggerExecJob(request);
        // 清理日志上下文
        OperatorLogs.clear();
    }

}
