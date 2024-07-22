package com.orion.visor.module.asset.task;

import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.common.utils.LockerUtils;
import com.orion.visor.module.asset.define.config.AppExecLogAutoClearConfig;
import com.orion.visor.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.visor.module.asset.enums.ExecStatusEnum;
import com.orion.visor.module.asset.service.ExecLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 执行日志文件自动清理
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/24 23:50
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "app.auto-clear.exec-log.enabled", havingValue = "true")
public class ExecLogFileAutoClearTask {

    /**
     * 分布式锁名称
     */
    private static final String LOCK_KEY = "clear:exl:lock";

    @Resource
    private AppExecLogAutoClearConfig appExecLogAutoClearConfig;

    @Resource
    private ExecLogService execLogService;

    /**
     * 清理
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void clear() {
        log.info("ExecLogFileAutoClearTask.clear start");
        // 获取锁并执行
        LockerUtils.tryLock(LOCK_KEY, this::doClear);
        log.info("ExecLogFileAutoClearTask.clear finish");
    }

    /**
     * 执行清理
     */
    private void doClear() {
        // 删除的时间区间
        Date createLessEq = Dates.stream()
                .subDay(appExecLogAutoClearConfig.getKeepPeriod())
                .date();
        // 清理
        ExecLogQueryRequest request = new ExecLogQueryRequest();
        request.setCreateTimeLe(createLessEq);
        request.setStatusList(ExecStatusEnum.FINISH_STATUS_LIST);
        execLogService.clearExecLog(request);
    }

}
