package com.orion.ops.module.infra.service.impl;

import com.orion.lang.function.Functions;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.module.infra.convert.DataAliasConvert;
import com.orion.ops.module.infra.dao.DataAliasDAO;
import com.orion.ops.module.infra.define.cache.DataAliasCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.DataAliasDO;
import com.orion.ops.module.infra.entity.request.data.DataAliasUpdateRequest;
import com.orion.ops.module.infra.service.DataAliasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据别名 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
@Slf4j
@Service
public class DataAliasServiceImpl implements DataAliasService {

    @Resource
    private DataAliasDAO dataAliasDAO;
    // @Override
    // public Integer deleteDataAlias(DataAliasQueryRequest request) {
    //     log.info("DataAliasService.deleteDataAlias request: {}", JSON.toJSONString(request));
    //     // 条件
    //     LambdaQueryWrapper<DataAliasDO> wrapper = this.buildQueryWrapper(request);
    //     // 删除
    //     int effect = dataAliasDAO.delete(wrapper);
    //     log.info("DataAliasService.deleteDataAlias effect: {}", effect);
    //     // 删除缓存
    //     RedisMaps.delete(DataAliasCacheKeyDefine.DATA_ALIAS);
    //     return effect;
    // }

    @Override
    public Integer updateDataAlias(DataAliasUpdateRequest request) {
        // 检查是否存在
        Long userId = request.getUserId();
        String type = request.getType();
        DataAliasDO alias = dataAliasDAO.of()
                .createWrapper()
                .eq(DataAliasDO::getUserId, userId)
                .eq(DataAliasDO::getType, type)
                .eq(DataAliasDO::getRelId, request.getRelId())
                .then()
                .only()
                .get();
        int effect;
        if (alias == null) {
            // 插入
            DataAliasDO record = DataAliasConvert.MAPPER.to(request);
            effect = dataAliasDAO.insert(record);
        } else {
            // 更新
            DataAliasDO update = DataAliasDO.builder()
                    .id(alias.getId())
                    .alias(request.getAlias())
                    .build();
            effect = dataAliasDAO.updateById(update);
        }
        // 删除缓存
        RedisMaps.delete(DataAliasCacheKeyDefine.DATA_ALIAS.format(userId, type));
        return effect;
    }

    @Override
    public String getDataAlias(Long userId, String type, Long relId) {
        return null;
    }

    @Override
    public Map<Long, String> getDataAlias(Long userId, String type) {
        // 查询缓存
        String key = DataAliasCacheKeyDefine.DATA_ALIAS.format(userId, type);
        Map<String, String> entities = RedisMaps.entities(key);
        if (Maps.isEmpty(entities)) {
            // 查询数据库
            entities = dataAliasDAO.of()
                    .createWrapper()
                    .eq(DataAliasDO::getUserId, userId)
                    .eq(DataAliasDO::getType, type)
                    .then()
                    .stream()
                    .collect(Collectors.toMap(
                            s -> String.valueOf(s.getRelId()),
                            DataAliasDO::getAlias,
                            Functions.right())
                    );

            // 设置屏障 防止穿透
            CacheBarriers.MAP.check(entities);
            // 设置缓存
            RedisMaps.putAll(key, DataAliasCacheKeyDefine.DATA_ALIAS, entities);
        }
        // 删除屏障
        CacheBarriers.MAP.remove(entities);
        // 转换
        return Maps.map(entities, Long::valueOf, Function.identity());
    }

    @Override
    public Integer deleteDataAliasByUserId(Long userId) {
        return null;
    }

    @Override
    public Integer deleteDataAliasByRelId(String type, Long relId) {
        return null;
    }

}
