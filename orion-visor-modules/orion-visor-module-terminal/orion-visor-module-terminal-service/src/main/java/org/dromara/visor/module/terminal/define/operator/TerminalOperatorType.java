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

import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

/**
 * 终端 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("terminal:terminal")
public class TerminalOperatorType extends InitializingOperatorTypes {

    public static final String CONNECT = "terminal:connect";

    public static final String SFTP_MKDIR = "terminal:sftp-mkdir";

    public static final String SFTP_TOUCH = "terminal:sftp-touch";

    public static final String SFTP_MOVE = "terminal:sftp-move";

    public static final String SFTP_REMOVE = "terminal:sftp-remove";

    public static final String SFTP_TRUNCATE = "terminal:sftp-truncate";

    public static final String SFTP_CHMOD = "terminal:sftp-chmod";

    public static final String SFTP_CHOWN = "terminal:sftp-chown";

    public static final String SFTP_CHGRP = "terminal:sftp-chgrp";

    public static final String SFTP_GET_CONTENT = "terminal:sftp-get-content";

    public static final String SFTP_SET_CONTENT = "terminal:sftp-set-content";

    public static final String SFTP_UPLOAD = "terminal:sftp-upload";

    public static final String SFTP_DOWNLOAD = "terminal:sftp-download";

    public static final String RDP_UPLOAD = "terminal:rdp-upload";

    public static final String RDP_DOWNLOAD = "terminal:rdp-download";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(OperatorRiskLevel.L, CONNECT, "连接主机 ${connectType} <sb>${hostName}</sb>"),
                new OperatorType(OperatorRiskLevel.L, SFTP_MKDIR, "创建文件夹 ${hostName} <sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.L, SFTP_TOUCH, "创建文件 ${hostName} <sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.M, SFTP_MOVE, "移动文件 ${hostName} <sb>${path}</sb> 至 <sb>${target}</sb>"),
                new OperatorType(OperatorRiskLevel.H, SFTP_REMOVE, "删除文件 ${hostName} ${count}个\n<sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.H, SFTP_TRUNCATE, "截断文件 ${hostName} <sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.M, SFTP_CHMOD, "文件提权 ${hostName} <sb>${path}</sb> <sb>${mod}</sb>"),
                new OperatorType(OperatorRiskLevel.M, SFTP_CHOWN, "修改文件归属 ${hostName} <sb>${path}</sb> <sb>${id}</sb>"),
                new OperatorType(OperatorRiskLevel.M, SFTP_CHGRP, "修改文件分组 ${hostName} <sb>${path}</sb> <sb>${id}</sb>"),
                new OperatorType(OperatorRiskLevel.L, SFTP_GET_CONTENT, "获取文件内容 ${hostName} <sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.M, SFTP_SET_CONTENT, "修改文件内容 ${hostName} <sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.M, SFTP_UPLOAD, "上传文件 ${hostName} (${count}个)\n<sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.M, SFTP_DOWNLOAD, "下载文件 ${hostName} (${count}个)\n<sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.M, RDP_UPLOAD, "上传文件 ${hostName} <sb>${path}</sb>"),
                new OperatorType(OperatorRiskLevel.M, RDP_DOWNLOAD, "下载文件 ${hostName} <sb>${path}</sb>"),
        };
    }

}
