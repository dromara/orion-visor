package com.orion.visor.module.asset.handler.host.exec.job;

import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.FieldConst;
import com.orion.visor.module.asset.entity.request.exec.ExecJobTriggerRequest;
import com.orion.visor.module.asset.service.ExecJobService;
import com.orion.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
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
