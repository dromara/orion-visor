package com.orion.ops.framework.job.config;

import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;

/**
 * quartz 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/27 0:58
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_JOB_QUARTZ)
public class OrionQuartzAutoConfiguration {

    /**
     * @return 任务工厂
     */
    @Bean
    public SpringBeanJobFactory jobFactory() {
        return new SpringBeanJobFactory();
    }

    /**
     * @param dataSource dataSource
     * @param jobFactory jobFactory
     * @return 调度器工厂
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, SpringBeanJobFactory jobFactory) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        return factory;
    }

    /**
     * @param schedulerFactoryBean 调度器工厂
     * @return 调度器
     */
    @Bean(initMethod = "start")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }

}
