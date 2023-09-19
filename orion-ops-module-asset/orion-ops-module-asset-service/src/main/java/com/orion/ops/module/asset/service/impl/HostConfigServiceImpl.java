package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.HostConfigConvert;
import com.orion.ops.module.asset.dao.HostConfigDAO;
import com.orion.ops.module.asset.entity.domain.HostConfigDO;
import com.orion.ops.module.asset.entity.request.host.HostConfigUpdateRequest;
import com.orion.ops.module.asset.entity.request.host.HostConfigUpdateStatusRequest;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.handler.host.config.model.HostConfigModel;
import com.orion.ops.module.asset.handler.host.config.strategy.HostConfigStrategy;
import com.orion.ops.module.asset.service.HostConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
    private HostConfigDAO hostConfigDAO;

    @Override
    public HostConfigVO getHostConfig(Long hostId, String type) {
        HostConfigTypeEnum configType = Valid.valid(HostConfigTypeEnum::of, type);
        // 查询配置
        HostConfigDO config = hostConfigDAO.getHostConfigByHostId(hostId, type);
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        // 转换
        HostConfigVO vo = HostConfigConvert.MAPPER.to(config);
        // 获取配置
        Map<String, Object> configMap = configType.getStrategy().toView(config.getConfig());
        vo.setConfig(configMap);
        return vo;
    }

    @Override
    public <T extends HostConfigModel> T getHostConfig(Long hostId, HostConfigTypeEnum type) {
        // 查询配置
        HostConfigDO config = hostConfigDAO.getHostConfigByHostId(hostId, type.name());
        if (config == null) {
            return null;
        }
        return (T) JSON.parseObject(config.getConfig(), type.getType());
    }

    @Override
    public List<HostConfigVO> getHostConfig(Long hostId) {
        List<HostConfigDO> configs = hostConfigDAO.getHostConfigByHostId(hostId);
        return configs.stream().map(s -> {
            HostConfigVO vo = HostConfigConvert.MAPPER.to(s);
            // 获取配置
            Map<String, Object> config = HostConfigTypeEnum.of(s.getType())
                    .getStrategy()
                    .toView(s.getConfig());
            vo.setConfig(config);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Integer updateHostConfig(HostConfigUpdateRequest request) {
        String typeValue = request.getType();
        HostConfigTypeEnum type = Valid.valid(HostConfigTypeEnum::of, typeValue);
        HostConfigModel requestConfig = JSON.parseObject(request.getConfig(), type.getType());
        // 查询原配置
        HostConfigDO record = hostConfigDAO.getHostConfigByHostId(request.getHostId(), typeValue);
        HostConfigStrategy<HostConfigModel> strategy = type.getStrategy();
        if (record == null) {
            // 填充
            strategy.insertFill(requestConfig);
            // 检查参数
            Valid.valid(requestConfig);
            // 新增配置
            HostConfigDO insert = HostConfigConvert.MAPPER.to(request);
            insert.setVersion(Const.DEFAULT_VERSION);
            insert.setConfig(requestConfig.serial());
            hostConfigDAO.insert(insert);
            return Const.DEFAULT_VERSION;
        } else {
            // 检查版本
            Valid.eq(record.getVersion(), request.getVersion(), ErrorMessage.DATA_MODIFIED);
            // 填充
            HostConfigModel beforeConfig = JSON.parseObject(record.getConfig(), type.getType());
            strategy.updateFill(beforeConfig, requestConfig);
            // 检查参数
            Valid.valid(requestConfig);
            // 修改配置
            // TODO 检查version是否改变
            HostConfigDO update = new HostConfigDO();
            update.setId(record.getId());
            update.setVersion(request.getVersion());
            update.setConfig(requestConfig.serial());
            hostConfigDAO.updateById(update);
            return update.getVersion();
        }
    }

    @Override
    public Integer updateHostConfigStatus(HostConfigUpdateStatusRequest request) {
        Long hostId = request.getHostId();
        String typeValue = request.getType();
        Integer status = request.getStatus();
        Valid.valid(BooleanBit::of, status);
        HostConfigTypeEnum type = Valid.valid(HostConfigTypeEnum::of, typeValue);
        // 查询配置
        HostConfigDO record = hostConfigDAO.getHostConfigByHostId(hostId, typeValue);
        HostConfigStrategy<HostConfigModel> strategy = type.getStrategy();
        if (record == null) {
            // 插入默认值
            HostConfigDO insert = new HostConfigDO();
            insert.setHostId(hostId);
            insert.setType(typeValue);
            insert.setStatus(status);
            insert.setVersion(Const.DEFAULT_VERSION);
            insert.setConfig(strategy.getDefault().serial());
            hostConfigDAO.insert(insert);
            return Const.DEFAULT_VERSION;
        } else {
            // TODO 检查version是否改变
            // 修改状态
            HostConfigDO update = new HostConfigDO();
            update.setId(record.getId());
            update.setStatus(status);
            hostConfigDAO.updateById(update);
            return update.getVersion();
        }
    }

}
