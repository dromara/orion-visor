package com.orion.ops.module.asset.handler.host.exec.command;

import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.support.timeout.TimeoutEndpoint;
import com.orion.lang.utils.Threads;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO KIT
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/20 16:50
 */
public class TimeOutCheckerFix implements TimeoutChecker {

    private final List<TimeoutEndpoint> tasks = new ArrayList<>();

    private final long delay;

    private boolean run;

    public TimeOutCheckerFix() {
        this(DEFAULT_DELAY);
    }

    public TimeOutCheckerFix(long delay) {
        this.delay = delay;
        this.run = true;
    }

    @Override
    public <T extends TimeoutEndpoint> void addTask(T task) {
        tasks.add(task);
    }

    @Override
    public void run() {
        while (run) {
            // 完成或超时 直接移除
            tasks.removeIf(ch -> ch.isDone() || ch.checkTimeout());
            // 等待
            // 不为空则休眠
            Threads.sleep(delay);
        }
    }

    @Override
    public void close() {
        this.run = false;
    }

}
