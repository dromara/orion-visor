package com.orion.visor.module.asset.task;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.common.annotation.Keep;
import com.orion.visor.framework.common.file.FileClient;
import com.orion.visor.framework.common.utils.LockerUtils;
import com.orion.visor.module.asset.dao.ExecHostLogDAO;
import com.orion.visor.module.asset.define.config.AppExecLogConfig;
import com.orion.visor.module.asset.entity.domain.ExecHostLogDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 执行日志文件自动清理
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/24 23:50
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "app.exec-log.auto-clear", havingValue = "true", matchIfMissing = true)
public class ExecLogFileAutoClearTask {

    /**
     * 分布式锁名称
     */
    private static final String LOCK_KEY = "clear:elf:lock";

    @Resource
    private AppExecLogConfig appExecLogConfig;

    @Keep
    @Resource
    private FileClient logsFileClient;

    @Resource
    private ExecHostLogDAO execHostLogDAO;

    /**
     * 清理
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void clear() {
        log.info("ExecLogFileAutoClearTask.clear start");
        // 获取锁并且执行
        LockerUtils.tryLock(LOCK_KEY, this::doClearFile);
        log.info("ExecLogFileAutoClearTask.clear finish");
    }

    /**
     * 执行清理文件
     */
    private void doClearFile() {
        // 删除的时间区间
        String maxPeriod = Dates.stream()
                .subDay(appExecLogConfig.getKeepPeriod())
                .format();
        // 获取需要删除的最大id
        ExecHostLogDO hostLog = execHostLogDAO.of()
                .createWrapper()
                .select(ExecHostLogDO::getLogId, ExecHostLogDO::getLogPath)
                .lt(ExecHostLogDO::getCreateTime, maxPeriod)
                .orderByDesc(ExecHostLogDO::getId)
                .then()
                .getOne();
        if (hostLog == null) {
            return;
        }
        // 获取执行日志根目录
        String hostLogPath = logsFileClient.getAbsolutePath(hostLog.getLogPath());
        String execLogPath = Files1.getParentPath(hostLogPath);
        String parentPath = Files1.getParentPath(execLogPath);
        // 获取需要删除的文件
        List<File> files = Files1.listFilesFilter(parentPath, s -> {
            if (!Strings.isInteger(s.getName())) {
                return false;
            }
            return Long.parseLong(s.getName()) <= hostLog.getLogId();
        }, false, true);
        if (files.isEmpty()) {
            return;
        }
        // 删除日志文件
        files.forEach(Files1::delete);
    }

}
