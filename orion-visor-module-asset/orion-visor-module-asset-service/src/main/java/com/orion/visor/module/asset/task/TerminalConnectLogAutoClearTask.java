/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.task;

import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.common.utils.LockerUtils;
import com.orion.visor.module.asset.define.config.AppTerminalConnectLogAutoClearConfig;
import com.orion.visor.module.asset.entity.request.host.TerminalConnectLogClearRequest;
import com.orion.visor.module.asset.enums.TerminalConnectStatusEnum;
import com.orion.visor.module.asset.service.TerminalConnectLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 终端连接日志自动清理
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/24 23:50
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "app.auto-clear.terminal-connect-log.enabled", havingValue = "true")
public class TerminalConnectLogAutoClearTask {

    /**
     * 分布式锁名称
     */
    private static final String LOCK_KEY = "clear:tcl:lock";

    @Resource
    private AppTerminalConnectLogAutoClearConfig appTerminalConnectLogAutoClearConfig;

    @Resource
    private TerminalConnectLogService terminalConnectLogService;

    /**
     * 清理
     */
    @Scheduled(cron = "0 10 3 * * ?")
    public void clear() {
        log.info("TerminalConnectLogAutoClearTask.clear start");
        // 获取锁并执行
        LockerUtils.tryLock(LOCK_KEY, this::doClear);
        log.info("TerminalConnectLogAutoClearTask.clear finish");
    }

    /**
     * 执行清理
     */
    private void doClear() {
        // 删除的时间区间
        Date createLessEq = Dates.stream()
                .subDay(appTerminalConnectLogAutoClearConfig.getKeepPeriod())
                .date();
        // 清理
        TerminalConnectLogClearRequest request = new TerminalConnectLogClearRequest();
        request.setCreateTimeLe(createLessEq);
        request.setStatusList(TerminalConnectStatusEnum.FINISH_STATUS_LIST);
        terminalConnectLogService.clearTerminalConnectLog(request);
    }

}
