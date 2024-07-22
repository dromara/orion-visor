package com.orion.visor.module.asset.utils;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Files1;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.net.host.sftp.SftpFile;
import com.orion.visor.module.asset.define.config.AppSftpConfig;
import com.orion.visor.module.asset.handler.host.transfer.model.SftpFileBackupParams;

/**
 * sftp 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 16:17
 */
public class SftpUtils {

    private SftpUtils() {
    }

    /**
     * 检查上传文件是否存在 并且执行响应策略
     *
     * @param config   config
     * @param executor executor
     * @param path     path
     */
    public static void checkUploadFilePresent(AppSftpConfig config, SftpExecutor executor, String path) {
        // 重复不备份
        if (!Booleans.isTrue(config.getUploadPresentBackup())) {
            return;
        }
        // 检查文件是否存在
        SftpFile file = executor.getFile(path);
        if (file != null) {
            // 文件存在则备份
            SftpFileBackupParams backupParams = new SftpFileBackupParams(file.getName(), System.currentTimeMillis());
            String target = Strings.format(config.getBackupFileName(), JSON.parseObject(JSON.toJSONString(backupParams)));
            // 移动
            // FIXME kit
            move(executor, path, target);
            // executor.move(path, target);
        }
    }

    /**
     * 移动文件
     * FIXME kit DELETE
     *
     * @param executor executor
     * @param source   source
     * @param target   target
     */
    public static void move(SftpExecutor executor, String source, String target) {
        try {
            source = Files1.getPath(source);
            target = Files1.getPath(target);
            if (target.charAt(0) == '/') {
                // 检查是否需要创建目标文件目录
                if (!isSameParentPath(source, target)) {
                    executor.makeDirectories(Files1.getParentPath(target));
                }
                // 绝对路径
                executor.getChannel().rename(source, Files1.getPath(Files1.normalize(target)));
            } else {
                // 相对路径
                executor.getChannel().rename(source, Files1.getPath(Files1.normalize(Files1.getPath(source + "/../" + target))));
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    /**
     * FIXME kit DELETE
     *
     * @param source source
     * @param target target
     * @return res
     */
    private static boolean isSameParentPath(String source, String target) {
        return Files1.getParentPath(source).equals(Files1.getParentPath(target));
    }

}
