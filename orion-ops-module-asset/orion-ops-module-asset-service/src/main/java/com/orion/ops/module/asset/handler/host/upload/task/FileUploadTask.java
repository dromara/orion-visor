package com.orion.ops.module.asset.handler.host.upload.task;

import com.orion.lang.utils.Threads;
import com.orion.lang.utils.io.Streams;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.module.asset.dao.UploadTaskDAO;
import com.orion.ops.module.asset.dao.UploadTaskFileDAO;
import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.entity.domain.UploadTaskDO;
import com.orion.ops.module.asset.entity.domain.UploadTaskFileDO;
import com.orion.ops.module.asset.enums.UploadTaskFileStatusEnum;
import com.orion.ops.module.asset.enums.UploadTaskStatusEnum;
import com.orion.ops.module.asset.handler.host.upload.dto.FileUploadFileItemDTO;
import com.orion.ops.module.asset.handler.host.upload.manager.FileUploadTaskManager;
import com.orion.ops.module.asset.handler.host.upload.uploader.FileUploader;
import com.orion.ops.module.asset.handler.host.upload.uploader.IFileUploader;
import com.orion.ops.module.asset.service.UploadTaskService;
import com.orion.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 上传任务 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 13:38
 */
@Slf4j
public class FileUploadTask implements IFileUploadTask {

    private static final UploadTaskDAO uploadTaskDAO = SpringHolder.getBean(UploadTaskDAO.class);

    private static final UploadTaskFileDAO uploadTaskFileDAO = SpringHolder.getBean(UploadTaskFileDAO.class);

    private static final UploadTaskService uploadTaskService = SpringHolder.getBean(UploadTaskService.class);

    private static final FileUploadTaskManager fileUploadTaskManager = SpringHolder.getBean(FileUploadTaskManager.class);

    private final Long id;

    @Getter
    private final List<IFileUploader> uploaderList;

    private UploadTaskDO record;

    private volatile boolean canceled;

    private volatile boolean closed;

    public FileUploadTask(Long id) {
        this.id = id;
        this.uploaderList = new ArrayList<>();
    }

    @Override
    public void run() {
        log.info("FileUploadTask.run start id: {}", id);
        // 查询任务
        this.record = uploadTaskDAO.selectById(id);
        if (record == null) {
            return;
        }
        // 检查任务状态 非准备中则取消执行
        if (!UploadTaskStatusEnum.WAITING.name().equals(record.getStatus())) {
            return;
        }
        try {
            // 添加任务
            fileUploadTaskManager.addTask(id, this);
            // 修改状态
            this.updateStatus(UploadTaskStatusEnum.UPLOADING);
            // 创建文件上传器
            this.createFileUploader();
            // 执行上传
            this.runUpload();
            log.info("FileUploadTask.run finish id: {}", id);
        } catch (Exception e) {
            log.error("FileUploadTask.run error id: {}", id, e);
        } finally {
            // 修改状态
            if (canceled) {
                this.updateStatus(UploadTaskStatusEnum.CANCELED);
            } else {
                this.updateStatus(UploadTaskStatusEnum.FINISHED);
            }
            // 移除任务
            fileUploadTaskManager.removeTask(id);
            // 释放资源
            this.close();
        }
    }

    @Override
    public void cancel() {
        log.info("FileUploadTask.cancel id: {}, canceled: {}, closed: {}", id, canceled, closed);
        if (this.canceled || this.closed) {
            return;
        }
        // 关闭
        this.canceled = true;
        uploaderList.forEach(IFileUploader::cancel);
        this.close();
    }

    @Override
    public void close() {
        log.info("FileUploadTask.close id: {}, canceled: {}, closed: {}", id, canceled, closed);
        if (closed) {
            return;
        }
        this.closed = true;
        // 删除临时文件
        uploadTaskService.clearUploadSwapFiles(id);
        // 关闭
        uploaderList.forEach(Streams::close);
    }

    /**
     * 创建文件上传器
     */
    private void createFileUploader() {
        // 查询文件
        List<UploadTaskFileDO> uploadFiles = uploadTaskFileDAO.selectByTaskId(id, UploadTaskFileStatusEnum.WAITING.name());
        Map<Long, List<UploadTaskFileDO>> hostFileGroup = uploadFiles.stream()
                .collect(Collectors.groupingBy(UploadTaskFileDO::getHostId));
        hostFileGroup.forEach((k, v) -> {
            // 设置上传的文件
            List<FileUploadFileItemDTO> files = v.stream()
                    .map(s -> FileUploadFileItemDTO.builder()
                            .id(s.getId())
                            .fileId(s.getFileId())
                            .remotePath(record.getRemotePath() + Const.SLASH + s.getFilePath())
                            .status(UploadTaskFileStatusEnum.WAITING.name())
                            .current(0L)
                            .build())
                    .collect(Collectors.toList());
            // 添加到上传器
            uploaderList.add(new FileUploader(id, k, files));
        });
    }

    /**
     * 执行上传
     */
    private void runUpload() throws Exception {
        if (uploaderList.size() == 1) {
            // 单个主机直接执行
            IFileUploader handler = uploaderList.get(0);
            handler.run();
        } else {
            // 多个主机异步阻塞执行
            Threads.blockRun(uploaderList, AssetThreadPools.UPLOAD_HOST);
        }
    }

    /**
     * 更新状态
     *
     * @param status status
     */
    private void updateStatus(UploadTaskStatusEnum status) {
        UploadTaskDO update = new UploadTaskDO();
        update.setId(id);
        update.setStatus(status.name());
        if (UploadTaskStatusEnum.UPLOADING.equals(status)) {
            update.setStartTime(new Date());
        } else if (UploadTaskStatusEnum.FINISHED.equals(status)) {
            update.setEndTime(new Date());
        } else if (UploadTaskStatusEnum.CANCELED.equals(status)) {
            update.setEndTime(new Date());
        }
        uploadTaskDAO.updateById(update);
    }

}
