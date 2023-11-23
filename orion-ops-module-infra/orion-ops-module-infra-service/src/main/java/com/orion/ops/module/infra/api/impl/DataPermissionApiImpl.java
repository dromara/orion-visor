package com.orion.ops.module.infra.api.impl;

import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.DataPermissionApi;
import com.orion.ops.module.infra.convert.DataPermissionProviderConvert;
import com.orion.ops.module.infra.entity.dto.data.DataPermissionUpdateDTO;
import com.orion.ops.module.infra.entity.request.data.DataPermissionUpdateRequest;
import com.orion.ops.module.infra.enums.DataPermissionTypeEnum;
import com.orion.ops.module.infra.service.DataPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据权限 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Slf4j
@Service
public class DataPermissionApiImpl implements DataPermissionApi {

    @Resource
    private DataPermissionService dataPermissionService;

    @Override
    public void addDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto) {
        // 校验参数
        List<Long> relIdList = dto.getRelIdList();
        Valid.isTrue(dto.getUserId() != null || dto.getRoleId() != null);
        Valid.notEmpty(relIdList);
        // 添加权限
        DataPermissionUpdateRequest request = DataPermissionProviderConvert.MAPPER.toRequest(dto);
        request.setType(type.name());
        dataPermissionService.addDataPermission(request);
    }

    @Override
    public void updateDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto) {
        // 校验参数
        Valid.isTrue(dto.getUserId() != null || dto.getRoleId() != null);
        // 修改权限
        DataPermissionUpdateRequest request = DataPermissionProviderConvert.MAPPER.toRequest(dto);
        request.setType(type.name());
        dataPermissionService.updateDataPermission(request);
    }

    @Override
    public List<Long> getRelIdListByUserId(DataPermissionTypeEnum type, Long userId) {
        return dataPermissionService.getRelIdListByUserId(type.name(), userId);
    }

    @Override
    public List<Long> getRelIdListByRoleId(DataPermissionTypeEnum type, Long roleId) {
        return dataPermissionService.getRelIdListByRoleId(type.name(), roleId);
    }

    @Override
    public List<Long> getAllowRelIdList(DataPermissionTypeEnum type, Long userId) {
        return dataPermissionService.getAllowRelIdList(type.name(), userId);
    }

    @Override
    public int deleteByRelId(DataPermissionTypeEnum type, Long relId) {
        return dataPermissionService.deleteByRelId(type.name(), relId);
    }

}
