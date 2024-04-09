package com.orion.ops.module.asset.handler.host.exec.job;

import com.orion.ops.framework.common.constant.FieldConst;
import com.orion.ops.module.asset.service.ExecJobService;
import com.orion.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 计划执行命令任务
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
        long jobId = context.getMergedJobDataMap().getLong(FieldConst.KEY);
        // TODO
    }

}
