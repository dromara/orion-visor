package com.orion.ops.framework.job.config;

import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 调度器配置
 * <p>
 * TODO 后面业务扩展需要加上quartz的配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 16:58
 */
@EnableScheduling
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_JOB)
public class OrionSchedulerAutoConfiguration {

    /**
     * @return 任务调度器
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.setThreadNamePrefix("scheduling-task-");
        return scheduler;
    }

}
