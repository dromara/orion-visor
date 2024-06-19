package com.orion.visor.module.infra.task;

import com.orion.visor.framework.common.utils.LockerUtils;
import com.orion.visor.module.infra.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * tag 定时清理任务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 23:50
 */
@Slf4j
@Component
public class TagAutoClearTask {

    /**
     * 分布式锁名称
     */
    private static final String LOCK_KEY = "clear:tag:lock";

    @Resource
    private TagService tagService;

    /**
     * 清理
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clear() {
        log.info("TagAutoClearTask.clear start");
        // 获取锁并执行
        LockerUtils.tryLock(LOCK_KEY, tagService::clearUnusedTag);
        log.info("TagAutoClearTask.clear finish");
    }

}
