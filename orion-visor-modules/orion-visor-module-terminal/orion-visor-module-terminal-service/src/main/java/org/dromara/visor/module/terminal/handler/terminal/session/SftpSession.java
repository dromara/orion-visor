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
package org.dromara.visor.module.terminal.handler.terminal.session;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.sftp.SftpFile;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionSftpConfig;
import org.dromara.visor.module.terminal.handler.terminal.model.response.SftpFileVO;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISftpTerminalSender;
import org.dromara.visor.module.terminal.utils.SftpFileUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 终端 sftp 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
@Slf4j
public class SftpSession extends AbstractTerminalSession<ISftpTerminalSender, TerminalSessionSftpConfig> implements ISftpSession {

    private final SessionStore sessionStore;

    private SftpExecutor executor;

    public SftpSession(TerminalChannelProps props,
                       ISftpTerminalSender sender,
                       TerminalSessionSftpConfig config,
                       SessionStore sessionStore) {
        super(props, sender, config);
        this.sessionStore = sessionStore;
    }

    @Override
    public void connect() {
        // 打开 sftp
        this.executor = sessionStore.getSftpExecutor(config.getFileNameCharset());
        // 连接会话
        executor.connect();
        this.connected = true;
    }

    @Override
    public String getHome() {
        return executor.getHome();
    }

    @Override
    public List<SftpFileVO> list(String path, boolean showHiddenFile) {
        // 获取路径
        path = Files1.getPath(path);
        // 查询文件
        List<SftpFile> files = executor.listFilesFilter(path,
                s -> showHiddenFile || !s.getName().startsWith(Const.DOT),
                false,
                true);
        return files.stream()
                .map(this::fileMapping)
                .sorted(Comparator.comparing(SftpFileVO::getName))
                .collect(Collectors.toList());
    }

    @Override
    public void mkdir(String path) {
        path = Assert.checkNormalize(path);
        // 创建文件夹
        executor.makeDirectories(path);
    }

    @Override
    public void touch(String path) {
        path = Assert.checkNormalize(path);
        // 创建文件
        executor.touch(path);
    }

    @Override
    public void move(String source, String target) {
        // 计算路径
        source = Assert.checkNormalize(source);
        target = SftpFileUtils.getAbsoluteTargetPath(source, target);
        // 移动
        executor.move(source, target);
    }

    @Override
    public void remove(String[] paths) {
        // 删除
        Arrays.stream(paths)
                .map(Assert::checkNormalize)
                .forEach(executor::remove);
    }

    @Override
    public void truncate(String path) {
        path = Assert.checkNormalize(path);
        // 截断
        executor.truncate(path);
    }

    @Override
    public void changeMode(String path, int mod) {
        path = Assert.checkNormalize(path);
        // 修改权限
        executor.changeMode(path, mod);
    }

    @Override
    public void changeOwner(String path, int uid) {
        path = Assert.checkNormalize(path);
        // 修改归属
        executor.changeOwner(path, uid);
    }

    @Override
    public void changeGroup(String path, int gid) {
        path = Assert.checkNormalize(path);
        // 修改分组
        executor.changeGroup(path, gid);
    }

    @Override
    public List<SftpFileVO> flatDirectory(String[] paths) {
        // 展开
        return Arrays.stream(paths)
                .map(s -> executor.listFiles(s, true, false))
                .flatMap(Collection::stream)
                .map(this::fileMapping)
                .collect(Collectors.toList());
    }

    @Override
    public String getContent(String path) {
        path = Assert.checkNormalize(path);
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
        path = Assert.checkNormalize(path);
        // 写入
        try {
            executor.write(path, Strings.bytes(content, config.getFileContentCharset()));
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void checkEditPermission(String path) {
        path = Assert.checkNormalize(path);
        // 检查文件是否存在
        Assert.isTrue(executor.isExist(path), ErrorMessage.FILE_ABSENT);
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
        Streams.close(sender);
    }

    /**
     * 文件映射
     *
     * @param sftpFile sftpFile
     * @return file
     */
    private SftpFileVO fileMapping(SftpFile sftpFile) {
        // 转化文件
        SftpFileVO file = SftpFileUtils.toFile(sftpFile);
        // 设置权限
        file.setCanPreview(this.calcCanPreview(sftpFile));
        return file;
    }

    /**
     * 检查是否可预览
     *
     * @param file file
     * @return canPreview
     */
    private boolean calcCanPreview(SftpFile file) {
        // 检查文件类型及大小
        return file.isRegularFile() && file.getSize() <= Objects1.def(config.getFilePreviewSize(), Const.N_2) * 1024 * 1024;
    }

}
