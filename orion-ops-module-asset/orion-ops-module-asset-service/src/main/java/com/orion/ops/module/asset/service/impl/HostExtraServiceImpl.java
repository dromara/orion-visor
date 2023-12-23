package com.orion.ops.module.asset.service.impl;

import com.orion.lang.function.Functions;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;
import com.orion.ops.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.entity.request.host.HostAliasUpdateRequest;
import com.orion.ops.module.asset.entity.request.host.HostExtraQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostExtraUpdateRequest;
import com.orion.ops.module.asset.enums.HostExtraItemEnum;
import com.orion.ops.module.asset.service.HostExtraService;
import com.orion.ops.module.infra.api.DataAliasApi;
import com.orion.ops.module.infra.api.DataExtraApi;
import com.orion.ops.module.infra.entity.dto.data.DataAliasUpdateDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraQueryDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraSetDTO;
import com.orion.ops.module.infra.enums.DataExtraTypeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private DataExtraApi dataExtraApi;

    @Override
    public Integer updateHostAlias(HostAliasUpdateRequest request) {
        DataAliasUpdateDTO update = DataAliasUpdateDTO.builder()
                .userId(SecurityUtils.getLoginUserId())
                .relId(request.getId())
                .alias(request.getName())
                .build();
        return dataAliasApi.updateDataAlias(update, DataExtraTypeEnum.HOST);
    }

    @Override
    public Map<String, Object> getHostExtra(Long hostId, String item) {
        HostExtraItemEnum extraItem = Valid.valid(HostExtraItemEnum::of, item);
        // 查询配置项
        Long userId = SecurityUtils.getLoginUserId();
        DataExtraQueryDTO query = DataExtraQueryDTO.builder()
                .userId(userId)
                .relId(hostId)
                .item(item)
                .build();
        String extraValue = dataExtraApi.getExtraValue(query, DataExtraTypeEnum.HOST);
        // 检查初始化并转为视图
        return this.checkItemAndToView(extraItem, extraValue, userId, hostId);
    }

    @Override
    public Map<String, Map<String, Object>> getHostExtraList(HostExtraQueryRequest request) {
        Long hostId = request.getHostId();
        List<String> items = request.getItems();
        List<HostExtraItemEnum> extraItems = items.stream()
                .map(s -> Valid.valid(HostExtraItemEnum::of, s))
                .collect(Collectors.toList());
        // 查询配置项
        Long userId = SecurityUtils.getLoginUserId();
        DataExtraQueryDTO query = DataExtraQueryDTO.builder()
                .userId(userId)
                .relId(hostId)
                .items(items)
                .build();
        Map<String, String> extraValues = dataExtraApi.getExtraItems(query, DataExtraTypeEnum.HOST)
                .stream()
                .collect(Collectors.toMap(
                        DataExtraDTO::getItem,
                        DataExtraDTO::getValue,
                        Functions.right())
                );
        // 检查初始化
        Map<String, Map<String, Object>> result = Maps.newMap();
        for (HostExtraItemEnum extraItem : extraItems) {
            String item = extraItem.getItem();
            // 检查初始化并转为视图
            Map<String, Object> extraValue = this.checkItemAndToView(extraItem, extraValues.get(item), userId, hostId);
            result.put(item, extraValue);
        }
        return result;
    }

    @Override
    public Integer updateHostExtra(HostExtraUpdateRequest request) {
        String item = request.getItem();
        Long hostId = request.getHostId();
        Long userId = SecurityUtils.getLoginUserId();
        HostExtraItemEnum extraItem = Valid.valid(HostExtraItemEnum::of, item);
        MapDataStrategy<GenericsDataModel> strategy = extraItem.getStrategyBean();
        // 查询原始配置
        DataExtraQueryDTO query = DataExtraQueryDTO.builder()
                .userId(userId)
                .relId(hostId)
                .item(item)
                .build();
        DataExtraDTO beforeExtraItem = dataExtraApi.getExtraItem(query, DataExtraTypeEnum.HOST);
        Valid.notNull(beforeExtraItem, ErrorMessage.CONFIG_ABSENT);
        GenericsDataModel newExtra = extraItem.parse(request.getExtra());
        GenericsDataModel beforeExtra = extraItem.parse(beforeExtraItem.getValue());
        // 更新验证
        strategy.doValidChain(beforeExtra, newExtra);
        // 更新配置
        return dataExtraApi.updateExtraValue(beforeExtraItem.getId(), newExtra.serial());
    }

    @Override
    public void deleteHostKeyCallback(Long id) {

    }

    @Override
    public void deleteHostIdentityCallback(Long id) {

    }

    /**
     * 检查配置项并且转为视图 (不存在则初始化默认值)
     *
     * @param extraItem  extraItem
     * @param extraValue extraValue
     * @param userId     userId
     * @param hostId     hostId
     * @return viewMap
     */
    private Map<String, Object> checkItemAndToView(HostExtraItemEnum extraItem, String extraValue, Long userId, Long hostId) {
        String item = extraItem.getItem();
        MapDataStrategy<GenericsDataModel> strategy = extraItem.getStrategyBean();
        if (extraValue == null) {
            // 初始化默认数据
            extraValue = strategy.getDefault().serial();
            // 插入默认值
            DataExtraSetDTO set = DataExtraSetDTO.builder()
                    .userId(userId)
                    .relId(hostId)
                    .item(item)
                    .value(extraValue)
                    .build();
            dataExtraApi.addExtraItem(set, DataExtraTypeEnum.HOST);
        }
        return strategy.toView(extraValue);
    }

}
