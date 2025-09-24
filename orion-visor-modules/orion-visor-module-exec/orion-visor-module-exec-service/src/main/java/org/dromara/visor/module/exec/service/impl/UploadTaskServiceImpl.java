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
package org.dromara.visor.module.exec.service.impl;

import cn.orionsec.kit.lang.annotation.Keep;
import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.*;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.time.Dates;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.enums.EndpointDefine;
import org.dromara.visor.common.file.FileClient;
import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.common.utils.SqlUtils;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.api.AssetAuthorizedDataApi;
import org.dromara.visor.module.asset.api.HostApi;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.asset.enums.HostStatusEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.exec.convert.UploadTaskConvert;
import org.dromara.visor.module.exec.convert.UploadTaskFileConvert;
import org.dromara.visor.module.exec.dao.UploadTaskDAO;
import org.dromara.visor.module.exec.dao.UploadTaskFileDAO;
import org.dromara.visor.module.exec.entity.domain.UploadTaskDO;
import org.dromara.visor.module.exec.entity.domain.UploadTaskFileDO;
import org.dromara.visor.module.exec.entity.dto.UploadTaskExtraDTO;
import org.dromara.visor.module.exec.entity.request.upload.*;
import org.dromara.visor.module.exec.entity.vo.*;
import org.dromara.visor.module.exec.enums.UploadTaskFileStatusEnum;
import org.dromara.visor.module.exec.enums.UploadTaskStatusEnum;
import org.dromara.visor.module.exec.handler.upload.FileUploadTasks;
import org.dromara.visor.module.exec.handler.upload.manager.FileUploadTaskManager;
import org.dromara.visor.module.exec.handler.upload.model.FileUploadFileItemDTO;
import org.dromara.visor.module.exec.handler.upload.task.IFileUploadTask;
import org.dromara.visor.module.exec.handler.upload.uploader.IFileUploader;
import org.dromara.visor.module.exec.service.UploadTaskFileService;
import org.dromara.visor.module.exec.service.UploadTaskService;
import org.dromara.visor.module.infra.api.FileUploadApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 上传任务 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Slf4j
@Service
public class UploadTaskServiceImpl implements UploadTaskService {

    private static final String DEFAULT_DESC = "上传文件 {}";

    @Resource
    private UploadTaskDAO uploadTaskDAO;

    @Resource
    private UploadTaskFileDAO uploadTaskFileDAO;

    @Resource
    private UploadTaskFileService uploadTaskFileService;

    @Keep
    @Resource
    private FileClient localFileClient;

    @Resource
    private FileUploadTaskManager fileUploadTaskManager;

    @Resource
    private HostApi hostApi;

    @Resource
    private AssetAuthorizedDataApi assetAuthorizedDataApi;

    @Resource
    private FileUploadApi fileUploadApi;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UploadTaskCreateVO createUploadTask(UploadTaskCreateRequest request) {
        LoginUser user = SecurityUtils.getLoginUserNotNull();
        List<Long> hostIdList = request.getHostIdList();
        List<UploadTaskFileRequest> files = request.getFiles();
        log.info("UploadTaskService-createUploadTask request: {}", JSON.toJSONString(request));
        // 检查主机是否有权限
        this.checkHostPermission(hostIdList);
        // 查询主机信息
        List<HostDTO> hosts = this.getUploadTaskHosts(hostIdList);
        // 转换
        UploadTaskDO record = UploadTaskConvert.MAPPER.to(request);
        record.setUserId(user.getId());
        record.setUsername(user.getUsername());
        record.setDescription(Strings.def(record.getDescription(), () -> Strings.format(DEFAULT_DESC, Dates.current())));
        record.setStatus(UploadTaskStatusEnum.WAITING.name());
        record.setFileCount(files.size());
        record.setHostCount(hostIdList.size());
        UploadTaskExtraDTO extra = UploadTaskExtraDTO.builder()
                .hostIdList(hostIdList)
                .hosts(Lists.map(hosts, HostDTO::toBase))
                .build();
        record.setExtraInfo(JSON.toJSONString(extra));
        // 插入任务表
        int effect = uploadTaskDAO.insert(record);
        Long id = record.getId();
        // 插入任务文件表
        List<UploadTaskFileDO> uploadFiles = new ArrayList<>();
        for (Long hostId : hostIdList) {
            files.stream()
                    .map(s -> UploadTaskFileDO.builder()
                            .taskId(id)
                            .hostId(hostId)
                            .fileId(s.getFileId())
                            .filePath(s.getFilePath())
                            .fileSize(s.getFileSize())
                            .status(UploadTaskFileStatusEnum.WAITING.name())
                            .build())
                    .forEach(uploadFiles::add);
        }
        uploadTaskFileDAO.insertBatch(uploadFiles);
        // 设置 uploadToken
        String token = fileUploadApi.createUploadToken(user.getId(), EndpointDefine.UPLOAD_SWAP.format(id));
        log.info("UploadTaskService-createUploadTask id: {}, effect: {}", id, effect);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.NAME, record.getDescription());
        OperatorLogs.add(OperatorLogs.COUNT, files.size());
        return UploadTaskCreateVO.builder()
                .id(id)
                .token(token)
                .build();
    }

    @Override
    public UploadTaskVO getUploadTask(Long id) {
        // 查询任务
        UploadTaskDO record = uploadTaskDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 查询任务文件
        List<UploadTaskFileVO> files = uploadTaskFileService.getFileByTaskId(id);
        // 返回
        UploadTaskVO uploadTask = UploadTaskConvert.MAPPER.to(record);
        // 计算传输进度
        this.computeUploadProgress(id, files);
        // 设置任务文件
        this.setTaskHostFiles(uploadTask, files);
        return uploadTask;
    }

    @Override
    public DataGrid<UploadTaskVO> getUploadTaskPage(UploadTaskQueryRequest request) {
        // 条件
        LambdaQueryWrapper<UploadTaskDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return uploadTaskDAO.of()
                .wrapper(wrapper)
                .page(request)
                .order(request, UploadTaskDO::getId)
                .dataGrid(UploadTaskConvert.MAPPER::to);
    }

    @Override
    public List<UploadTaskStatusVO> getUploadTaskStatus(List<Long> idList, Boolean queryFiles) {
        // 查询任务
        List<UploadTaskStatusVO> tasks = uploadTaskDAO.of()
                .createWrapper()
                .select(UploadTaskDO::getId,
                        UploadTaskDO::getStatus,
                        UploadTaskDO::getStartTime,
                        UploadTaskDO::getEndTime)
                .in(UploadTaskDO::getId, idList)
                .then()
                .list(UploadTaskConvert.MAPPER::toStatus);
        if (!Booleans.isTrue(queryFiles)) {
            return tasks;
        }
        // 查询任务文件
        Map<Long, List<UploadTaskFileVO>> taskFilesMap = uploadTaskFileDAO.of()
                .createWrapper()
                .select(UploadTaskFileDO::getId,
                        UploadTaskFileDO::getTaskId,
                        UploadTaskFileDO::getHostId,
                        UploadTaskFileDO::getStatus,
                        UploadTaskFileDO::getFileSize,
                        UploadTaskFileDO::getErrorMessage,
                        UploadTaskFileDO::getStartTime,
                        UploadTaskFileDO::getEndTime)
                .in(UploadTaskFileDO::getTaskId, idList)
                .then()
                .stream()
                .map(UploadTaskFileConvert.MAPPER::to)
                .collect(Collectors.groupingBy(UploadTaskFileVO::getTaskId));
        for (UploadTaskStatusVO task : tasks) {
            Long id = task.getId();
            List<UploadTaskFileVO> files = taskFilesMap.get(id);
            if (files == null) {
                files = Lists.empty();
            } else {
                // 计算进度
                this.computeUploadProgress(id, files);
            }
            task.setFiles(files);
        }
        return tasks;
    }

    @Override
    public Long getUploadTaskCount(UploadTaskQueryRequest request) {
        // 条件
        LambdaQueryWrapper<UploadTaskDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return uploadTaskDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer clearUploadTask(UploadTaskClearRequest request) {
        // 查询id
        LambdaQueryWrapper<UploadTaskDO> wrapper = this.buildQueryWrapper(request)
                .select(UploadTaskDO::getId)
                .orderByAsc(UploadTaskDO::getId)
                .last(SqlUtils.limit(request.getLimit()));
        List<Long> idList = uploadTaskDAO.of(wrapper)
                .list(UploadTaskDO::getId);
        // 删除
        return this.deleteUploadTaskByIdList(idList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteUploadTaskById(Long id) {
        return this.deleteUploadTaskByIdList(Lists.singleton(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteUploadTaskByIdList(List<Long> idList) {
        log.info("UploadTaskService-deleteUploadTaskByIdList idList: {}", idList);
        // 查询任务
        List<UploadTaskDO> records = uploadTaskDAO.selectBatchIds(idList);
        // 取消任务
        this.checkCancelTask(records, UploadTaskStatusEnum.CANCELED);
        // 删除任务
        int effect = uploadTaskDAO.deleteBatchIds(idList);
        // 删除任务文件
        uploadTaskFileService.deleteFileByTaskIdList(idList);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        log.info("UploadTaskService-deleteUploadTaskByIdList effect: {}", effect);
        return effect;
    }

    @Override
    public void startUploadTask(Long id) {
        // 查询任务
        UploadTaskDO record = uploadTaskDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.TASK_ABSENT);
        // 检查任务状态
        Valid.eq(record.getStatus(), UploadTaskStatusEnum.WAITING.name(), ErrorMessage.ILLEGAL_STATUS);
        // 检查文件完整性
        this.checkFileCompleteness(id);
        // 执行任务
        FileUploadTasks.start(id);
    }

    @Override
    public void cancelUploadTask(UploadTaskRequest request) {
        // 查询任务
        UploadTaskDO record = uploadTaskDAO.selectById(request.getId());
        Valid.notNull(record, ErrorMessage.TASK_ABSENT);
        // 检查状态
        Valid.isTrue(UploadTaskStatusEnum.of(record.getStatus()).isCancelable(), ErrorMessage.ILLEGAL_STATUS);
        // 取消任务
        if (Booleans.isTrue(request.getFailed())) {
            this.checkCancelTask(Lists.singleton(record), UploadTaskStatusEnum.FAILED);
        } else {
            this.checkCancelTask(Lists.singleton(record), UploadTaskStatusEnum.CANCELED);
        }
    }

    @Override
    public void clearUploadSwapFiles(Long id) {
        this.clearUploadSwapFiles(Lists.singleton(id));
    }

    @Override
    public void clearUploadSwapFiles(List<Long> idList) {
        if (Lists.isEmpty(idList)) {
            return;
        }
        // 查询记录
        List<String> paths = idList.stream()
                .map(EndpointDefine.UPLOAD_SWAP::format)
                .map(localFileClient::getReturnPath)
                .map(localFileClient::getAbsolutePath)
                .collect(Collectors.toList());
        // 删除文件
        paths.forEach(Files1::delete);
    }

    @Override
    public LambdaQueryWrapper<UploadTaskDO> buildQueryWrapper(UploadTaskQueryRequest request) {
        return uploadTaskDAO.wrapper()
                .eq(UploadTaskDO::getId, request.getId())
                .eq(UploadTaskDO::getUserId, request.getUserId())
                .like(UploadTaskDO::getDescription, request.getDescription())
                .like(UploadTaskDO::getRemotePath, request.getRemotePath())
                .eq(UploadTaskDO::getStatus, request.getStatus())
                .ge(UploadTaskDO::getCreateTime, Arrays1.getIfPresent(request.getCreateTimeRange(), 0))
                .le(UploadTaskDO::getCreateTime, Arrays1.getIfPresent(request.getCreateTimeRange(), 1));
    }

    /**
     * 检查主机权限
     *
     * @param hostIdList hostIdList
     */
    private void checkHostPermission(List<Long> hostIdList) {
        // 查询有权限的主机
        List<Long> authorizedHostIdList = assetAuthorizedDataApi.getUserAuthorizedEnabledHostId(SecurityUtils.getLoginUserId(), HostTypeEnum.SSH);
        for (Long hostId : hostIdList) {
            Valid.isTrue(authorizedHostIdList.contains(hostId), Strings.format(ErrorMessage.PLEASE_CHECK_HOST_SSH, hostId));
        }
    }

    /**
     * 查询上传任务主机信息
     *
     * @param hostIdList hostIdList
     * @return hosts
     */
    public List<HostDTO> getUploadTaskHosts(List<Long> hostIdList) {
        // 查询主机信息
        List<HostDTO> hosts = hostApi.selectByIdList(hostIdList);
        // 检查主机数量
        Valid.eq(hosts.size(), hostIdList.size(), ErrorMessage.HOST_ABSENT);
        // 检查主机状态
        for (HostDTO host : hosts) {
            Valid.eq(HostStatusEnum.ENABLED.name(), host.getStatus(), ErrorMessage.HOST_NOT_ENABLED, host.getName());
        }
        return hosts;
    }

    /**
     * 检查文件完整性
     *
     * @param id id
     */
    private void checkFileCompleteness(Long id) {
        List<Long> cancelIdList = new ArrayList<>();
        // 查询任务文件
        List<UploadTaskFileDO> files = uploadTaskFileDAO.selectByTaskId(id);
        Map<String, List<UploadTaskFileDO>> fileIdGroups = files.stream()
                .collect(Collectors.groupingBy(UploadTaskFileDO::getFileId));
        fileIdGroups.forEach((k, v) -> {
            // 获取文件实际路径
            String path = localFileClient.getReturnPath(EndpointDefine.UPLOAD_SWAP.format(id) + Const.SLASH + k);
            File file = new File(localFileClient.getAbsolutePath(path));
            // 文件不存在/大小不正确
            if (!Files1.isFile(file) || file.length() != v.get(0).getFileSize()) {
                cancelIdList.addAll(Lists.map(v, UploadTaskFileDO::getId));
            }
        });
        if (cancelIdList.isEmpty()) {
            return;
        }
        // 修改任务状态
        uploadTaskFileDAO.updateToCanceledByIdList(cancelIdList);
    }

    /**
     * 检查需要取消的任务
     *
     * @param records records
     * @param status  status
     */
    private void checkCancelTask(List<UploadTaskDO> records, UploadTaskStatusEnum status) {
        // 需要取消的记录
        List<UploadTaskDO> cancelableRecords = records.stream()
                .filter(s -> UploadTaskStatusEnum.of(s.getStatus()).isCancelable())
                .collect(Collectors.toList());
        if (cancelableRecords.isEmpty()) {
            return;
        }
        // 更新状态任务
        List<Long> updateIdList = cancelableRecords.stream()
                .map(UploadTaskDO::getId)
                .filter(s -> fileUploadTaskManager.getTask(s) == null)
                .collect(Collectors.toList());
        if (!updateIdList.isEmpty()) {
            // 更新任务状态
            UploadTaskDO updateTask = new UploadTaskDO();
            updateTask.setStatus(status.name());
            updateTask.setEndTime(new Date());
            uploadTaskDAO.update(updateTask, Conditions.in(UploadTaskDO::getId, updateIdList));
            // 更新任务文件状态
            UploadTaskFileDO uploadFile = new UploadTaskFileDO();
            uploadFile.setStatus(UploadTaskFileStatusEnum.CANCELED.name());
            uploadFile.setEndTime(new Date());
            LambdaQueryWrapper<UploadTaskFileDO> updateFileQuery = uploadTaskFileDAO.wrapper()
                    .in(UploadTaskFileDO::getTaskId, updateIdList)
                    .in(UploadTaskFileDO::getStatus,
                            UploadTaskFileStatusEnum.WAITING.name(),
                            UploadTaskFileStatusEnum.UPLOADING.name());
            uploadTaskFileDAO.update(uploadFile, updateFileQuery);
        }
        // 异步处理
        Threads.start(() -> {
            // 删除文件
            this.clearUploadSwapFiles(updateIdList);
            // 取消上传
            cancelableRecords.stream()
                    .map(UploadTaskDO::getId)
                    .map(fileUploadTaskManager::getTask)
                    .filter(Objects::nonNull)
                    .forEach(IFileUploadTask::cancel);
        });
    }

    /**
     * 计算传输进度
     *
     * @param id    id
     * @param files files
     */
    private void computeUploadProgress(Long id, List<UploadTaskFileVO> files) {
        // 获取上传任务进度
        IFileUploadTask task = fileUploadTaskManager.getTask(id);
        Map<Long, FileUploadFileItemDTO> fileItemMap;
        if (task == null) {
            fileItemMap = Maps.empty();
        } else {
            fileItemMap = task.getUploaderList()
                    .stream()
                    .map(IFileUploader::getFiles)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toMap(FileUploadFileItemDTO::getId, Function.identity()));
        }
        // 设置进度
        for (UploadTaskFileVO file : files) {
            String status = file.getStatus();
            if (UploadTaskFileStatusEnum.WAITING.name().equals(status)) {
                file.setCurrent(0L);
            } else if (UploadTaskFileStatusEnum.UPLOADING.name().equals(status)) {
                // 上传中获取当前值
                Long current = Optional.ofNullable(file.getId())
                        .map(fileItemMap::get)
                        .map(FileUploadFileItemDTO::getCurrent)
                        .orElse(file.getFileSize());
                file.setCurrent(current);
            } else if (UploadTaskFileStatusEnum.FINISHED.name().equals(status)) {
                file.setCurrent(file.getFileSize());
            } else if (UploadTaskFileStatusEnum.FAILED.name().equals(status)) {
                file.setCurrent(file.getFileSize());
            } else if (UploadTaskFileStatusEnum.CANCELED.name().equals(status)) {
                file.setCurrent(file.getFileSize());
            }
        }
    }

    /**
     * 设置主机任务文件
     *
     * @param task  task
     * @param files files
     */
    private void setTaskHostFiles(UploadTaskVO task, List<UploadTaskFileVO> files) {
        Map<Long, List<UploadTaskFileVO>> hostFiles = files.stream()
                .collect(Collectors.groupingBy(UploadTaskFileVO::getHostId));
        List<UploadTaskHostVO> hosts = JSON.parseObject(task.getExtraInfo(), UploadTaskExtraDTO.class)
                .getHosts()
                .stream()
                .map(s -> UploadTaskHostVO.builder()
                        .id(s.getId())
                        .code(s.getCode())
                        .name(s.getName())
                        .address(s.getAddress())
                        .files(hostFiles.getOrDefault(s.getId(), Lists.empty()))
                        .build())
                .collect(Collectors.toList());
        task.setHosts(hosts);
    }

}
