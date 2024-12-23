/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.utils.LockerUtils;
import org.dromara.visor.module.asset.service.PathBookmarkGroupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时清理未使用的路径标签分组
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/24 23:50
 */
@Slf4j
@Component
public class PathBookmarkGroupAutoClearTask {

    /**
     * 分布式锁名称
     */
    private static final String LOCK_KEY = "clear:pbg:lock";

    @Resource
    private PathBookmarkGroupService pathBookmarkGroupService;

    /**
     * 清理
     */
    @Scheduled(cron = "0 20 2 * * ?")
    public void clear() {
        log.info("PathBookmarkGroupAutoClearTask.clear start");
        // 获取锁并执行
        LockerUtils.tryLock(LOCK_KEY, pathBookmarkGroupService::clearUnusedGroup);
        log.info("PathBookmarkGroupAutoClearTask.clear finish");
    }

}
