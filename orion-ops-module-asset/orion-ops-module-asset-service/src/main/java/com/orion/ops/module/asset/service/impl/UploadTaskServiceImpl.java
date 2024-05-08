package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.time.Dates;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.file.FileClient;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.HostConvert;
import com.orion.ops.module.asset.convert.UploadTaskConvert;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.dao.UploadTaskDAO;
import com.orion.ops.module.asset.dao.UploadTaskFileDAO;
import com.orion.ops.module.asset.entity.domain.UploadTaskDO;
import com.orion.ops.module.asset.entity.domain.UploadTaskFileDO;
import com.orion.ops.module.asset.entity.dto.UploadTaskExtraDTO;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskCreateRequest;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskFileRequest;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostBaseVO;
import com.orion.ops.module.asset.entity.vo.UploadTaskCreateVO;
import com.orion.ops.module.asset.entity.vo.UploadTaskFileVO;
import com.orion.ops.module.asset.entity.vo.UploadTaskVO;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.enums.UploadTaskFileStatusEnum;
import com.orion.ops.module.asset.enums.UploadTaskStatusEnum;
import com.orion.ops.module.asset.service.AssetAuthorizedDataService;
import com.orion.ops.module.asset.service.UploadTaskFileService;
import com.orion.ops.module.asset.service.UploadTaskService;
import com.orion.ops.module.infra.api.FileUploadApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

    private static final String SWAP_ENDPOINT = "/upload/swap/{}";

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
        record.setStatus(UploadTaskStatusEnum.PREPARATION.name());
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
    public UploadTaskVO getUploadTaskById(Long id) {
        // 查询任务
        UploadTaskDO record = uploadTaskDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 查询任务文件 TODO PROGRESS
        List<UploadTaskFileVO> files = uploadTaskFileService.getFileByTaskId(id);
        // 返回
        UploadTaskVO task = UploadTaskConvert.MAPPER.to(record);
        task.setFiles(files);
        return task;
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
        // TODO 暂停 / 删除交换区文件
        log.info("UploadTaskService-deleteUploadTaskByIdList idList: {}", idList);
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

    }

    @Override
    public void cancelUploadTask(Long id) {

    }

    @Override
    public void clearUploadSwapFiles(Long id) {
        this.clearUploadSwapFiles(Lists.singleton(id));
    }

    @Override
    public void clearUploadSwapFiles(List<Long> idList) {
        // 查询记录
        List<String> paths = idList.stream()
                .map(s -> Strings.format(SWAP_ENDPOINT, s))
                .map(localFileClient::getReturnPath)
                .map(localFileClient::getAbsolutePath)
                .collect(Collectors.toList());
        // TODO test
        paths.forEach(System.out::println);
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
                .in(UploadTaskDO::getDescription, request.getDescription())
                .eq(UploadTaskDO::getRemotePath, request.getRemotePath())
                .eq(UploadTaskDO::getStatus, request.getStatus())
                .ge(UploadTaskDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(UploadTaskDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1))
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

}
