package com.orion.visor.module.asset.task;

import com.orion.visor.framework.common.utils.LockerUtils;
import com.orion.visor.module.asset.service.CommandSnippetGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时清理未使用的命令片段分组
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/24 23:50
 */
@Slf4j
@Component
public class CommandSnippetGroupAutoClearTask {

    /**
     * 分布式锁名称
     */
    private static final String LOCK_KEY = "clear:csg:lock";

    @Resource
    private CommandSnippetGroupService commandSnippetGroupService;

    /**
     * 清理
     */
    @Scheduled(cron = "0 10 2 * * ?")
    public void clear() {
        log.info("CommandSnippetGroupAutoClearTask.clear start");
        // 获取锁并清理
        LockerUtils.tryLock(LOCK_KEY, commandSnippetGroupService::clearUnusedGroup);
        log.info("CommandSnippetGroupAutoClearTask.clear finish");
    }

}
