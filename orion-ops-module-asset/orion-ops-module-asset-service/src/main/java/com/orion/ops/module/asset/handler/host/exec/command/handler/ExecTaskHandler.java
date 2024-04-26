package com.orion.ops.module.asset.handler.host.exec.command.handler;

import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.support.timeout.TimeoutCheckers;
import com.orion.lang.support.timeout.TimeoutEndpoint;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Streams;
import com.orion.ops.module.asset.dao.ExecLogDAO;
import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.define.config.AppExecLogConfig;
import com.orion.ops.module.asset.entity.domain.ExecLogDO;
import com.orion.ops.module.asset.enums.ExecStatusEnum;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandDTO;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandHostDTO;
import com.orion.ops.module.asset.handler.host.exec.command.manager.ExecTaskManager;
import com.orion.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 命令执行任务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 11:43
 */
@Slf4j
public class ExecTaskHandler implements IExecTaskHandler {

    private static final ExecLogDAO execLogDAO = SpringHolder.getBean(ExecLogDAO.class);

    private static final ExecTaskManager execTaskManager = SpringHolder.getBean(ExecTaskManager.class);

    private static final AppExecLogConfig appExecLogConfig = SpringHolder.getBean(AppExecLogConfig.class);

    private final ExecCommandDTO execCommand;

    private TimeoutChecker<TimeoutEndpoint> timeoutChecker;

    @Getter
    private final List<IExecCommandHandler> handlers;

    public ExecTaskHandler(ExecCommandDTO execCommand) {
        this.execCommand = execCommand;
        this.handlers = Lists.newList();
    }

    @Override
    public void run() {
        Long id = execCommand.getLogId();
        // 添加任务
        execTaskManager.addTask(id, this);
        log.info("ExecTaskHandler.run start id: {}", id);
        // 更新状态
        this.updateStatus(ExecStatusEnum.RUNNING);
        try {
            // 执行命令
            this.runHostCommand();
            // 更新状态-执行完成
            log.info("ExecTaskHandler.run completed id: {}", id);
            this.updateStatus(ExecStatusEnum.COMPLETED);
        } catch (Exception e) {
            // 更新状态-执行失败
            this.updateStatus(ExecStatusEnum.FAILED);
            log.error("ExecTaskHandler.run error id: {}", id, e);
        } finally {
            // 释放资源
            Streams.close(this);
            // 移除任务
            execTaskManager.removeTask(id);
        }
    }

    @Override
    public void interrupt() {
        log.info("ExecTaskHandler-interrupt id: {}", execCommand.getLogId());
        handlers.forEach(IExecCommandHandler::interrupt);
    }

    /**
     * 执行主机命令
     *
     * @throws Exception Exception
     */
    private void runHostCommand() throws Exception {
        // 超时检查
        if (execCommand.getTimeout() != 0) {
            this.timeoutChecker = TimeoutCheckers.create();
            AssetThreadPools.TIMEOUT_CHECK.execute(this.timeoutChecker);
        }
        // 执行命令
        List<ExecCommandHostDTO> hosts = execCommand.getHosts();
        if (hosts.size() == 1) {
            // 单个主机直接执行
            IExecCommandHandler handler = this.createCommandHandler(hosts.get(0));
            handlers.add(handler);
            handler.run();
        } else {
            hosts.stream()
                    .map(this::createCommandHandler)
                    .forEach(handlers::add);
            // 多个主机异步阻塞执行
            Threads.blockRun(handlers, AssetThreadPools.EXEC_HOST);
        }
    }

    /**
     * 创建命令执行器
     *
     * @param host host
     * @return handler
     */
    private IExecCommandHandler createCommandHandler(ExecCommandHostDTO host) {
        if (Booleans.isTrue(appExecLogConfig.getAppendAnsi())) {
            // ansi 日志
            return new ExecCommandAnsiHandler(execCommand, host, timeoutChecker);
        } else {
            // 原始日志
            return new ExecCommandOriginHandler(execCommand, host, timeoutChecker);
        }
    }

    /**
     * 更新状态
     *
     * @param status status
     */
    private void updateStatus(ExecStatusEnum status) {
        Long id = execCommand.getLogId();
        String statusName = status.name();
        log.info("ExecTaskHandler-updateStatus start id: {}, status: {}", id, statusName);
        ExecLogDO update = new ExecLogDO();
        update.setId(id);
        update.setStatus(statusName);
        if (ExecStatusEnum.RUNNING.equals(status)) {
            // 执行中
            update.setStartTime(new Date());
        } else if (ExecStatusEnum.COMPLETED.equals(status)) {
            // 执行完成
            update.setFinishTime(new Date());
        } else if (ExecStatusEnum.FAILED.equals(status)) {
            // 执行失败
            update.setFinishTime(new Date());
        }
        int effect = execLogDAO.updateById(update);
        log.info("ExecTaskHandler-updateStatus finish id: {}, effect: {}", id, effect);
    }

    @Override
    public void close() {
        log.info("ExecTaskHandler-close id: {}", execCommand.getLogId());
        Streams.close(timeoutChecker);
        this.handlers.forEach(Streams::close);
    }

}
