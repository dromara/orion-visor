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
package org.dromara.visor.module.exec.handler.upload.uploader;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.common.enums.EndpointDefine;
import org.dromara.visor.common.file.FileClient;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.ssh.SessionStores;
import org.dromara.visor.common.utils.PathUtils;
import org.dromara.visor.module.asset.api.HostConnectApi;
import org.dromara.visor.module.asset.enums.HostOsTypeEnum;
import org.dromara.visor.module.common.utils.SftpUtils;
import org.dromara.visor.module.exec.dao.UploadTaskFileDAO;
import org.dromara.visor.module.exec.entity.domain.UploadTaskFileDO;
import org.dromara.visor.module.exec.enums.UploadTaskFileStatusEnum;
import org.dromara.visor.module.exec.handler.upload.model.FileUploadConfigDTO;
import org.dromara.visor.module.exec.handler.upload.model.FileUploadFileItemDTO;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 主机文件上传器 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 13:41
 */
@Slf4j
public class FileUploader implements IFileUploader {

    private static final int ERROR_MESSAGE_LEN = 1024;

    private static final HostConnectApi hostConnectApi = SpringHolder.getBean(HostConnectApi.class);

    private static final UploadTaskFileDAO uploadTaskFileDAO = SpringHolder.getBean(UploadTaskFileDAO.class);

    private static final FileClient localFileClient = SpringHolder.getBean("localFileClient");

    private SshConnectConfig connectConfig;

    private SessionStore sessionStore;

    private SftpExecutor executor;

    private final Long taskId;

    private final Long hostId;

    private final FileUploadConfigDTO config;

    @Getter
    private final List<FileUploadFileItemDTO> files;

    private InputStream inputStream;

    private OutputStream outputStream;

    private volatile boolean canceled;

    private volatile boolean closed;

    public FileUploader(FileUploadConfigDTO config) {
        this.taskId = config.getTaskId();
        this.hostId = config.getHostId();
        this.files = config.getFiles();
        this.config = config;
    }

    @Override
    public void run() {
        try {
            // 初始化会话
            boolean run = this.initSession();
            if (!run) {
                return;
            }
            // 计算实际上传路径
            this.calcRemotePath();
            // 上传文件
            for (FileUploadFileItemDTO file : files) {
                if (closed) {
                    break;
                }
                // 上传
                this.uploadFile(file);
            }
            // 检查是否取消
            this.finishCheckCancel();
        } finally {
            // 释放资源
            this.close();
        }
    }

    /**
     * 初始化会话
     *
     * @return 是否执行
     */
    private boolean initSession() {
        log.info("HostFileUploader.initSession start taskId: {}, hostId: {}", taskId, hostId);
        try {
            // 打开会话
            this.connectConfig = hostConnectApi.getSshConnectConfig(hostId, config.getUserId());
            this.sessionStore = SessionStores.openSessionStore(connectConfig);
            this.executor = sessionStore.getSftpExecutor(connectConfig.getFileNameCharset());
            executor.connect();
            log.info("HostFileUploader.initSession success taskId: {}, hostId: {}", taskId, hostId);
            return true;
        } catch (Exception e) {
            log.error("HostFileUploader.initSession error taskId: {}, hostId: {}", taskId, hostId, e);
            // 修改状态
            uploadTaskFileDAO.updateToFailedByTaskHostId(taskId, hostId, ErrorMessage.getErrorMessage(e, ErrorMessage.CONNECT_ERROR));
            files.forEach(s -> s.setStatus(UploadTaskFileStatusEnum.FAILED.name()));
            return false;
        }
    }

    /**
     * 计算实际上传路径
     */
    public void calcRemotePath() {
        // 计算上传目录
        String remotePath = config.getRemotePath();
        boolean containsEnv = remotePath.contains(Const.DOLLAR);
        if (containsEnv) {
            // 替换占位符
            String username = connectConfig.getUsername();
            String home = PathUtils.getHomePath(HostOsTypeEnum.WINDOWS.is(connectConfig.getOsType()), username);
            // 替换环境变量路径
            Map<String, String> env = Maps.newMap(4);
            env.put(ExtraFieldConst.USERNAME, username);
            env.put(ExtraFieldConst.HOME, home);
            // 设置主机上传路径
            config.setRemotePath(Files1.getPath(Strings.format(remotePath, env)));
        }
    }

    /**
     * 上传文件
     *
     * @param file file
     */
    private void uploadFile(FileUploadFileItemDTO file) {
        log.info("HostFileUploader.uploadFile start taskId: {}, hostId: {}, id: {}", taskId, hostId, file.getId());
        String path = Files1.getPath(config.getRemotePath(), file.getFilePath());
        try {
            // 修改为上传中
            this.updateToUploading(file, path);
            // 获取本地文件路径
            String endpoint = EndpointDefine.UPLOAD_SWAP.format(taskId);
            String localPath = localFileClient.getReturnPath(endpoint + Const.SLASH + file.getFileId());
            // 检查文件是否存在
            SftpUtils.checkUploadFilePresent(executor, path);
            // 打开输出流
            this.inputStream = localFileClient.getContentInputStream(localPath);
            this.outputStream = executor.openOutputStream(path);
            // 传输
            byte[] buffer = new byte[executor.getBufferSize()];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
                file.setCurrent(file.getCurrent() + read);
            }
            outputStream.flush();
            // 修改状态
            this.updateToFinished(file);
            log.info("HostFileUploader.uploadFile finish taskId: {}, hostId: {}, id: {}", taskId, hostId, file.getId());
        } catch (Exception e) {
            log.error("HostFileUploader.uploadFile error taskId: {}, hostId: {}, id: {}, canceled: {}", taskId, hostId, file.getId(), canceled, e);
            // 修改状态
            if (canceled) {
                this.updateToCanceled(file);
            } else {
                this.updateToFailed(file, ErrorMessage.getErrorMessage(e, ErrorMessage.FILE_UPLOAD_ERROR));
            }
        } finally {
            // 释放文件
            this.releaseFileResource();
        }
    }

    /**
     * 释放文件资源
     */
    private void releaseFileResource() {
        Streams.close(outputStream);
        Streams.close(inputStream);
    }

    /**
     * 检查是否取消
     */
    private void finishCheckCancel() {
        if (!canceled) {
            return;
        }
        // 将等待中的文件修改为已取消
        List<Long> idList = files.stream()
                .filter(s -> UploadTaskFileStatusEnum.WAITING.name().equals(s.getStatus()))
                .map(FileUploadFileItemDTO::getId)
                .collect(Collectors.toList());
        if (idList.isEmpty()) {
            return;
        }
        // 修改状态
        uploadTaskFileDAO.updateToCanceledByIdList(idList);
    }

    /**
     * 修改为上传中
     *
     * @param file         file
     * @param realFilePath realFilePath
     */
    private void updateToUploading(FileUploadFileItemDTO file, String realFilePath) {
        UploadTaskFileDO record = this.getUpdateRecord(file, UploadTaskFileStatusEnum.UPLOADING);
        record.setRealFilePath(realFilePath);
        record.setStartTime(new Date());
        uploadTaskFileDAO.updateById(record);
    }

    /**
     * 修改为完成
     *
     * @param file file
     */
    private void updateToFinished(FileUploadFileItemDTO file) {
        UploadTaskFileDO record = this.getUpdateRecord(file, UploadTaskFileStatusEnum.FINISHED);
        record.setEndTime(new Date());
        uploadTaskFileDAO.updateById(record);
    }

    /**
     * 修改为失败
     *
     * @param file         file
     * @param errorMessage errorMessage
     */
    private void updateToFailed(FileUploadFileItemDTO file, String errorMessage) {
        UploadTaskFileDO record = this.getUpdateRecord(file, UploadTaskFileStatusEnum.FAILED);
        record.setErrorMessage(errorMessage);
        record.setEndTime(new Date());
        uploadTaskFileDAO.updateById(record);
    }

    /**
     * 修改为取消
     *
     * @param file file
     */
    private void updateToCanceled(FileUploadFileItemDTO file) {
        UploadTaskFileDO record = this.getUpdateRecord(file, UploadTaskFileStatusEnum.CANCELED);
        record.setEndTime(new Date());
        uploadTaskFileDAO.updateById(record);
    }

    /**
     * 获取修改记录
     *
     * @param file   file
     * @param status status
     * @return record
     */
    private UploadTaskFileDO getUpdateRecord(FileUploadFileItemDTO file, UploadTaskFileStatusEnum status) {
        file.setStatus(status.name());
        UploadTaskFileDO update = new UploadTaskFileDO();
        update.setId(file.getId());
        update.setStatus(status.name());
        return update;
    }

    @Override
    public void cancel() {
        log.info("HostFileUploader.cancel taskId: {}, hostId: {}, canceled: {}, closed: {}", taskId, hostId, canceled, closed);
        if (this.canceled || this.closed) {
            return;
        }
        // 关闭
        this.canceled = true;
        this.close();
    }

    @Override
    public void close() {
        log.info("HostFileUploader.close taskId: {}, hostId: {}, closed: {}", taskId, hostId, closed);
        if (closed) {
            return;
        }
        this.closed = true;
        // 释放文件资源
        this.releaseFileResource();
        // 释放连接资源
        Streams.close(executor);
        Streams.close(sessionStore);
    }

}
