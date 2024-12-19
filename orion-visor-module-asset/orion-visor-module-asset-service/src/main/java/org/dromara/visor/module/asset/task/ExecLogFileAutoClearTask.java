/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.module.asset.task;

import cn.orionsec.kit.lang.utils.time.Dates;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.utils.LockerUtils;
import org.dromara.visor.module.asset.define.config.AppExecLogAutoClearConfig;
import org.dromara.visor.module.asset.entity.request.exec.ExecLogClearRequest;
import org.dromara.visor.module.asset.enums.ExecStatusEnum;
import org.dromara.visor.module.asset.service.ExecLogService;
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
        ExecLogClearRequest request = new ExecLogClearRequest();
        request.setCreateTimeLe(createLessEq);
        request.setStatusList(ExecStatusEnum.FINISH_STATUS_LIST);
        execLogService.clearExecLog(request);
    }

}
