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
package org.dromara.visor.module.exec.handler.exec.log.tracker;

import cn.orionsec.kit.ext.tail.Tracker;
import cn.orionsec.kit.ext.tail.delay.DelayTrackerListener;
import cn.orionsec.kit.ext.tail.mode.FileNotFoundMode;
import cn.orionsec.kit.ext.tail.mode.FileOffsetMode;
import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import cn.orionsec.kit.lang.utils.Charsets;
import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.spring.SpringHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.file.FileClient;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.common.config.AppLogConfig;
import org.dromara.visor.module.exec.dao.ExecHostLogDAO;
import org.dromara.visor.module.exec.entity.domain.ExecHostLogDO;
import org.dromara.visor.module.exec.enums.ExecHostStatusEnum;
import org.dromara.visor.module.exec.handler.exec.log.constant.LogConst;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * log tracker 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 23:36
 */
@Slf4j
public class ExecLogTracker implements IExecLogTracker {

    private static final AppLogConfig appLogConfig = SpringHolder.getBean(AppLogConfig.class);

    private static final FileClient localFileClient = SpringHolder.getBean("localFileClient");

    private static final ExecHostLogDAO execHostLogDAO = SpringHolder.getBean(ExecHostLogDAO.class);

    private final WebSocketSession session;

    @Getter
    private final Long execId;

    @Getter
    private final Long execHostId;

    private Charset charset;

    private String absolutePath;

    private ExecHostLogDO execHostLog;

    private RandomAccessFile file;

    private DelayTrackerListener tracker;

    private volatile boolean close;

    public ExecLogTracker(Long execId,
                          Long execHostId,
                          WebSocketSession session) {
        this.execId = execId;
        this.execHostId = execHostId;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            // 初始化数据
            this.initData();
            // 查看日志
            if (ExecHostStatusEnum.RUNNING.name().equals(execHostLog.getStatus())) {
                // 追踪文件
                this.tailFile();
            } else {
                // 直接读取文件
                this.readFile();
            }
        } catch (InvalidArgumentException e) {
            // 业务异常
            log.error("exec log tracker init error id: {}", execHostId, e);
            // 发送消息
            this.sendMessage(e.getMessage());
        } catch (Exception e) {
            log.error("exec log tracker exec error id: {}", execHostId, e);
        } finally {
            // 释放资源
            this.close();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 读取数据
        this.execHostLog = execHostLogDAO.selectByIdAndLogId(execHostId, execId);
        Assert.notNull(execHostLog, ErrorMessage.DATA_ABSENT);
        // 检查任务状态
        Assert.neq(execHostLog.getStatus(), ExecHostStatusEnum.WAITING.name(), ErrorMessage.ILLEGAL_STATUS);
        // 获取文件路径
        this.absolutePath = localFileClient.getAbsolutePath(execHostLog.getLogPath());
        Assert.isTrue(Files1.isFile(absolutePath), ErrorMessage.FILE_ABSENT);
        // 获取编码集
        this.charset = Charsets.of(this.getCharset());
    }

    /**
     * 追踪文件
     */
    private void tailFile() {
        // 创建追踪器
        this.tracker = new DelayTrackerListener(absolutePath, this);
        tracker.charset(charset.name());
        tracker.delayMillis(appLogConfig.getTrackerLoadInterval());
        tracker.offset(FileOffsetMode.LINE, appLogConfig.getTrackerLoadLines());
        tracker.notFoundMode(FileNotFoundMode.WAIT_COUNT, Const.N_10);
        // 开始追踪
        tracker.run();
    }

    /**
     * 读取文件
     *
     * @throws IOException IOException
     */
    private void readFile() throws IOException {
        this.file = Files1.openRandomAccess(absolutePath, Const.ACCESS_R);
        // 获取文件位置
        long pos = FileReaders.readTailLinesSeek(file, appLogConfig.getTrackerLoadLines());
        // 设置文件位置
        file.seek(pos);
        // 读取到尾部
        byte[] buffer = new byte[Const.BUFFER_KB_8];
        int len;
        while ((len = file.read(buffer)) != -1) {
            // 发送消息
            this.sendMessage(new String(buffer, 0, len, charset));
        }
    }

    /**
     * 读取参数中的编码集
     *
     * @return charset
     */
    private String getCharset() {
        JSONObject params = JSON.parseObject(execHostLog.getParameter());
        if (params != null) {
            String charset = params.getString(Const.CHARSET);
            if (charset != null) {
                return charset;
            }
        }
        return Const.UTF_8;
    }

    /**
     * 发送消息
     *
     * @param message message
     */
    private void sendMessage(String message) {
        try {
            WebSockets.sendText(session, execHostId + LogConst.SEPARATOR + message);
        } catch (Exception e) {
            log.error("ExecLogTracker.send error", e);
        }
    }

    @Override
    public void setLastModify() {
        if (tracker != null) {
            tracker.setFileLastModifyTime();
        }
    }

    @Override
    public void read(byte[] bytes, int len, Tracker tracker) {
        // 发送消息
        this.sendMessage(new String(bytes, 0, len, charset));
    }

    @Override
    public void close() {
        log.info("ExecLogTracker.close execHostId: {}, closed: {}", execHostId, close);
        if (close) {
            return;
        }
        this.close = true;
        if (file != null) {
            Streams.close(file);
        }
        if (tracker != null) {
            tracker.stop();
        }
    }

}
