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
package org.dromara.visor.module.terminal.define.operator;

import cn.orionsec.kit.lang.utils.collect.Lists;
import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

import java.util.List;

/**
 * 终端文件日志 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/2 14:37
 */
@Module("terminal:terminal-file-log")
public class TerminalFileLogOperatorType extends InitializingOperatorTypes {

    public static final String DELETE = "terminal-file-log:delete";

    public static final List<String> TYPES = Lists.of(
            TerminalOperatorType.SFTP_MKDIR,
            TerminalOperatorType.SFTP_TOUCH,
            TerminalOperatorType.SFTP_MOVE,
            TerminalOperatorType.SFTP_REMOVE,
            TerminalOperatorType.SFTP_TRUNCATE,
            TerminalOperatorType.SFTP_CHMOD,
            TerminalOperatorType.SFTP_CHOWN,
            TerminalOperatorType.SFTP_CHGRP,
            TerminalOperatorType.SFTP_GET_CONTENT,
            TerminalOperatorType.SFTP_SET_CONTENT,
            TerminalOperatorType.SFTP_UPLOAD,
            TerminalOperatorType.SFTP_DOWNLOAD,
            TerminalOperatorType.RDP_UPLOAD,
            TerminalOperatorType.RDP_DOWNLOAD
    );

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(OperatorRiskLevel.H, DELETE, "删除文件操作日志 <sb>${count}</sb> 条"),
        };
    }

}
