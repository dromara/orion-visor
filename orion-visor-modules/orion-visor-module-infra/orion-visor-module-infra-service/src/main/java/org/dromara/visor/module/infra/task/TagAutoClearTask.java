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
package org.dromara.visor.module.infra.task;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.LockerUtils;
import org.dromara.visor.module.infra.service.TagService;
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
