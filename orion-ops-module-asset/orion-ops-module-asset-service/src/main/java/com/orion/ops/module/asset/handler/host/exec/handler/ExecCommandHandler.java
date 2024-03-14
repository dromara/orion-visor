package com.orion.ops.module.asset.handler.host.exec.handler;

import com.alibaba.fastjson.JSON;
import com.orion.lang.exception.AuthenticationException;
import com.orion.lang.exception.ConnectionRuntimeException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.ssh.command.CommandExecutor;
import com.orion.ops.framework.common.file.FileClient;
import com.orion.ops.module.asset.dao.ExecHostLogDAO;
import com.orion.ops.module.asset.entity.domain.ExecHostLogDO;
import com.orion.ops.module.asset.enums.ExecHostStatusEnum;
import com.orion.ops.module.asset.handler.host.exec.dto.ExecCommandHostDTO;
import com.orion.ops.module.asset.service.HostTerminalService;
import com.orion.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 11:30
 */
@Slf4j
public class ExecCommandHandler implements IExecCommandHandler {

    private final FileClient fileClient = SpringHolder.getBean("logsFileClient");

    private final HostTerminalService hostTerminalService = SpringHolder.getBean(HostTerminalService.class);

    private final ExecHostLogDAO execHostLogDAO = SpringHolder.getBean(ExecHostLogDAO.class);

    private final ExecCommandHostDTO execHostCommand;

    private final TimeoutChecker timeoutChecker;

    @Getter
    private ExecHostStatusEnum status;

    private SessionStore sessionStore;

    private CommandExecutor executor;

    private OutputStream logOutputStream;

    private volatile boolean closed;

    private volatile boolean interrupted;

    public ExecCommandHandler(ExecCommandHostDTO execHostCommand, TimeoutChecker timeoutChecker) {
        this.status = ExecHostStatusEnum.WAITING;
        this.execHostCommand = execHostCommand;
        this.timeoutChecker = timeoutChecker;
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
        } finally {
            Streams.close(this);
        }
        // 执行回调
        if (this.interrupted) {
            // 中断执行
            this.updateStatus(ExecHostStatusEnum.INTERRUPTED, null);
        } else if (ex != null) {
            // 执行失败
            this.updateStatus(ExecHostStatusEnum.FAILED, ex);
        } else if (executor.isTimeout()) {
            // 更新执行超时
            this.updateStatus(ExecHostStatusEnum.FAILED, new TimeoutException());
        } else {
            // 更新执行完成
            this.updateStatus(ExecHostStatusEnum.COMPLETED, null);
        }
    }

    /**
     * 执行命令
     *
     * @throws IOException IOException
     */
    private void execCommand() throws Exception {
        // 打开日志流
        this.logOutputStream = fileClient.getContentOutputStream(execHostCommand.getLogPath());
        // 打开会话
        this.sessionStore = hostTerminalService.openSessionStore(execHostCommand.getHostId());
        this.executor = sessionStore.getCommandExecutor(Strings.replaceCRLF(execHostCommand.getCommand()));
        // 执行命令
        executor.timeout(execHostCommand.getTimeout(), TimeUnit.SECONDS, timeoutChecker);
        executor.merge();
        executor.transfer(logOutputStream);
        executor.connect();
        executor.exec();
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
        log.info("ExecCommandHandler.updateStatus start id: {}, status: {}", id, statusName);
        ExecHostLogDO update = new ExecHostLogDO();
        update.setId(id);
        update.setStatus(statusName);
        if (ExecHostStatusEnum.RUNNING.equals(status)) {
            // 运行中
            update.setStartTime(new Date());
        } else if (ExecHostStatusEnum.COMPLETED.equals(status)) {
            // 完成
            update.setFinishTime(new Date());
            update.setExitStatus(executor.getExitCode());
        } else if (ExecHostStatusEnum.FAILED.equals(status)) {
            // 失败
            update.setFinishTime(new Date());
            update.setErrorMessage(this.getErrorMessage(ex));
        } else if (ExecHostStatusEnum.INTERRUPTED.equals(status)) {
            // 中断
            update.setFinishTime(new Date());
        }
        int effect = execHostLogDAO.updateById(update);
        log.info("ExecCommandHandler.updateStatus finish id: {}, effect: {}", id, effect);
    }

    @Override
    public void write(String msg) {
        this.executor.write(msg);
    }

    @Override
    public void interrupted() {
        log.info("ExecCommandHandler.interrupted id: {}, interrupted: {}, closed: {}",
                execHostCommand.getHostLogId(), interrupted, closed);
        if (this.interrupted || this.closed) {
            return;
        }
        // 关闭
        this.interrupted = true;
        this.close();
    }

    @Override
    public void close() {
        log.info("ExecCommandHandler.closed id: {}, closed: {}",
                execHostCommand.getHostLogId(), closed);
        if (this.closed) {
            return;
        }
        this.closed = true;
        Streams.close(executor);
        Streams.close(sessionStore);
        Streams.close(logOutputStream);
        // TODO 关闭日志
    }

    /**
     * 获取错误信息
     *
     * @param ex ex
     * @return errorMessage
     */
    private String getErrorMessage(Exception ex) {
        String message;
        if (ex instanceof TimeoutException) {
            message = "执行超时";
        } else if (ex instanceof InvalidArgumentException) {
            message = ex.getMessage();
        } else if (ex instanceof ConnectionRuntimeException) {
            message = "连接失败";
        } else if (ex instanceof AuthenticationException) {
            message = "认证失败";
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
