package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 主机终端 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("asset:host-terminal")
public class HostTerminalOperatorType extends InitializingOperatorTypes {

    public static final String CONNECT = "host-terminal:connect";

    public static final String SFTP_MKDIR = "host-terminal:sftp-mkdir";

    public static final String SFTP_TOUCH = "host-terminal:sftp-touch";

    public static final String SFTP_MOVE = "host-terminal:sftp-move";

    public static final String SFTP_REMOVE = "host-terminal:sftp-remove";

    public static final String SFTP_TRUNCATE = "host-terminal:sftp-truncate";

    public static final String SFTP_CHMOD = "host-terminal:sftp-chmod";

    public static final String SFTP_SET_CONTENT = "host-terminal:sftp-set-content";

    public static final String SFTP_UPLOAD = "host-terminal:sftp-upload";

    public static final String SFTP_DOWNLOAD = "host-terminal:sftp-download";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CONNECT, "连接主机 ${connectType} <sb>${hostName}</sb>"),
                new OperatorType(L, SFTP_MKDIR, "创建文件夹 ${hostName} <sb>${path}</sb>"),
                new OperatorType(L, SFTP_TOUCH, "创建文件 ${hostName} <sb>${path}</sb>"),
                new OperatorType(M, SFTP_MOVE, "移动文件 ${hostName} <sb>${path}</sb> 至 <sb>${target}</sb>"),
                new OperatorType(H, SFTP_REMOVE, "删除文件 ${hostName} <sb>${path}</sb>"),
                new OperatorType(H, SFTP_TRUNCATE, "截断文件 ${hostName} <sb>${path}</sb>"),
                new OperatorType(M, SFTP_CHMOD, "文件提权 ${hostName} <sb>${path}</sb> <sb>${mod}</sb>"),
                new OperatorType(M, SFTP_SET_CONTENT, "修改文件内容 ${hostName} <sb>${path}</sb>"),
                new OperatorType(M, SFTP_UPLOAD, "上传文件 ${hostName} <sb>${path}</sb>"),
                new OperatorType(M, SFTP_DOWNLOAD, "下载文件 ${hostName} <sb>${path}</sb>"),
        };
    }

}
