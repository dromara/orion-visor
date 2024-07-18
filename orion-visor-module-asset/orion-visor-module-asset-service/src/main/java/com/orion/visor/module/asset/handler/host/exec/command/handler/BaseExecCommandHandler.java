package com.orion.visor.module.asset.handler.host.exec.command.handler;

import com.alibaba.fastjson.JSON;
import com.orion.lang.exception.AuthenticationException;
import com.orion.lang.exception.ConnectionRuntimeException;
import com.orion.lang.exception.SftpException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.support.timeout.TimeoutEndpoint;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.ansi.AnsiAppender;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.net.host.ssh.command.CommandExecutor;
import com.orion.spring.SpringHolder;
import com.orion.visor.framework.common.file.FileClient;
import com.orion.visor.module.asset.dao.ExecHostLogDAO;
import com.orion.visor.module.asset.entity.domain.ExecHostLogDO;
import com.orion.visor.module.asset.enums.ExecHostStatusEnum;
import com.orion.visor.module.asset.handler.host.exec.command.model.ExecCommandDTO;
import com.orion.visor.module.asset.handler.host.exec.command.model.ExecCommandHostDTO;
import com.orion.visor.module.asset.handler.host.exec.log.manager.ExecLogManager;
import com.orion.visor.module.asset.service.HostTerminalService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    private static final HostTerminalService hostTerminalService = SpringHolder.getBean(HostTerminalService.class);

    private static final ExecHostLogDAO execHostLogDAO = SpringHolder.getBean(ExecHostLogDAO.class);

    protected final ExecCommandDTO execCommand;

    protected final ExecCommandHostDTO execHostCommand;

    private final TimeoutChecker<TimeoutEndpoint> timeoutChecker;

    @Getter
    protected ExecHostStatusEnum status;

    protected ExecHostLogDO updateRecord;

    private OutputStream logOutputStream;

    private SessionStore sessionStore;

    private CommandExecutor executor;

    @Getter
    private Integer exitCode;

    private volatile boolean closed;

    private volatile boolean interrupted;

    public BaseExecCommandHandler(ExecCommandDTO execCommand,
                                  ExecCommandHostDTO execHostCommand,
                                  TimeoutChecker<TimeoutEndpoint> timeoutChecker) {
        this.status = ExecHostStatusEnum.WAITING;
        this.execCommand = execCommand;
        this.execHostCommand = execHostCommand;
        this.timeoutChecker = timeoutChecker;
        this.updateRecord = new ExecHostLogDO();
    }

    @Override
    public void run() {
        Long id = execHostCommand.getHostLogId();
        Exception ex = null;
        log.info("ExecCommandHandler run start id: {}, info: {}", id, JSON.toJSONString(execHostCommand));
        // 更新状态
        this.updateStatus(ExecHostStatusEnum.RUNNING, null);
        try {
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
     * 初始化日志输出流
     *
     * @throws Exception Exception
     */
    protected void initLogOutputStream() throws Exception {
        // 打开日志流
        this.logOutputStream = fileClient.getContentOutputStream(execHostCommand.getLogPath());
    }

    /**
     * 执行命令
     *
     * @throws IOException IOException
     */
    protected void execCommand() throws Exception {
        // 初始化日志
        this.initLogOutputStream();
        // 打开会话
        this.sessionStore = hostTerminalService.openSessionStore(execHostCommand.getHostId());
        if (Booleans.isTrue(execCommand.getScriptExec())) {
            // 上传脚本文件
            this.uploadScriptFile();
            // 执行脚本文件
            this.executor = sessionStore.getCommandExecutor(execHostCommand.getScriptPath());
        } else {
            // 执行命令
            byte[] command = Strings.replaceCRLF(execHostCommand.getCommand()).getBytes(execHostCommand.getCharset());
            this.executor = sessionStore.getCommandExecutor(command);
        }
        // 执行命令
        executor.timeout(execCommand.getTimeout(), TimeUnit.SECONDS, timeoutChecker);
        executor.merge();
        executor.transfer(logOutputStream);
        executor.connect();
        executor.exec();
    }

    /**
     * 上传脚本文件
     */
    protected void uploadScriptFile() {
        SftpExecutor sftpExecutor = null;
        try {
            // 打开 sftp
            sftpExecutor = sessionStore.getSftpExecutor(execHostCommand.getFileNameCharset());
            sftpExecutor.connect();
            // 必须要以 / 开头
            String scriptPath = execHostCommand.getScriptPath();
            if (!scriptPath.startsWith("/")) {
                scriptPath = "/" + scriptPath;
            }
            // 创建文件
            sftpExecutor.touch(scriptPath);
            // 写入命令
            byte[] command = Strings.replaceCRLF(execHostCommand.getCommand()).getBytes(execHostCommand.getFileContentCharset());
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
            this.updateStatus(ExecHostStatusEnum.INTERRUPTED, null);
        } else if (e != null) {
            // 执行失败
            this.updateStatus(ExecHostStatusEnum.FAILED, e);
        } else if (executor.isTimeout()) {
            // 更新执行超时
            this.updateStatus(ExecHostStatusEnum.TIMEOUT, null);
        } else {
            // 更新执行完成
            this.updateStatus(ExecHostStatusEnum.COMPLETED, null);
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
     */
    private void updateStatus(ExecHostStatusEnum status, Exception ex) {
        this.status = status;
        Long id = execHostCommand.getHostLogId();
        String statusName = status.name();
        log.info("BaseExecCommandHandler.updateStatus start id: {}, status: {}", id, statusName);
        updateRecord.setId(id);
        updateRecord.setStatus(statusName);
        if (ExecHostStatusEnum.RUNNING.equals(status)) {
            // 运行中
            updateRecord.setStartTime(new Date());
        } else if (ExecHostStatusEnum.COMPLETED.equals(status)) {
            // 完成
            updateRecord.setFinishTime(new Date());
            updateRecord.setExitCode(executor.getExitCode());
            this.exitCode = executor.getExitCode();
        } else if (ExecHostStatusEnum.FAILED.equals(status)) {
            // 失败
            updateRecord.setFinishTime(new Date());
            updateRecord.setErrorMessage(this.getErrorMessage(ex));
        } else if (ExecHostStatusEnum.TIMEOUT.equals(status)) {
            // 超时
            updateRecord.setFinishTime(new Date());
        } else if (ExecHostStatusEnum.INTERRUPTED.equals(status)) {
            // 中断
            updateRecord.setFinishTime(new Date());
        }
        int effect = execHostLogDAO.updateById(updateRecord);
        log.info("BaseExecCommandHandler.updateStatus finish id: {}, effect: {}", id, effect);
    }

    @Override
    public void write(String msg) {
        this.executor.write(msg);
    }

    @Override
    public void interrupt() {
        log.info("BaseExecCommandHandler.interrupt id: {}, interrupted: {}, closed: {}",
                execHostCommand.getHostLogId(), interrupted, closed);
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
        log.info("BaseExecCommandHandler.closed id: {}, closed: {}",
                execHostCommand.getHostLogId(), closed);
        if (this.closed) {
            return;
        }
        this.closed = true;
        Streams.close(logOutputStream);
        Streams.close(executor);
        Streams.close(sessionStore);
        execLogManager.asyncCloseTailFile(execHostCommand.getLogPath());
    }

    /**
     * 获取错误信息
     *
     * @param ex ex
     * @return errorMessage
     */
    protected String getErrorMessage(Exception ex) {
        String message;
        if (ex instanceof InvalidArgumentException || ex instanceof IllegalArgumentException) {
            message = ex.getMessage();
        } else if (ex instanceof ConnectionRuntimeException) {
            message = "连接失败";
        } else if (ex instanceof AuthenticationException) {
            message = "认证失败";
        } else if (ex instanceof SftpException) {
            message = "脚本上传失败";
        } else {
            message = "执行失败";
        }
        return Strings.retain(message, 250);
    }

    @Override
    public Long getHostId() {
        return execHostCommand.getHostId();
    }

}
