package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.time.Dates;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.file.FileClient;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.HostConvert;
import com.orion.ops.module.asset.convert.UploadTaskConvert;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.dao.UploadTaskDAO;
import com.orion.ops.module.asset.entity.domain.UploadTaskDO;
import com.orion.ops.module.asset.entity.dto.UploadTaskExtraDTO;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskCreateRequest;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostBaseVO;
import com.orion.ops.module.asset.entity.vo.UploadTaskCreateVO;
import com.orion.ops.module.asset.entity.vo.UploadTaskVO;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.enums.UploadTaskStatusEnum;
import com.orion.ops.module.asset.service.AssetAuthorizedDataService;
import com.orion.ops.module.asset.service.UploadTaskService;
import com.orion.ops.module.infra.api.FileUploadApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Resource
    private FileClient localFileClient;

    @Resource
    private FileUploadApi fileUploadApi;

    @Override
    public UploadTaskCreateVO createUploadTask(UploadTaskCreateRequest request) {
        LoginUser user = Valid.notNull(SecurityUtils.getLoginUser());
        List<Long> hostIdList = request.getHostIdList();
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
        // 插入
        int effect = uploadTaskDAO.insert(record);
        Long id = record.getId();
        // 设置 uploadToken
        String token = fileUploadApi.createUploadToken(user.getId(), Strings.format(SWAP_ENDPOINT, id));
        log.info("UploadTaskService-createUploadTask id: {}, effect: {}", id, effect);
        return UploadTaskCreateVO.builder()
                .id(id)
                .token(token)
                .build();
    }

    @Override
    public UploadTaskVO getUploadTaskById(Long id) {
        // 查询
        UploadTaskDO record = uploadTaskDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return UploadTaskConvert.MAPPER.to(record);
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
    public Integer deleteUploadTaskById(Long id) {
        log.info("UploadTaskService-deleteUploadTaskById id: {}", id);
        // 检查数据是否存在
        UploadTaskDO record = uploadTaskDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = uploadTaskDAO.deleteById(id);
        log.info("UploadTaskService-deleteUploadTaskById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer deleteUploadTaskByIdList(List<Long> idList) {
        log.info("UploadTaskService-deleteUploadTaskByIdList idList: {}", idList);
        int effect = uploadTaskDAO.deleteBatchIds(idList);
        log.info("UploadTaskService-deleteUploadTaskByIdList effect: {}", effect);
        return effect;
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
                .eq(UploadTaskDO::getDescription, request.getDescription())
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
