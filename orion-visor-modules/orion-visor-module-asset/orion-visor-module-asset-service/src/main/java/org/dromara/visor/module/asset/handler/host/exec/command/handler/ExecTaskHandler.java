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
package org.dromara.visor.module.asset.handler.host.exec.command.handler;

import cn.orionsec.kit.lang.support.timeout.TimeoutChecker;
import cn.orionsec.kit.lang.support.timeout.TimeoutCheckers;
import cn.orionsec.kit.lang.support.timeout.TimeoutEndpoint;
import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.net.host.ssh.ExitCode;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.module.asset.dao.ExecLogDAO;
import org.dromara.visor.module.asset.define.AssetThreadPools;
import org.dromara.visor.module.asset.define.config.AppExecLogConfig;
import org.dromara.visor.module.asset.define.message.ExecMessageDefine;
import org.dromara.visor.module.asset.entity.domain.ExecLogDO;
import org.dromara.visor.module.asset.enums.ExecHostStatusEnum;
import org.dromara.visor.module.asset.enums.ExecStatusEnum;
import org.dromara.visor.module.asset.handler.host.exec.command.manager.ExecTaskManager;
import org.dromara.visor.module.asset.handler.host.exec.command.model.ExecCommandDTO;
import org.dromara.visor.module.asset.handler.host.exec.command.model.ExecCommandHostDTO;
import org.dromara.visor.module.infra.api.SystemMessageApi;
import org.dromara.visor.module.infra.entity.dto.message.SystemMessageDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final SystemMessageApi systemMessageApi = SpringHolder.getBean(SystemMessageApi.class);

    private final ExecCommandDTO execCommand;

    private TimeoutChecker<TimeoutEndpoint> timeoutChecker;

    @Getter
    private final List<IExecCommandHandler> handlers;

    private Date startTime;

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
        try {
            // 更新状态
            this.updateStatus(ExecStatusEnum.RUNNING);
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
            // 检查是否发送消息
            this.checkSendMessage();
            // 移除任务
            execTaskManager.removeTask(id);
            // 释放资源
            this.close();
        }
    }

    @Override
    public void interrupt() {
        log.info("ExecTaskHandler-interrupt id: {}", execCommand.getLogId());
        handlers.forEach(IExecCommandHandler::interrupt);
    }

    @Override
    public void close() {
        log.info("ExecTaskHandler-close id: {}", execCommand.getLogId());
        Streams.close(timeoutChecker);
        this.handlers.forEach(Streams::close);
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
        try {
            String statusName = status.name();
            log.info("ExecTaskHandler-updateStatus start id: {}, status: {}", id, statusName);
            ExecLogDO update = new ExecLogDO();
            update.setId(id);
            update.setStatus(statusName);
            if (ExecStatusEnum.RUNNING.equals(status)) {
                // 执行中
                this.startTime = new Date();
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
        } catch (Exception e) {
            log.error("ExecTaskHandler-updateStatus error id: {}", id, e);
        }
    }

    /**
     * 检查是否发送消息
     */
    private void checkSendMessage() {
        // 检查是否执行失败/exitCode
        boolean hasError = handlers.stream().anyMatch(s ->
                ExecHostStatusEnum.FAILED.equals(s.getStatus())
                        || ExecHostStatusEnum.TIMEOUT.equals(s.getStatus())
                        || !ExitCode.isSuccess(s.getExitCode()));
        if (!hasError) {
            return;
        }
        // 参数
        Map<String, Object> params = new HashMap<>();
        params.put(ExtraFieldConst.ID, execCommand.getLogId());
        params.put(ExtraFieldConst.TIME, Dates.format(this.startTime, Dates.MD_HM));
        SystemMessageDTO message = SystemMessageDTO.builder()
                .receiverId(execCommand.getUserId())
                .receiverUsername(execCommand.getUsername())
                .relKey(String.valueOf(execCommand.getLogId()))
                .params(params)
                .build();
        // 发送
        systemMessageApi.create(ExecMessageDefine.EXEC_FAILED, message);
    }

}
