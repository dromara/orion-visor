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
package org.dromara.visor.framework.job.core.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.collect.Maps;
import org.dromara.visor.framework.common.constant.FieldConst;
import org.quartz.*;

import java.util.Map;

/**
 * quartz 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/27 17:14
 */
public class QuartzUtils {

    private static final String JOB_PREFIX = "Job_";

    private static final String TRIGGER_PREFIX = "Trigger_";

    private static Scheduler scheduler;

    private QuartzUtils() {
    }

    /**
     * 添加任务
     *
     * @param type     type
     * @param key      key
     * @param cron     cron
     * @param desc     desc
     * @param jobClass jobClass
     */
    public static void addJob(String type, Object key,
                              String cron, String desc,
                              Class<? extends Job> jobClass) {
        QuartzUtils.addJob(type, key,
                cron, desc,
                jobClass, Maps.newMap());
    }

    /**
     * 添加任务
     *
     * @param type     type
     * @param key      key
     * @param cron     cron
     * @param desc     desc
     * @param jobClass jobClass
     * @param params   params
     */
    public static void addJob(String type, Object key,
                              String cron, String desc,
                              Class<? extends Job> jobClass,
                              Map<String, Object> params) {
        params.put(FieldConst.KEY, key);
        // 生成 job
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withDescription(desc)
                .withIdentity(getJobKey(type, key))
                .usingJobData(new JobDataMap(params))
                .storeDurably()
                .build();
        // 生成触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(type, key))
                .withSchedule(CronScheduleBuilder
                        .cronSchedule(cron)
                        // 补偿一次
                        // .withMisfireHandlingInstructionFireAndProceed()
                        // 不补偿
                        .withMisfireHandlingInstructionDoNothing())
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
            throw Exceptions.task(e);
        }
    }

    /**
     * 删除任务
     *
     * @param type type
     * @param key  key
     */
    public static void deleteJob(String type, Object key) {
        try {
            scheduler.deleteJob(getJobKey(type, key));
        } catch (Exception e) {
            throw Exceptions.task(e);
        }
    }

    /**
     * 暂停任务
     *
     * @param type type
     * @param key  key
     */
    public static void pauseJob(String type, Object key) {
        try {
            scheduler.pauseJob(getJobKey(type, key));
        } catch (Exception e) {
            throw Exceptions.task(e);
        }
    }

    /**
     * 恢复任务
     *
     * @param type type
     * @param key  key
     */
    public static void resumeJob(String type, Object key) {
        try {
            scheduler.resumeJob(getJobKey(type, key));
        } catch (Exception e) {
            throw Exceptions.task(e);
        }
    }

    /**
     * 立即执行任务
     *
     * @param type type
     * @param key  key
     */
    public static void triggerJob(String type, Object key) {
        triggerJob(type, key, Maps.newMap());
    }

    /**
     * 立即执行任务
     *
     * @param type   type
     * @param key    key
     * @param params params
     */
    public static void triggerJob(String type, Object key, Map<String, Object> params) {
        try {
            params.put(FieldConst.KEY, key);
            scheduler.triggerJob(getJobKey(type, key), new JobDataMap(params));
        } catch (Exception e) {
            throw Exceptions.task(e);
        }
    }

    /**
     * 获取 jobKey
     *
     * @param type type
     * @param key  key
     * @return jobKey
     */
    private static JobKey getJobKey(String type, Object key) {
        return new JobKey(JOB_PREFIX + type + "_" + Objects1.toString(key), JOB_PREFIX + type);
    }

    /**
     * 获取 triggerKey
     *
     * @param type type
     * @param key  key
     * @return triggerKey
     */
    private static TriggerKey getTriggerKey(String type, Object key) {
        return new TriggerKey(TRIGGER_PREFIX + type + "_" + Objects1.toString(key), TRIGGER_PREFIX + type);
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
