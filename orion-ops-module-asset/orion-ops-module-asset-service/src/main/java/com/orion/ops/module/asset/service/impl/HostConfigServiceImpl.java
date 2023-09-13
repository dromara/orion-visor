package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.HostConfigConvert;
import com.orion.ops.module.asset.dao.HostConfigDAO;
import com.orion.ops.module.asset.entity.domain.HostConfigDO;
import com.orion.ops.module.asset.entity.dto.host.HostConfigContent;
import com.orion.ops.module.asset.entity.request.host.HostConfigUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.service.HostConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;
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
        Valid.valid(HostConfigTypeEnum::of, type);
        // 查询配置
        HostConfigDO config = hostConfigDAO.getHostConfigByHostId(hostId, type);
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        // 转换
        HostConfigVO vo = HostConfigConvert.MAPPER.to(config);
        vo.setConfig(JSON.parseObject(config.getConfig()));
        return vo;
    }

    @Override
    public <T extends HostConfigContent> T getHostConfig(Long hostId, HostConfigTypeEnum type) {
        // 查询配置
        HostConfigDO config = hostConfigDAO.getHostConfigByHostId(hostId, type.name());
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        return JSON.parseObject(config.getConfig(), (Type) type.getType());
    }

    @Override
    public List<HostConfigVO> getHostConfig(Long hostId) {
        List<HostConfigDO> configs = hostConfigDAO.getHostConfigByHostId(hostId);
        return configs.stream().map(config -> {
            HostConfigVO vo = HostConfigConvert.MAPPER.to(config);
            vo.setConfig(JSON.parseObject(config.getConfig()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateHostConfig(HostConfigUpdateRequest request) {
        String typeValue = request.getType();
        HostConfigTypeEnum type = Valid.valid(HostConfigTypeEnum::of, typeValue);
        HostConfigContent requestConfig = JSON.parseObject(request.getConfig(), type.getType());
        // 查询原配置
        HostConfigDO record = hostConfigDAO.getHostConfigByHostId(request.getHostId(), typeValue);
        if (record == null) {
            // 填充
            type.insertFill(requestConfig);
            // 检查参数
            Valid.valid(requestConfig);
            // 新增配置
            HostConfigDO insert = HostConfigConvert.MAPPER.to(request);
            insert.setConfig(JSON.toJSONString(requestConfig));
            hostConfigDAO.insert(insert);
        } else {
            // 检查版本
            Valid.eq(record.getVersion(), request.getVersion(), ErrorMessage.DATA_MODIFIED);
            // 填充
            HostConfigContent beforeConfig = JSON.parseObject(record.getConfig(), type.getType());
            type.updateFill(beforeConfig, requestConfig);
            // 检查参数
            Valid.valid(requestConfig);
            // 修改配置
            HostConfigDO update = new HostConfigDO();
            update.setId(record.getId());
            update.setVersion(request.getVersion());
            update.setConfig(JSON.toJSONString(requestConfig));
            hostConfigDAO.updateById(update);
        }
    }

}
