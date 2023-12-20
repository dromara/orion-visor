package com.orion.ops.module.asset.service.impl;

import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.entity.request.host.HostAliasUpdateRequest;
import com.orion.ops.module.asset.service.HostExtraService;
import com.orion.ops.module.infra.api.DataAliasApi;
import com.orion.ops.module.infra.entity.dto.data.DataAliasUpdateDTO;
import com.orion.ops.module.infra.enums.DataExtraTypeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 主机拓展信息 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 12:11
 */
@Service
public class HostExtraServiceImpl implements HostExtraService {

    @Resource
    private DataAliasApi dataAliasApi;

    @Override
    public Integer updateHostAlias(HostAliasUpdateRequest request) {
        DataAliasUpdateDTO update = DataAliasUpdateDTO.builder()
                .userId(SecurityUtils.getLoginUserId())
                .relId(request.getId())
                .alias(request.getName())
                .build();
        return dataAliasApi.updateDataAlias(update, DataExtraTypeEnum.HOST);
    }

}
