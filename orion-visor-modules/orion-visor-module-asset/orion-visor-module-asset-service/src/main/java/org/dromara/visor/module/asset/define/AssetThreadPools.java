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
package org.dromara.visor.module.asset.define;

import cn.orionsec.kit.lang.define.thread.ExecutorBuilder;
import cn.orionsec.kit.lang.utils.Systems;
import org.dromara.visor.common.constant.Const;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 执行线程池
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 11:21
 */
public interface AssetThreadPools {

    /**
     * 批量执行主机命令线程池
     */
    ThreadPoolExecutor AGENT_INSTALL = ExecutorBuilder.create()
            .namedThreadFactory("agent-install-")
            .corePoolSize(Systems.PROCESS_NUM)
            .maxPoolSize(Systems.PROCESS_NUM)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new LinkedBlockingQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

}
