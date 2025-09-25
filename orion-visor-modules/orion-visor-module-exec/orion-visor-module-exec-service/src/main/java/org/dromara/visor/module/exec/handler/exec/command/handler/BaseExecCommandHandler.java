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
package org.dromara.visor.module.exec.handler.exec.command.handler;

import cn.orionsec.kit.lang.exception.AuthenticationException;
import cn.orionsec.kit.lang.exception.ConnectionRuntimeException;
import cn.orionsec.kit.lang.exception.SftpException;
import cn.orionsec.kit.lang.id.UUIds;
import cn.orionsec.kit.lang.support.timeout.TimeoutChecker;
import cn.orionsec.kit.lang.support.timeout.TimeoutEndpoint;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.ansi.AnsiAppender;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import cn.orionsec.kit.spring.SpringHolder;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.FileConst;
import org.dromara.visor.common.enums.BooleanBit;
import org.dromara.visor.common.enums.EndpointDefine;
import org.dromara.visor.common.file.FileClient;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.ssh.SessionStores;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.common.utils.PathUtils;
import org.dromara.visor.module.asset.api.HostConnectApi;
import org.dromara.visor.module.asset.enums.HostOsTypeEnum;
import org.dromara.visor.module.exec.dao.ExecHostLogDAO;
import org.dromara.visor.module.exec.entity.domain.ExecHostLogDO;
import org.dromara.visor.module.exec.entity.domain.ExecLogDO;
import org.dromara.visor.module.exec.enums.ExecHostStatusEnum;
import org.dromara.visor.module.exec.handler.exec.log.manager.ExecLogManager;
import org.dromara.visor.module.exec.utils.ExecUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 命令执行器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/25 18:35
 */
@Slf4j
public abstract class BaseExecCommandHandler implements IExecCommandHandler {

    private static final FileClient fileClient = SpringHolder.getBean("logsFileClient");

    private static final ExecLogManager execLogManager = SpringHolder.getBean(ExecLogManager.class);

    private static final HostConnectApi hostConnectApi = SpringHolder.getBean(HostConnectApi.class);

    private static final ExecHostLogDAO execHostLogDAO = SpringHolder.getBean(ExecHostLogDAO.class);

    @Getter
    protected final Long id;

    protected final Map<String, Object> builtParams;

    protected final TimeoutChecker<TimeoutEndpoint> timeoutChecker;

    protected final ExecLogDO execLog;

    protected ExecHostLogDO execHostLog;

    @Getter
    protected ExecHostStatusEnum status;

    private SshConnectConfig connectConfig;

    private OutputStream logOutputStream;

    private SessionStore sessionStore;

    private CommandExecutor executor;

    @Getter
    private Integer exitCode;

    private volatile boolean closed;

    private volatile boolean interrupted;

    public BaseExecCommandHandler(Long id,
                                  ExecLogDO execLog,
                                  Map<String, Object> builtParams,
                                  TimeoutChecker<TimeoutEndpoint> timeoutChecker) {
        this.id = id;
        this.execLog = execLog;
        this.builtParams = builtParams;
        this.timeoutChecker = timeoutChecker;
        this.status = ExecHostStatusEnum.WAITING;
    }

    @Override
    public void run() {
        Exception ex = null;
        log.info("ExecCommandHandler run start id: {}", id);
        // 初始化数据以及修改状态
        if (!this.initData()) {
            return;
        }
        try {
            // 初始化日志
            this.initLogOutputStream();
            // 执行命令
            this.execCommand();
            log.info("ExecCommandHandler run complete id: {}", id);
        } catch (Exception e) {
            log.error("ExecCommandHandler run error id: {}", id, e);
            ex = e;
        }
        // 执行完成回调
        try {
            // 回调
            this.onFinishCallback(ex);
        } finally {
            // 释放资源
            Streams.close(this);
        }
    }

    /**
     * 初始化数据
     *
     * @return pass
     */
    private boolean initData() {
        Exception ex = null;
        try {
            // 查询任务
            this.execHostLog = execHostLogDAO.selectById(id);
            Assert.notNull(this.execHostLog, ErrorMessage.TASK_ABSENT);
            // 检查任务状态
            this.status = ExecHostStatusEnum.of(execHostLog.getStatus());
            Assert.eq(this.status, ExecHostStatusEnum.WAITING, ErrorMessage.TASK_ABSENT, ErrorMessage.ILLEGAL_STATUS);
            // 获取主机会话
            this.connectConfig = hostConnectApi.getSshConnectConfig(execHostLog.getHostId(), execLog.getUserId());
            // 设置日志路径
            this.setLogPath();
            // 设置脚本路径
            this.setScriptPath();
            // 设置执行的命令以及参数
            this.setExecCommand();
        } catch (Exception e) {
            log.error("BaseExecCommandHandler.initData error id: {}", id, e);
            ex = e;
        }
        boolean passed = ex == null;
        // 更新状态
        this.updateStatus(passed ? ExecHostStatusEnum.RUNNING : ExecHostStatusEnum.FAILED, ex, (s) -> {
            // 修改其他参数
            s.setCommand(execHostLog.getCommand());
            s.setParameter(execHostLog.getParameter());
            s.setLogPath(execHostLog.getLogPath());
            s.setScriptPath(execHostLog.getScriptPath());
        });
        return passed;
    }

    /**
     * 执行命令
     *
     * @throws IOException IOException
     */
    protected void execCommand() throws Exception {
        // 打开会话
        this.sessionStore = SessionStores.openSessionStore(connectConfig);
        if (BooleanBit.toBoolean(execLog.getScriptExec())) {
            // 上传脚本文件
            this.uploadScriptFile();
            // 执行脚本文件
            this.executor = sessionStore.getCommandExecutor(execHostLog.getScriptPath());
        } else {
            // 执行命令
            byte[] command = execHostLog.getCommand().getBytes(connectConfig.getCharset());
            this.executor = sessionStore.getCommandExecutor(command);
        }
        // 执行命令
        executor.timeout(execLog.getTimeout(), TimeUnit.SECONDS, timeoutChecker);
        executor.merge();
        executor.transfer(logOutputStream);
        executor.connect();
        executor.exec();
    }

    /**
     * 初始化日志输出流
     *
     * @throws Exception Exception
     */
    protected void initLogOutputStream() throws Exception {
        // 打开日志流
        this.logOutputStream = fileClient.getContentOutputStream(execHostLog.getLogPath());
    }

    /**
     * 上传脚本文件
     */
    protected void uploadScriptFile() {
        SftpExecutor sftpExecutor = null;
        try {
            // 打开 sftp
            sftpExecutor = sessionStore.getSftpExecutor(connectConfig.getFileNameCharset());
            sftpExecutor.connect();
            // 文件上传必须要以 / 开头
            String scriptPath = PathUtils.prependSeparator(execHostLog.getScriptPath());
            // 创建文件
            sftpExecutor.touch(scriptPath);
            // 写入命令
            byte[] command = execHostLog.getCommand().getBytes(connectConfig.getFileContentCharset());
            sftpExecutor.write(scriptPath, command);
            // 修改权限
            sftpExecutor.changeMode(scriptPath, 777);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        } finally {
            Streams.close(sftpExecutor);
        }
    }

    /**
     * 执行完成回调
     *
     * @param e e
     */
    protected void onFinishCallback(Exception e) {
        // 执行回调
        if (this.interrupted) {
            // 中断执行
            this.updateStatus(ExecHostStatusEnum.INTERRUPTED, null, null);
        } else if (e != null) {
            // 执行失败
            this.updateStatus(ExecHostStatusEnum.FAILED, e, null);
        } else if (executor.isTimeout()) {
            // 更新执行超时
            this.updateStatus(ExecHostStatusEnum.TIMEOUT, null, null);
        } else {
            // 更新执行完成
            this.updateStatus(ExecHostStatusEnum.COMPLETED, null, null);
        }
    }

    /**
     * 拼接日志
     *
     * @param appender appender
     */
    protected void appendLog(AnsiAppender appender) {
        try {
            logOutputStream.write(Strings.bytes(appender.toString()));
            logOutputStream.flush();
        } catch (Exception e) {
            log.error("BaseExecCommandHandler.appendLog error", e);
        }
    }

    /**
     * 更新状态
     *
     * @param status status
     * @param ex     ex
     * @param filler filler
     */
    private void updateStatus(ExecHostStatusEnum status, Exception ex, Consumer<ExecHostLogDO> filler) {
        this.status = status;
        String statusName = status.name();
        execHostLog.setStatus(statusName);
        log.info("BaseExecCommandHandler.updateStatus start id: {}, status: {}", id, statusName);
        try {
            if (ExecHostStatusEnum.RUNNING.equals(status)) {
                // 运行中
                execHostLog.setStartTime(new Date());
            } else if (ExecHostStatusEnum.COMPLETED.equals(status)) {
                // 完成
                execHostLog.setFinishTime(new Date());
                execHostLog.setExitCode(executor.getExitCode());
                this.exitCode = executor.getExitCode();
            } else if (ExecHostStatusEnum.FAILED.equals(status)) {
                // 失败
                execHostLog.setFinishTime(new Date());
                execHostLog.setErrorMessage(this.getErrorMessage(ex));
            } else if (ExecHostStatusEnum.TIMEOUT.equals(status)) {
                // 超时
                execHostLog.setFinishTime(new Date());
            } else if (ExecHostStatusEnum.INTERRUPTED.equals(status)) {
                // 中断
                execHostLog.setFinishTime(new Date());
            }
            // 选择性更新
            ExecHostLogDO updateRecord = ExecHostLogDO.builder()
                    .id(execHostLog.getId())
                    .status(execHostLog.getStatus())
                    .exitCode(execHostLog.getExitCode())
                    .startTime(execHostLog.getStartTime())
                    .finishTime(execHostLog.getFinishTime())
                    .errorMessage(execHostLog.getErrorMessage())
                    .build();
            // 填充参数
            if (filler != null) {
                filler.accept(updateRecord);
            }
            int effect = execHostLogDAO.updateById(updateRecord);
            log.info("BaseExecCommandHandler.updateStatus finish id: {}, effect: {}", id, effect);
        } catch (Exception e) {
            log.error("BaseExecCommandHandler.updateStatus error id: {}", id, e);
        }
    }

    @Override
    public void write(String msg) {
        this.executor.write(msg);
    }

    @Override
    public void interrupt() {
        log.info("BaseExecCommandHandler.interrupt id: {}, interrupted: {}, closed: {}", id, interrupted, closed);
        if (this.interrupted || this.closed) {
            return;
        }
        // 关闭
        this.interrupted = true;
        Streams.close(executor);
        Streams.close(sessionStore);
    }

    @Override
    public void close() {
        log.info("BaseExecCommandHandler.closed id: {}, closed: {}", id, closed);
        if (this.closed) {
            return;
        }
        this.closed = true;
        Streams.close(logOutputStream);
        Streams.close(executor);
        Streams.close(sessionStore);
        execLogManager.asyncCloseTailFile(id);
    }

    /**
     * 设置日志路径
     */
    private void setLogPath() {
        // 构建日志路径
        String logPath = fileClient.getReturnPath(EndpointDefine.EXEC_LOG.format(execHostLog.getLogId(), id));
        execHostLog.setLogPath(logPath);
    }

    /**
     * 设置脚本路径
     */
    private void setScriptPath() {
        String scriptPath = null;
        // 获取脚本路径
        if (BooleanBit.toBoolean(execLog.getScriptExec())) {
            HostOsTypeEnum os = HostOsTypeEnum.of(connectConfig.getOsType());
            String name = FileConst.EXEC
                    + "/" + execHostLog.getLogId()
                    + "/" + id
                    + os.getScriptSuffix();
            scriptPath = PathUtils.buildAppPath(HostOsTypeEnum.WINDOWS.equals(os), connectConfig.getUsername(), FileConst.SCRIPT, name);
        }
        execHostLog.setScriptPath(scriptPath);
    }

    /**
     * 设置实际执行的命令以及参数
     */
    private void setExecCommand() {
        String uuid = UUIds.random();
        // 参数列表
        Map<String, Object> params = Maps.newMap(builtParams);
        params.put("execHostId", id);
        params.put("hostId", connectConfig.getHostId());
        params.put("hostName", connectConfig.getHostName());
        params.put("hostCode", connectConfig.getHostCode());
        params.put("hostAddress", connectConfig.getHostAddress());
        params.put("hostPort", connectConfig.getHostPort());
        params.put("hostUsername", connectConfig.getUsername());
        params.put("agentKey", connectConfig.getAgentKey());
        params.put("hostUuid", uuid);
        params.put("hostUuidShort", uuid.replace("-", Strings.EMPTY));
        params.put("osType", connectConfig.getOsType());
        params.put("archType", connectConfig.getArchType());
        params.put("charset", connectConfig.getCharset());
        params.put("scriptPath", execHostLog.getScriptPath());
        // 获取实际命令
        String paramsJson = JSON.toJSONString(params);
        String command = ExecUtils.format(execLog.getCommand(), paramsJson);
        execHostLog.setCommand(command);
        execHostLog.setParameter(paramsJson);
    }

    /**
     * 获取错误信息
     *
     * @param ex ex
     * @return errorMessage
     */
    protected String getErrorMessage(Exception ex) {
        if (ex == null) {
            return null;
        }
        String message;
        if (ErrorMessage.isBizException(ex)) {
            // 业务异常
            message = ex.getMessage();
        } else if (ex instanceof ConnectionRuntimeException) {
            // 连接异常
            message = ErrorMessage.CONNECT_ERROR;
        } else if (ex instanceof AuthenticationException) {
            // 认证异常
            message = ErrorMessage.AUTH_ERROR;
        } else if (ex instanceof SftpException) {
            // 上传异常
            message = ErrorMessage.SCRIPT_UPLOAD_ERROR;
        } else {
            // 其他异常
            message = ErrorMessage.EXEC_ERROR;
        }
        return Strings.retain(message, 250);
    }

}
