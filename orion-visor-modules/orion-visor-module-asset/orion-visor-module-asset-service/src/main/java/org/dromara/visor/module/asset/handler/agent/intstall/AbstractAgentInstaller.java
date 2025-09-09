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

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.FileConst;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.ssh.SessionStores;
import org.dromara.visor.common.utils.PathUtils;
import org.dromara.visor.module.asset.entity.domain.HostAgentLogDO;
import org.dromara.visor.module.asset.enums.AgentLogStatusEnum;
import org.dromara.visor.module.asset.enums.HostOsTypeEnum;
import org.dromara.visor.module.asset.handler.agent.model.AgentInstallParams;
import org.dromara.visor.module.asset.service.HostAgentLogService;
import org.dromara.visor.module.asset.service.HostConnectService;

import java.io.IOException;

/**
 * 探针安装器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/29 11:12
 */
@Slf4j
public abstract class AbstractAgentInstaller implements AgentInstaller {

    private static final String REPLACEMENT = "$$";

    private static final HostAgentLogService hostAgentLogService = SpringHolder.getBean(HostAgentLogService.class);

    private static final HostConnectService hostConnectService = SpringHolder.getBean(HostConnectService.class);

    protected final Long logId;

    protected final AgentInstallParams params;

    protected final String startScriptName;

    protected final String uploadAgentName;

    protected String agentHomePath;

    protected HostAgentLogDO record;

    protected SshConnectConfig sshConfig;

    protected SessionStore sessionStore;

    protected CommandExecutor commandExecutor;

    protected SftpExecutor sftpExecutor;

    public AbstractAgentInstaller(AgentInstallParams params) {
        this.params = params;
        this.logId = params.getLogId();
        this.startScriptName = Const.START + HostOsTypeEnum.of(params.getOsType()).getScriptSuffix();
        this.uploadAgentName = FileConst.AGENT + HostOsTypeEnum.of(params.getOsType()).getBinarySuffix();
    }

    @Override
    public void run() {
        log.info("AgentInstaller install start {}", logId);
        // 查询记录
        this.record = hostAgentLogService.selectById(logId);
        Valid.notNull(record, "AgentInstaller record is null {}", logId);
        Valid.eq(record.getStatus(), AgentLogStatusEnum.WAIT.name(), "AgentInstaller record status is not WAIT {}", logId);
        try {
            // 更新状态
            this.updateStatus(AgentLogStatusEnum.RUNNING, null);
            // 打开会话
            this.initSession();
            log.info("AgentInstaller install session init {}", logId);
            // 上传文件
            this.uploadFile();
            log.info("AgentInstaller install upload finish {}", logId);
            // 启动探针
            this.startAgent();
            log.info("AgentInstaller install stated {}", logId);
            // 更新状态
            this.updateStatus(AgentLogStatusEnum.SUCCESS, null);
        } catch (Exception e) {
            // 更新状态
            log.error("AgentInstaller install error {}", logId, e);
            this.updateStatus(AgentLogStatusEnum.FAILED, ErrorMessage.getErrorMessage(e, 1000));
        } finally {
            this.close();
        }
    }

    /**
     * 初始化会话
     */
    private void initSession() {
        // 获取 ssh 配置
        this.sshConfig = hostConnectService.getSshConnectConfig(params.getHostId());
        // 设置探针家目录
        this.agentHomePath = this.getAgentHomePath();
        // 打开会话
        this.sessionStore = SessionStores.openSessionStore(sshConfig);
        // 打开 sftp
        this.sftpExecutor = sessionStore.getSftpExecutor(sshConfig.getFileNameCharset());
        sftpExecutor.connect();
    }

    /**
     * 上传文件
     *
     * @throws IOException IOException
     */
    protected abstract void uploadFile() throws IOException;

    /**
     * 启动 agent
     */
    protected abstract void startAgent() throws IOException;

    /**
     * 获取探针家目录
     *
     * @return path
     */
    protected String getAgentHomePath() {
        return PathUtils.buildAppPath(HostOsTypeEnum.WINDOWS.name().equals(params.getOsType()),
                sshConfig.getUsername(),
                FileConst.AGENT) + Const.SLASH;
    }

    /**
     * 替换文件内容
     *
     * @param path path
     * @return content
     */
    protected String replaceContent(String path) {
        // 读取文件
        byte[] contentBytes = FileReaders.readAllBytesFast(path);
        // 格式化文件
        return Strings.format(new String(contentBytes), REPLACEMENT, params.getReplaceVars());
    }

    /**
     * 更新状态
     *
     * @param status  状态
     * @param message 消息
     */
    protected void updateStatus(AgentLogStatusEnum status, String message) {
        log.info("AgentInstaller update status {}, {}", logId, status);
        hostAgentLogService.updateStatus(logId, status.name(), message);
    }

    @Override
    public void close() {
        Streams.close(sftpExecutor);
        Streams.close(commandExecutor);
        Streams.close(sessionStore);
    }

}
