package com.orion.ops.module.asset.service.impl;

import com.orion.lang.function.Functions;
import com.orion.lang.utils.Exceptions;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.framework.common.enums.EnableStatus;
import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;
import com.orion.ops.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.HostConfigConvert;
import com.orion.ops.module.asset.dao.HostConfigDAO;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.entity.domain.HostConfigDO;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.request.host.HostConfigUpdateRequest;
import com.orion.ops.module.asset.entity.request.host.HostConfigUpdateStatusRequest;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.service.HostConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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
        Map<String, Object> configMap = configType.getStrategyBean().toView(config.getConfig());
        vo.setConfig(configMap);
        return vo;
    }

    @Override
    public <T extends GenericsDataModel> T getHostConfig(Long hostId, HostConfigTypeEnum type) {
        // 查询配置
        HostConfigDO config = hostConfigDAO.getHostConfigByHostId(hostId, type.getType());
        if (config == null) {
            return null;
        }
        // 检查配置状态
        if (!BooleanBit.toBoolean(config.getStatus())) {
            throw Exceptions.disabled();
        }
        return type.parse(config.getConfig());
    }

    @Override
    public List<HostConfigVO> getHostConfigList(Long hostId) {
        // 查询
        List<HostConfigDO> configs = hostConfigDAO.getHostConfigByHostId(hostId);
        return configs.stream()
                .map(this::convertHostConfig)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<HostConfigVO> getHostConfigList(List<Long> hostIdList, String type) {
        // 查询
        List<HostConfigDO> configs = hostConfigDAO.getHostConfigByHostIdList(hostIdList, type);
        // 返回
        return configs.stream()
                .map(this::convertHostConfig)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public <T extends GenericsDataModel> Map<Long, T> getHostConfigMap(List<Long> hostIdList, HostConfigTypeEnum type) {
        // 查询
        List<HostConfigDO> configs = hostConfigDAO.getHostConfigByHostIdList(hostIdList, type.getType());
        // 返回
        return configs.stream()
                .collect(Collectors.toMap(
                        HostConfigDO::getHostId,
                        s -> type.parse(s.getConfig()),
                        Functions.right()));
    }

    @Override
    public Integer updateHostConfig(HostConfigUpdateRequest request) {
        // 查询原配置
        HostConfigDO record = this.getHostConfigByType(request.getHostId(), request.getType());
        Valid.notNull(record, ErrorMessage.CONFIG_ABSENT);
        HostConfigTypeEnum type = Valid.valid(HostConfigTypeEnum::of, record.getType());
        GenericsDataModel newConfig = type.parse(request.getConfig());
        // 查询主机
        HostDO host = hostDAO.selectById(record.getHostId());
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.REL_ID, host.getId());
        OperatorLogs.add(OperatorLogs.NAME, host.getName());
        OperatorLogs.add(OperatorLogs.TYPE, type.getType());
        // 检查版本
        Valid.eq(record.getVersion(), request.getVersion(), ErrorMessage.DATA_MODIFIED);
        MapDataStrategy<GenericsDataModel> strategy = type.getStrategyBean();
        GenericsDataModel beforeConfig = type.parse(record.getConfig());
        // 更新前校验
        strategy.doValidChain(beforeConfig, newConfig);
        // 修改配置
        HostConfigDO update = new HostConfigDO();
        update.setId(record.getId());
        update.setVersion(request.getVersion());
        update.setConfig(newConfig.serial());
        int effect = hostConfigDAO.updateById(update);
        Valid.version(effect);
        return update.getVersion();
    }

    @Override
    public Integer updateHostConfigStatus(HostConfigUpdateStatusRequest request) {
        Long hostId = request.getHostId();
        String type = request.getType();
        Integer status = request.getStatus();
        EnableStatus statusEnum = Valid.valid(EnableStatus::of, status);
        HostConfigTypeEnum configType = Valid.valid(HostConfigTypeEnum::of, type);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        HostConfigDO config = this.getHostConfigByType(hostId, type);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.REL_ID, host.getId());
        OperatorLogs.add(OperatorLogs.NAME, host.getName());
        OperatorLogs.add(OperatorLogs.STATUS_NAME, statusEnum.name());
        if (config != null) {
            // 修改 查询配置
            Integer version = request.getVersion();
            Valid.notNull(version);
            // 修改状态
            HostConfigDO update = new HostConfigDO();
            update.setId(config.getId());
            update.setStatus(status);
            update.setVersion(version);
            update.setUpdateTime(new Date());
            int effect = hostConfigDAO.updateById(update);
            Valid.version(effect);
            return update.getVersion();
        } else {
            // 新增 初始化
            HostConfigDO defaultConfig = this.getDefaultConfig(hostId, configType);
            defaultConfig.setStatus(status);
            // 插入数据
            hostConfigDAO.insert(defaultConfig);
            return defaultConfig.getVersion();
        }
    }


    @Override
    public void initHostConfig(Long hostId) {
        List<HostConfigDO> configs = Arrays.stream(HostConfigTypeEnum.values())
                .map(s -> this.getDefaultConfig(hostId, s))
                .collect(Collectors.toList());
        hostConfigDAO.insertBatch(configs);
    }

    @Override
    public List<Long> getEnabledConfigHostId(String type, List<Long> hostIdList) {
        return hostConfigDAO.of()
                .createValidateWrapper()
                .select(HostConfigDO::getHostId)
                .eq(HostConfigDO::getType, type)
                .eq(HostConfigDO::getStatus, BooleanBit.TRUE.getValue())
                .in(HostConfigDO::getHostId, hostIdList)
                .then()
                .stream()
                .map(HostConfigDO::getHostId)
                .collect(Collectors.toList());
    }

    /**
     * 通过类型获取配置
     *
     * @param hostId hostId
     * @param type   type
     * @return config
     */
    private HostConfigDO getHostConfigByType(Long hostId, String type) {
        // 查询配置
        return hostConfigDAO.of()
                .createWrapper()
                .eq(HostConfigDO::getHostId, hostId)
                .eq(HostConfigDO::getType, type)
                .then()
                .getOne();
    }

    /**
     * 获取默认配置
     *
     * @param hostId hostId
     * @param type   type
     * @return config
     */
    private HostConfigDO getDefaultConfig(Long hostId, HostConfigTypeEnum type) {
        HostConfigDO insert = new HostConfigDO();
        insert.setHostId(hostId);
        insert.setType(type.getType());
        insert.setStatus(type.getDefaultStatus());
        insert.setConfig(type.getStrategyBean().getDefault().serial());
        insert.setVersion(Const.DEFAULT_VERSION);
        return insert;
    }

    /**
     * 转化配置
     *
     * @param row row
     * @return config
     */
    private HostConfigVO convertHostConfig(HostConfigDO row) {
        // 获取配置
        HostConfigTypeEnum type = HostConfigTypeEnum.of(row.getType());
        if (type == null) {
            return null;
        }
        // 转为视图
        HostConfigVO vo = HostConfigConvert.MAPPER.to(row);
        Map<String, Object> config = type.getStrategyBean().toView(row.getConfig());
        vo.setConfig(config);
        return vo;
    }

}
