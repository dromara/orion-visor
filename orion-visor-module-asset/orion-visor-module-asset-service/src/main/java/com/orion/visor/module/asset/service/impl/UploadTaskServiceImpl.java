package com.orion.visor.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.annotation.Keep;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.file.FileClient;
import com.orion.visor.framework.common.security.LoginUser;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.mybatis.core.query.Conditions;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.asset.convert.HostConvert;
import com.orion.visor.module.asset.convert.UploadTaskConvert;
import com.orion.visor.module.asset.convert.UploadTaskFileConvert;
import com.orion.visor.module.asset.dao.HostDAO;
import com.orion.visor.module.asset.dao.UploadTaskDAO;
import com.orion.visor.module.asset.dao.UploadTaskFileDAO;
import com.orion.visor.module.asset.entity.domain.UploadTaskDO;
import com.orion.visor.module.asset.entity.domain.UploadTaskFileDO;
import com.orion.visor.module.asset.entity.dto.UploadTaskExtraDTO;
import com.orion.visor.module.asset.entity.request.upload.UploadTaskCreateRequest;
import com.orion.visor.module.asset.entity.request.upload.UploadTaskFileRequest;
import com.orion.visor.module.asset.entity.request.upload.UploadTaskQueryRequest;
import com.orion.visor.module.asset.entity.request.upload.UploadTaskRequest;
import com.orion.visor.module.asset.entity.vo.*;
import com.orion.visor.module.asset.enums.HostConfigTypeEnum;
import com.orion.visor.module.asset.enums.UploadTaskFileStatusEnum;
import com.orion.visor.module.asset.enums.UploadTaskStatusEnum;
import com.orion.visor.module.asset.handler.host.upload.FileUploadTasks;
import com.orion.visor.module.asset.handler.host.upload.model.FileUploadFileItemDTO;
import com.orion.visor.module.asset.handler.host.upload.manager.FileUploadTaskManager;
import com.orion.visor.module.asset.handler.host.upload.task.IFileUploadTask;
import com.orion.visor.module.asset.handler.host.upload.uploader.IFileUploader;
import com.orion.visor.module.asset.service.AssetAuthorizedDataService;
import com.orion.visor.module.asset.service.UploadTaskFileService;
import com.orion.visor.module.asset.service.UploadTaskService;
import com.orion.visor.module.infra.api.FileUploadApi;
import lombok.extern.slf4j.Slf4j;
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
    private HostDAO hostDAO;

    @Resource
    private UploadTaskFileDAO uploadTaskFileDAO;

    @Resource
    private UploadTaskFileService uploadTaskFileService;

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Resource
    private FileUploadTaskManager fileUploadTaskManager;

    @Keep
    @Resource
    private FileClient localFileClient;

    @Resource
    private FileUploadApi fileUploadApi;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UploadTaskCreateVO createUploadTask(UploadTaskCreateRequest request) {
        LoginUser user = Valid.notNull(SecurityUtils.getLoginUser());
        List<Long> hostIdList = request.getHostIdList();
        List<UploadTaskFileRequest> files = request.getFiles();
        log.info("UploadTaskService-createUploadTask request: {}", JSON.toJSONString(request));
        // 检查主机是否有权限
        this.checkHostPermission(hostIdList);
        // 查询主机信息
        List<HostBaseVO> hosts = hostDAO.selectBatchIds(hostIdList)
                .stream()
                .map(HostConvert.MAPPER::toBase)
                .collect(Collectors.toList());
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
                .hosts(hosts)
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
        String token = fileUploadApi.createUploadToken(user.getId(), Strings.format(SWAP_ENDPOINT, id));
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
        return uploadTaskDAO.of(wrapper)
                .page(request)
                .dataGrid(UploadTaskConvert.MAPPER::to);
    }

    @Override
    public List<UploadTaskStatusVO> getUploadTaskStatus(List<Long> idList, Boolean queryFiles) {
        // 查询任务
        List<UploadTaskStatusVO> tasks = uploadTaskDAO.of()
                .createWrapper()
                .select(UploadTaskDO::getId, UploadTaskDO::getStatus,
                        UploadTaskDO::getStartTime, UploadTaskDO::getEndTime)
                .in(UploadTaskDO::getId, idList)
                .then()
                .list(UploadTaskConvert.MAPPER::toStatus);
        if (!Booleans.isTrue(queryFiles)) {
            return tasks;
        }
        // 查询任务文件
        Map<Long, List<UploadTaskFileVO>> taskFilesMap = uploadTaskFileDAO.of()
                .createWrapper()
                .select(UploadTaskFileDO::getId, UploadTaskFileDO::getTaskId, UploadTaskFileDO::getHostId,
                        UploadTaskFileDO::getStatus, UploadTaskFileDO::getFileSize,
                        UploadTaskFileDO::getStartTime, UploadTaskFileDO::getEndTime)
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
        return uploadTaskDAO.selectCount(this.buildQueryWrapper(request));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer clearUploadTask(UploadTaskQueryRequest request) {
        // 查询id
        LambdaQueryWrapper<UploadTaskDO> wrapper = this.buildQueryWrapper(request)
                .select(UploadTaskDO::getId);
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
                .map(s -> Strings.format(SWAP_ENDPOINT, s))
                .map(localFileClient::getReturnPath)
                .map(localFileClient::getAbsolutePath)
                .collect(Collectors.toList());
        // 删除文件
        paths.forEach(Files1::delete);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<UploadTaskDO> buildQueryWrapper(UploadTaskQueryRequest request) {
        return uploadTaskDAO.wrapper()
                .eq(UploadTaskDO::getId, request.getId())
                .eq(UploadTaskDO::getUserId, request.getUserId())
                .like(UploadTaskDO::getDescription, request.getDescription())
                .like(UploadTaskDO::getRemotePath, request.getRemotePath())
                .eq(UploadTaskDO::getStatus, request.getStatus())
                .ge(UploadTaskDO::getCreateTime, Arrays1.getIfPresent(request.getCreateTimeRange(), 0))
                .le(UploadTaskDO::getCreateTime, Arrays1.getIfPresent(request.getCreateTimeRange(), 1))
                .orderByDesc(UploadTaskDO::getId);
    }

    /**
     * 检查主机权限
     *
     * @param hostIdList hostIdList
     */
    private void checkHostPermission(List<Long> hostIdList) {
        // 查询有权限的主机
        List<Long> authorizedHostIdList = assetAuthorizedDataService.getUserAuthorizedHostIdWithEnabledConfig(SecurityUtils.getLoginUserId(), HostConfigTypeEnum.SSH);
        for (Long hostId : hostIdList) {
            Valid.isTrue(authorizedHostIdList.contains(hostId), Strings.format(ErrorMessage.PLEASE_CHECK_HOST_SSH, hostId));
        }
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
            String path = localFileClient.getReturnPath(Strings.format(SWAP_ENDPOINT, id) + Const.SLASH + k);
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
        uploadTaskFileDAO.updateStatusByIdList(cancelIdList, UploadTaskFileStatusEnum.CANCELED.name());
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
