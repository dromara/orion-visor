package com.orion.ops.module.infra.task;

import com.orion.ops.module.infra.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    private static final String LOCK_KEY = "tag:clear:lock";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private TagService tagService;

    /**
     * 定时清理未引用的 tag
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clear() {
        log.info("TagAutoClearTask.clear start");
        // 获取锁
        RLock lock = redissonClient.getLock(LOCK_KEY);
        // 未获取到直接返回
        if (!lock.tryLock()) {
            log.info("TagAutoClearTask.clear locked end");
            return;
        }
        try {
            // 清理
            tagService.clearUnusedTag();
            log.info("TagAutoClearTask.clear finish");
        } catch (Exception e) {
            log.error("TagAutoClearTask.clear error", e);
        } finally {
            lock.unlock();
        }
    }

}
