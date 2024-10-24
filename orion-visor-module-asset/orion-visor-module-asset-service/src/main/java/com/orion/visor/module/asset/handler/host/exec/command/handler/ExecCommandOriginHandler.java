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
package com.orion.visor.module.asset.handler.host.exec.command.handler;

import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.support.timeout.TimeoutEndpoint;
import com.orion.visor.module.asset.handler.host.exec.command.model.ExecCommandDTO;
import com.orion.visor.module.asset.handler.host.exec.command.model.ExecCommandHostDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 命令执行器 原始日志输出
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 11:30
 */
@Slf4j
public class ExecCommandOriginHandler extends BaseExecCommandHandler {

    public ExecCommandOriginHandler(ExecCommandDTO execCommand, ExecCommandHostDTO execHostCommand, TimeoutChecker<TimeoutEndpoint> timeoutChecker) {
        super(execCommand, execHostCommand, timeoutChecker);
    }

}
