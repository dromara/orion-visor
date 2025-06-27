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
package org.dromara.visor.module.terminal.handler.terminal.record;

import cn.orionsec.kit.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import org.dromara.visor.module.terminal.define.TerminalThreadPools;

/**
 * 终端异步保存器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/28 19:08
 */
@Slf4j
public class TerminalAsyncSaver {

    private static final OperatorLogFrameworkService operatorLogFrameworkService = SpringHolder.getBean(OperatorLogFrameworkService.class);

    private TerminalAsyncSaver() {
    }

    /**
     * 保存操作日志
     *
     * @param model model
     */
    public static void saveOperatorLog(OperatorLogModel model) {
        TerminalThreadPools.TERMINAL_ASYNC_SAVER.execute(() -> {
            model.setCreateTime(model.getStartTime());
            operatorLogFrameworkService.insert(model);
        });
    }

}
