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
package org.dromara.visor.module.asset.handler.host.terminal.session;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.FileType;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.sftp.SftpFile;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.asset.handler.host.terminal.model.TerminalConfig;
import org.dromara.visor.module.asset.handler.host.terminal.model.response.SftpFileVO;
import org.springframework.web.socket.WebSocketSession;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 终端 sftp 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
@Slf4j
public class SftpSession extends TerminalSession implements ISftpSession {

    private final SessionStore sessionStore;

    private SftpExecutor executor;

    public SftpSession(String sessionId,
                       WebSocketSession channel,
                       SessionStore sessionStore,
                       TerminalConfig config) {
        super(sessionId, channel, config);
        this.sessionStore = sessionStore;
    }

    @Override
    public void connect() {
        // 打开 sftp
        this.executor = sessionStore.getSftpExecutor(config.getFileNameCharset());
        executor.connect();
    }

    @Override
    public String getHome() {
        return executor.getHome();
    }

    @Override
    public List<SftpFileVO> list(String path, boolean showHiddenFile) {
        path = Files1.getPath(path);
        // 查询文件
        List<SftpFile> files = executor.listFilesFilter(path,
                s -> showHiddenFile || !s.getName().startsWith(Const.DOT),
                false,
                true);
        return files.stream()
                .map(SftpSession::fileMapping)
                .sorted(Comparator.comparing(SftpFileVO::getName))
                .collect(Collectors.toList());
    }

    @Override
    public void mkdir(String path) {
        path = Valid.checkNormalize(path);
        executor.makeDirectories(path);
    }

    @Override
    public void touch(String path) {
        path = Valid.checkNormalize(path);
        executor.touch(path);
    }

    @Override
    public void move(String source, String target) {
        source = Valid.checkNormalize(source);
        // 移动
        executor.move(source, target);
    }

    @Override
    public void remove(String[] paths) {
        Arrays.stream(paths)
                .map(Valid::checkNormalize)
                .forEach(executor::remove);
    }

    @Override
    public void truncate(String path) {
        path = Valid.checkNormalize(path);
        executor.truncate(path);
    }

    @Override
    public void chmod(String path, int mod) {
        path = Valid.checkNormalize(path);
        executor.changeMode(path, mod);
    }

    @Override
    public List<SftpFileVO> flatDirectory(String[] paths) {
        return Arrays.stream(paths)
                .map(s -> executor.listFiles(s, true, false))
                .flatMap(Collection::stream)
                .map(SftpSession::fileMapping)
                .collect(Collectors.toList());
    }

    @Override
    public String getContent(String path) {
        path = Valid.checkNormalize(path);
        try {
            // 获取文件
            SftpFile file = executor.getFile(path);
            if (file == null || file.getSize() == 0L) {
                return Const.EMPTY;
            }
            // 读取文件
            InputStream in = executor.openInputStream(path);
            return Streams.toString(in, config.getFileContentCharset());
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            // 关闭 inputStream 可能会被阻塞 ???...??? 只能关闭 executor
            Streams.close(this.executor);
            this.connect();
        }
    }

    @Override
    public void setContent(String path, String content) {
        path = Valid.checkNormalize(path);
        try {
            executor.write(path, Strings.bytes(content, config.getFileContentCharset()));
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void checkCanEdit(String path) {
        path = Valid.checkNormalize(path);
        // 检查文件是否存在
        Valid.isTrue(executor.isExist(path), ErrorMessage.FILE_ABSENT);
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
    private static SftpFileVO fileMapping(SftpFile sftpFile) {
        SftpFileVO file = new SftpFileVO();
        file.setName(sftpFile.getName());
        file.setPath(sftpFile.getPath());
        file.setSuffix(Files1.getSuffix(sftpFile.getName()));
        file.setSize(sftpFile.getSize());
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
