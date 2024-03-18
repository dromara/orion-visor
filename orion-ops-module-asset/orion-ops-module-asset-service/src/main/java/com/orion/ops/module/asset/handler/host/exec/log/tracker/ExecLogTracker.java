package com.orion.ops.module.asset.handler.host.exec.log.tracker;

import com.orion.ext.tail.Tracker;
import com.orion.ext.tail.delay.DelayTrackerListener;
import com.orion.ext.tail.mode.FileNotFoundMode;
import com.orion.ext.tail.mode.FileOffsetMode;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.entity.dto.ExecHostLogTailDTO;
import com.orion.ops.module.asset.handler.host.exec.log.constant.LogConst;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * log tracker 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 23:36
 */
@Slf4j
public class ExecLogTracker implements IExecLogTracker {

    private final WebSocketSession session;

    private final ExecHostLogTailDTO config;

    @Getter
    private final String trackerId;

    @Getter
    private final String absolutePath;

    private DelayTrackerListener tracker;

    private volatile boolean close;

    public ExecLogTracker(String trackerId,
                          String absolutePath,
                          WebSocketSession session,
                          ExecHostLogTailDTO config) {
        this.trackerId = trackerId;
        this.absolutePath = absolutePath;
        this.session = session;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            this.tracker = new DelayTrackerListener(absolutePath, this);
            tracker.charset(config.getCharset());
            tracker.delayMillis(LogConst.TRACKER_DELAY_MS);
            tracker.offset(FileOffsetMode.LINE, LogConst.TRACKER_OFFSET_LINE);
            tracker.notFoundMode(FileNotFoundMode.WAIT_COUNT, LogConst.TRACKER_WAIT_TIMES);
            // 开始监听文件
            tracker.run();
            // 监听完成回调
            // TODO test
            this.close = true;
        } catch (Exception e) {
            log.error("exec log tracker error path: {}", absolutePath, e);
        }
    }

    @Override
    public void setLastModify() {
        tracker.setFileLastModifyTime();
    }

    @Override
    public String getPath() {
        return config.getPath();
    }

    @Override
    public void read(byte[] bytes, int len, Tracker tracker) {
        WebSockets.sendText(session, config.getId() + LogConst.SEPARATOR + new String(bytes, 0, len));
    }

    @Override
    public void close() {
        // TODO test
        log.info("ExecLogTracker.close path: {}", absolutePath);
        if (close) {
            return;
        }
        this.close = true;
        if (tracker != null) {
            tracker.stop();
        }
    }

}
