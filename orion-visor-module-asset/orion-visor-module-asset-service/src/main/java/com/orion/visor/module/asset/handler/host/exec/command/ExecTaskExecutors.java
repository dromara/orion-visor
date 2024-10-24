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
package com.orion.visor.module.asset.handler.host.exec.command;

import com.orion.visor.module.asset.define.AssetThreadPools;
import com.orion.visor.module.asset.handler.host.exec.command.handler.ExecTaskHandler;
import com.orion.visor.module.asset.handler.host.exec.command.model.ExecCommandDTO;

/**
 * 批量执行命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 11:10
 */
public class ExecTaskExecutors {

    /**
     * 执行命令
     *
     * @param command command
     */
    public static void start(ExecCommandDTO command) {
        AssetThreadPools.EXEC_TASK.execute(new ExecTaskHandler(command));
    }

}
