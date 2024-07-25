package com.orion.visor.module.asset.service.impl;

import com.orion.lang.function.Functions;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.asset.dao.HostDAO;
import com.orion.visor.module.asset.entity.domain.HostDO;
import com.orion.visor.module.asset.enums.HostStatusEnum;
import com.orion.visor.module.asset.enums.HostTypeEnum;
import com.orion.visor.module.asset.handler.host.config.model.HostSshConfigModel;
import com.orion.visor.module.asset.service.HostConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 主机配置 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Slf4j
@Service
public class HostConfigServiceImpl implements HostConfigService {

    @Resource
    private HostDAO hostDAO;

    @Override
    public <T extends GenericsDataModel> T getHostConfig(Long id, HostTypeEnum type) {
        // 查询主机
        HostDO host = hostDAO.selectById(id);
        // 转换为配置
        return this.buildHostConfig(host, type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GenericsDataModel> T buildHostConfig(HostDO host, HostTypeEnum type) {
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 检查主机类型
        Valid.isTrue(type.name().equals(host.getType()), ErrorMessage.HOST_TYPE_ERROR);
        // 检查主机状态
        Valid.isTrue(HostStatusEnum.ENABLED.name().equals(host.getStatus()), ErrorMessage.HOST_NOT_ENABLED);
        // 查询主机配置
        HostSshConfigModel model = type.parse(host.getConfig());
        Valid.notNull(model, ErrorMessage.CONFIG_ABSENT);
        return (T) model;
    }

    @Override
    public <T extends GenericsDataModel> Map<Long, T> getHostConfigMap(List<Long> idList, HostTypeEnum type) {
        // 查询主机
        Map<Long, HostDO> hostMap = hostDAO.selectBatchIds(idList)
                .stream()
                .collect(Collectors.toMap(HostDO::getId, Function.identity(), Functions.right()));
        // 转换为配置
        Map<Long, T> result = new HashMap<>();
        for (Long id : idList) {
            result.put(id, this.buildHostConfig(hostMap.get(id), type));
        }
        return result;
    }

    @Override
    public <T extends GenericsDataModel> Map<Long, T> buildHostConfigMap(List<HostDO> hostList, HostTypeEnum type) {
        Map<Long, T> result = new HashMap<>();
        for (HostDO host : hostList) {
            result.put(host.getId(), this.buildHostConfig(host, type));
        }
        return result;
    }

}
