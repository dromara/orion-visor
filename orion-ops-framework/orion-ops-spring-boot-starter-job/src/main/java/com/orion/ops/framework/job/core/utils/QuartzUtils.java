package com.orion.ops.framework.job.core.utils;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.collect.Maps;
import org.quartz.*;
import org.quartz.spi.MutableTrigger;

import java.util.Map;

/**
 * quartz 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/27 17:14
 */
public class QuartzUtils {

    private static Scheduler scheduler;

    private QuartzUtils() {
    }

    /**
     * 添加任务
     *
     * @param name     name
     * @param group    group
     * @param cron     cron
     * @param jobClass jobClass
     */
    public static void addJob(String name, String group,
                              String cron, Class<? extends Job> jobClass) {
        QuartzUtils.addJob(name, group,
                cron, jobClass,
                Maps.empty());
    }

    /**
     * 添加任务
     *
     * @param name     name
     * @param group    group
     * @param cron     cron
     * @param jobClass jobClass
     * @param params   params
     */
    public static void addJob(String name, String group,
                              String cron, Class<? extends Job> jobClass,
                              Map<Object, Object> params) {
        // 生成 job
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(name, group)
                .usingJobData(new JobDataMap(params))
                .storeDurably()
                .build();
        // 生成触发器
        MutableTrigger trigger = CronScheduleBuilder.cronSchedule(cron)
                .withMisfireHandlingInstructionDoNothing()
                .build();
        QuartzUtils.addJob(jobDetail, trigger);
    }

    /**
     * 添加任务
     *
     * @param jobDetail jobDetail
     * @param trigger   trigger
     */
    public static void addJob(JobDetail jobDetail, Trigger trigger) {
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw Exceptions.task();
        }
    }

    /**
     * 删除任务
     *
     * @param name  name
     * @param group group
     */
    public static void deleteJob(String name, String group) {
        try {
            scheduler.deleteJob(new JobKey(name, group));
        } catch (Exception e) {
            throw Exceptions.task();
        }
    }

    /**
     * 暂停任务
     *
     * @param name  name
     * @param group group
     */
    public static void pauseJob(String name, String group) {
        try {
            scheduler.pauseJob(new JobKey(name, group));
        } catch (Exception e) {
            throw Exceptions.task();
        }
    }

    /**
     * 恢复任务
     *
     * @param name  name
     * @param group group
     */
    public static void resumeJob(String name, String group) {
        try {
            scheduler.resumeJob(new JobKey(name, group));
        } catch (Exception e) {
            throw Exceptions.task();
        }
    }

    /**
     * 立即执行任务
     *
     * @param name  name
     * @param group group
     */
    public static void triggerJob(String name, String group) {
        try {
            scheduler.triggerJob(new JobKey(name, group));
        } catch (Exception e) {
            throw Exceptions.task();
        }
    }

    /**
     * 设置调度器
     *
     * @param scheduler scheduler
     */
    public static void setScheduler(Scheduler scheduler) {
        if (QuartzUtils.scheduler != null) {
            // unmodified
            throw Exceptions.state();
        }
        QuartzUtils.scheduler = scheduler;
    }

}
