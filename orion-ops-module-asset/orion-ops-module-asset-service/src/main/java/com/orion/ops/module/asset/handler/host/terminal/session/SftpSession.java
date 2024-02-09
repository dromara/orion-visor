package com.orion.ops.module.asset.handler.host.terminal.session;

import com.orion.lang.utils.io.FileType;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.net.host.sftp.SftpFile;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalConfig;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpFileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 终端 ssh 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
@Slf4j
public class SftpSession extends TerminalSession implements ISftpSession {

    private final TerminalConfig config;

    private final SessionStore sessionStore;

    private SftpExecutor executor;

    public SftpSession(String sessionId,
                       WebSocketSession channel,
                       SessionStore sessionStore,
                       TerminalConfig config) {
        super(sessionId, channel);
        this.sessionStore = sessionStore;
        this.config = config;
    }

    @Override
    public void connect() {
        // 打开 shell
        this.executor = sessionStore.getSftpExecutor(config.getFileNameCharset());
        executor.connect();
    }

    @Override
    public String getHome() {
        return executor.getHome();
    }

    @Override
    public List<SftpFileResponse> list(String path, boolean showHiddenFile) {
        // 查询文件
        List<SftpFile> files = executor.listFilesFilter(path,
                s -> showHiddenFile || !s.getName().startsWith(Const.DOT),
                false,
                true);
        return files.stream()
                .map(SftpSession::fileMapping)
                .collect(Collectors.toList());
    }

    @Override
    public void keepAlive() {
        try {
            // 发送个信号 保证 socket 不自动关闭
            executor.sendSignal(Const.EMPTY);
        } catch (Exception e) {
            log.error("sftp keep-alive error {}", sessionId, e);
        }
    }

    @Override
    protected void releaseResource() {
        Streams.close(executor);
        Streams.close(sessionStore);
    }

    /**
     * 文件映射
     *
     * @param sftpFile sftpFile
     * @return file
     */
    private static SftpFileResponse fileMapping(SftpFile sftpFile) {
        SftpFileResponse file = new SftpFileResponse();
        file.setName(sftpFile.getName());
        file.setPath(sftpFile.getPath());
        file.setSuffix(Files1.getSuffix(sftpFile.getName()));
        file.setSize(Files1.getSize(sftpFile.getSize()));
        file.setSizeByte(sftpFile.getSize());
        file.setPermission(sftpFile.getPermission());
        file.setUid(sftpFile.getUid());
        file.setGid(sftpFile.getGid());
        file.setAttr(sftpFile.getPermissionString());
        file.setModifyTime(sftpFile.getModifyTime());
        Boolean isDir = Optional.ofNullable(FileType.of(file.getAttr()))
                .map(FileType.DIRECTORY::equals)
                .orElse(false);
        file.setIsDir(isDir);
        return file;
    }

}
