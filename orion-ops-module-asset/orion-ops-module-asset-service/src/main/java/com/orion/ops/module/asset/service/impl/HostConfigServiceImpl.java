package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.enums.EnableStatus;
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
import com.orion.ops.module.asset.handler.host.config.model.HostConfigModel;
import com.orion.ops.module.asset.handler.host.config.strategy.HostConfigStrategy;
import com.orion.ops.module.asset.service.HostConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
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
    public List<HostConfigVO> getHostConfigList(Long hostId) {
        List<HostConfigDO> configs = hostConfigDAO.getHostConfigByHostId(hostId);
        if (configs.isEmpty()) {
            // 初始化 兜底
            this.initHostConfig(hostId);
            configs = hostConfigDAO.getHostConfigByHostId(hostId);
        }
        // 返回
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
        Long id = request.getId();
        // 查询原配置
        HostConfigDO record = hostConfigDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.CONFIG_ABSENT);
        HostConfigTypeEnum type = Valid.valid(HostConfigTypeEnum::of, record.getType());
        HostConfigModel config = JSON.parseObject(request.getConfig(), type.getType());
        // 查询主机
        HostDO host = hostDAO.selectById(record.getHostId());
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.REL_ID, host.getId());
        OperatorLogs.add(OperatorLogs.NAME, host.getName());
        OperatorLogs.add(OperatorLogs.TYPE, type.name());
        // 检查版本
        Valid.eq(record.getVersion(), request.getVersion(), ErrorMessage.DATA_MODIFIED);
        HostConfigStrategy<HostConfigModel> strategy = type.getStrategy();
        // 预校验参数
        strategy.preValidConfig(config);
        // 更新填充
        HostConfigModel beforeConfig = JSON.parseObject(record.getConfig(), type.getType());
        strategy.updateFill(beforeConfig, config);
        // 检查参数
        strategy.validConfig(config);
        // 修改配置
        HostConfigDO update = new HostConfigDO();
        update.setId(id);
        update.setVersion(request.getVersion());
        update.setConfig(config.serial());
        int effect = hostConfigDAO.updateById(update);
        Valid.version(effect);
        return update.getVersion();
    }

    @Override
    public Integer updateHostConfigStatus(HostConfigUpdateStatusRequest request) {
        Long id = request.getId();
        Integer status = request.getStatus();
        EnableStatus statusEnum = Valid.valid(EnableStatus::of, status);
        // 查询配置
        HostConfigDO record = hostConfigDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.CONFIG_ABSENT);
        // 查询主机
        HostDO host = hostDAO.selectById(record.getHostId());
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.REL_ID, host.getId());
        OperatorLogs.add(OperatorLogs.NAME, host.getName());
        OperatorLogs.add(OperatorLogs.TYPE, HostConfigTypeEnum.of(record.getType()).name());
        OperatorLogs.add(OperatorLogs.STATUS_NAME, statusEnum.name());
        // 修改状态
        HostConfigDO update = new HostConfigDO();
        update.setId(id);
        update.setStatus(status);
        update.setVersion(request.getVersion());
        int effect = hostConfigDAO.updateById(update);
        Valid.version(effect);
        return update.getVersion();
    }

    @Override
    public void initHostConfig(Long hostId) {
        List<HostConfigDO> configs = Arrays.stream(HostConfigTypeEnum.values())
                .map(s -> {
                    HostConfigDO insert = new HostConfigDO();
                    insert.setHostId(hostId);
                    insert.setType(s.name());
                    insert.setStatus(s.getDefaultStatus());
                    insert.setConfig(s.getStrategy().getDefault().serial());
                    insert.setVersion(Const.DEFAULT_VERSION);
                    return insert;
                }).collect(Collectors.toList());
        hostConfigDAO.insertBatch(configs);
    }

}
