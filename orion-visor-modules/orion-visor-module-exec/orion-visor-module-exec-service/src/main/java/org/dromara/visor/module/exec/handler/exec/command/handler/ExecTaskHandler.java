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

import cn.orionsec.kit.lang.id.UUIds;
import cn.orionsec.kit.lang.support.timeout.TimeoutChecker;
import cn.orionsec.kit.lang.support.timeout.TimeoutCheckers;
import cn.orionsec.kit.lang.support.timeout.TimeoutEndpoint;
import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.net.host.ssh.ExitCode;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.module.common.config.AppLogConfig;
import org.dromara.visor.module.exec.dao.ExecLogDAO;
import org.dromara.visor.module.exec.define.ExecThreadPools;
import org.dromara.visor.module.exec.define.message.ExecMessageDefine;
import org.dromara.visor.module.exec.entity.domain.ExecLogDO;
import org.dromara.visor.module.exec.enums.ExecHostStatusEnum;
import org.dromara.visor.module.exec.enums.ExecStatusEnum;
import org.dromara.visor.module.exec.handler.exec.command.manager.ExecTaskManager;
import org.dromara.visor.module.exec.utils.ExecUtils;
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

    private static final AppLogConfig appLogConfig = SpringHolder.getBean(AppLogConfig.class);

    private static final ExecLogDAO execLogDAO = SpringHolder.getBean(ExecLogDAO.class);

    private static final ExecTaskManager execTaskManager = SpringHolder.getBean(ExecTaskManager.class);

    private static final SystemMessageApi systemMessageApi = SpringHolder.getBean(SystemMessageApi.class);

    private final Long id;

    private final List<Long> execHostIdList;

    private ExecLogDO execLog;

    private Map<String, Object> builtParams;

    private TimeoutChecker<TimeoutEndpoint> timeoutChecker;

    @Getter
    private final List<IExecCommandHandler> handlers;

    private Date startTime;

    public ExecTaskHandler(Long id, List<Long> execHostIdList) {
        this.id = id;
        this.execHostIdList = execHostIdList;
        this.handlers = Lists.newList();
    }

    @Override
    public void run() {
        log.info("ExecTaskHandler start id: {}", id);
        // 初始化数据
        if (!this.initData()) {
            return;
        }
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
        log.info("ExecTaskHandler-interrupt id: {}", id);
        handlers.forEach(IExecCommandHandler::interrupt);
    }

    @Override
    public void close() {
        log.info("ExecTaskHandler-close id: {}", id);
        Streams.close(timeoutChecker);
        this.handlers.forEach(Streams::close);
    }

    /**
     * 初始化数据
     *
     * @return pass
     */
    private boolean initData() {
        try {
            // 查询任务
            this.execLog = execLogDAO.selectById(id);
            Assert.notNull(execLog, ErrorMessage.TASK_ABSENT);
            Assert.eq(execLog.getStatus(), ExecStatusEnum.WAITING.name(), ErrorMessage.ILLEGAL_STATUS);
            // 获取内置变量
            this.builtParams = this.getBaseBuiltinParams();
            return true;
        } catch (Exception e) {
            log.error("ExecTaskHandler.init error id: {}", id, e);
            return false;
        }
    }

    /**
     * 执行主机命令
     *
     * @throws Exception Exception
     */
    private void runHostCommand() throws Exception {
        // 超时检查
        if (execLog.getTimeout() != 0) {
            this.timeoutChecker = TimeoutCheckers.create();
            ExecThreadPools.TIMEOUT_CHECK.execute(this.timeoutChecker);
        }
        // 执行命令
        if (execHostIdList.size() == 1) {
            // 单个主机直接执行
            IExecCommandHandler handler = this.createCommandHandler(execHostIdList.get(0));
            handlers.add(handler);
            handler.run();
        } else {
            execHostIdList.stream()
                    .map(this::createCommandHandler)
                    .forEach(handlers::add);
            // 多个主机异步阻塞执行
            Threads.blockRun(handlers, ExecThreadPools.EXEC_HOST);
        }
    }

    /**
     * 创建命令执行器
     *
     * @param execHostId execHostId
     * @return handler
     */
    private IExecCommandHandler createCommandHandler(Long execHostId) {
        if (Booleans.isTrue(appLogConfig.getExecDetailEnabled())) {
            // 详细日志
            return new ExecCommandDetailHandler(execHostId, execLog, builtParams, timeoutChecker);
        } else {
            // 原始日志
            return new ExecCommandOriginHandler(execHostId, execLog, builtParams, timeoutChecker);
        }
    }

    /**
     * 更新状态
     *
     * @param status status
     */
    private void updateStatus(ExecStatusEnum status) {
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
        params.put(ExtraFieldConst.ID, id);
        params.put(ExtraFieldConst.TIME, Dates.format(this.startTime, Dates.MD_HM));
        SystemMessageDTO message = SystemMessageDTO.builder()
                .receiverId(execLog.getUserId())
                .receiverUsername(execLog.getUsername())
                .relKey(String.valueOf(id))
                .params(params)
                .build();
        // 发送
        systemMessageApi.create(ExecMessageDefine.EXEC_FAILED, message);
    }

    /**
     * 获取基础内置参数
     *
     * @return params
     */
    private Map<String, Object> getBaseBuiltinParams() {
        String uuid = UUIds.random();
        Date date = new Date();
        // 输入参数
        Map<String, Object> params = ExecUtils.extraSchemaParams(execLog.getParameterSchema());
        // 添加内置参数
        params.put("userId", execLog.getUserId());
        params.put("username", execLog.getUsername());
        params.put("source", execLog.getSource());
        params.put("sourceId", execLog.getSourceId());
        params.put("seq", execLog.getExecSeq());
        params.put("execId", id);
        params.put("scriptExec", execLog.getScriptExec());
        params.put("uuid", uuid);
        params.put("uuidShort", uuid.replace("-", Strings.EMPTY));
        params.put("timestampMillis", date.getTime());
        params.put("timestamp", date.getTime() / Dates.SECOND_STAMP);
        params.put("date", Dates.format(date, Dates.YMD));
        params.put("datetime", Dates.format(date, Dates.YMD_HMS));
        return params;
    }

}
