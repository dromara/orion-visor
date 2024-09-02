package com.orion.visor.module.asset.task;

import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.common.utils.LockerUtils;
import com.orion.visor.module.asset.define.config.AppHostConnectLogAutoClearConfig;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogClearRequest;
import com.orion.visor.module.asset.enums.HostConnectStatusEnum;
import com.orion.visor.module.asset.service.HostConnectLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 主机连接日志自动清理
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/24 23:50
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "app.auto-clear.host-connect-log.enabled", havingValue = "true")
public class HostConnectLogAutoClearTask {

    /**
     * 分布式锁名称
     */
    private static final String LOCK_KEY = "clear:hcl:lock";

    @Resource
    private AppHostConnectLogAutoClearConfig appHostConnectLogAutoClearConfig;

    @Resource
    private HostConnectLogService hostConnectLogService;

    /**
     * 清理
     */
    @Scheduled(cron = "0 10 3 * * ?")
    public void clear() {
        log.info("HostConnectLogAutoClearTask.clear start");
        // 获取锁并执行
        LockerUtils.tryLock(LOCK_KEY, this::doClear);
        log.info("HostConnectLogAutoClearTask.clear finish");
    }

    /**
     * 执行清理
     */
    private void doClear() {
        // 删除的时间区间
        Date createLessEq = Dates.stream()
                .subDay(appHostConnectLogAutoClearConfig.getKeepPeriod())
                .date();
        // 清理
        HostConnectLogClearRequest request = new HostConnectLogClearRequest();
        request.setCreateTimeLe(createLessEq);
        request.setStatusList(HostConnectStatusEnum.FINISH_STATUS_LIST);
        hostConnectLogService.clearHostConnectLog(request);
    }

}
