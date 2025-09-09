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

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutors;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.FileConst;
import org.dromara.visor.module.asset.handler.agent.model.AgentInstallParams;

import java.io.IOException;

/**
 * linux 探针安装器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/29 11:12
 */
@Slf4j
public class LinuxAgentInstaller extends AbstractAgentInstaller {

    public LinuxAgentInstaller(AgentInstallParams params) {
        super(params);
    }

    @Override
    protected void uploadFile() throws IOException {
        // 写入配置文件
        sftpExecutor.write(agentHomePath + FileConst.CONFIG_YAML, this.replaceContent(params.getConfigFilePath()));
        log.info("写入配置文件成功");
        // 写入启动脚本
        sftpExecutor.write(agentHomePath + startScriptName, this.replaceContent(params.getStartScriptPath()));
        log.info("写入启动脚本成功");
        // 上传探针文件
        sftpExecutor.uploadFile(agentHomePath + uploadAgentName, params.getAgentFilePath());
        log.info("上传探针文件成功");
        // 脚本提权
        sftpExecutor.changeMode(agentHomePath + startScriptName, 777);
        log.info("脚本提权成功");
        // 探针提权
        sftpExecutor.changeMode(agentHomePath + uploadAgentName, 777);
        log.info("探针提权成功");
    }

    @Override
    protected void startAgent() throws IOException {
        String command = "cd " + agentHomePath + " && sh " + startScriptName;
        log.info("LinuxAgentInstaller command: {}", command);
        this.commandExecutor = sessionStore.getCommandExecutor(command);
        this.commandExecutor.pty(false);
        byte[] result = CommandExecutors.getCommandOutputResult(this.commandExecutor);
        // 如果不是成功启动则抛异常
        if (!commandExecutor.isSuccessExit()) {
            throw Exceptions.app("exit: " + commandExecutor.getExitCode() + ". " + new String(result));
        }
    }

}
