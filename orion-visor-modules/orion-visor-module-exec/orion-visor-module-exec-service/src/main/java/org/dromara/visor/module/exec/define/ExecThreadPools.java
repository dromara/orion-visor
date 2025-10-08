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
package org.dromara.visor.module.exec.define;

import org.dromara.visor.framework.executor.core.utils.ExecutorUtils;

import java.util.concurrent.Executor;

/**
 * 执行线程池
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 11:21
 */
public interface ExecThreadPools {

    /**
     * 超时检查线程池
     */
    Executor TIMEOUT_CHECK = ExecutorUtils.getExecutor("execTimeoutCheckExecutor");

    /**
     * 批量执行任务线程池
     */
    Executor EXEC_TASK = ExecutorUtils.getExecutor("execTaskExecutor");

    /**
     * 批量执行主机命令线程池
     */
    Executor EXEC_HOST = ExecutorUtils.getExecutor("execHostCommandExecutor");

    /**
     * 批量执行日志查看线程池
     */
    Executor EXEC_LOG = ExecutorUtils.getExecutor("execLogViewExecutor");

    /**
     * 批量上传任务线程池
     */
    Executor UPLOAD_TASK = ExecutorUtils.getExecutor("uploadTaskExecutor");

    /**
     * 批量上传主机线程池
     */
    Executor UPLOAD_HOST = ExecutorUtils.getExecutor("uploadHostExecutor");

}
