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
package org.dromara.visor.module.asset.handler.agent.intstall;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.FileConst;
import org.dromara.visor.module.asset.handler.agent.model.AgentInstallParams;

import java.io.IOException;

/**
 * windows 探针安装器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/29 11:12
 */
@Slf4j
public class WindowsAgentInstaller extends AbstractAgentInstaller {

    public WindowsAgentInstaller(AgentInstallParams params) {
        super(params);
    }

    @Override
    protected void uploadFile() throws IOException {
        // 写入配置文件
        sftpExecutor.write(Const.SLASH + agentHomePath + FileConst.CONFIG_YAML, this.replaceContent(params.getConfigFilePath()));
        log.info("写入配置文件成功");
        // 写入启动脚本
        sftpExecutor.write(Const.SLASH + agentHomePath + startScriptName, this.replaceContent(params.getStartScriptPath()));
        log.info("写入启动脚本成功");
        // 上传探针文件
        sftpExecutor.uploadFile(Const.SLASH + agentHomePath + uploadAgentName, params.getAgentFilePath());
        log.info("上传探针文件成功");
    }

    @Override
    protected void startAgent() {
        // TODO
    }

}
