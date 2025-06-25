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
package org.dromara.visor.module.terminal.handler.terminal.utils;

import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogFiller;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;

import java.util.Map;

/**
 * 终端工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/23 16:25
 */
public class TerminalUtils {

    private TerminalUtils() {
    }

    /**
     * 获取操作日志模型
     *
     * @param props     props
     * @param extra     extra
     * @param type      type
     * @param startTime startTime
     * @param ex        ex
     * @return model
     */
    public static OperatorLogModel getOperatorLogModel(TerminalChannelProps props,
                                                       Map<String, Object> extra,
                                                       String type,
                                                       long startTime,
                                                       Exception ex) {
        return OperatorLogFiller.create()
                // 填充用户信息
                .fillUserInfo(props.getUserId(), props.getUsername())
                // 填充 traceId
                .fillTraceId(props.getTraceId())
                // 填充请求留痕信息
                .fillIdentity(props.getIdentity())
                // 填充使用时间
                .fillUsedTime(startTime)
                // 填充结果信息
                .fillResult(null, ex)
                // 填充拓展信息
                .fillExtra(extra)
                // 填充日志
                .fillLogInfo(extra, type)
                .get();
    }

}
