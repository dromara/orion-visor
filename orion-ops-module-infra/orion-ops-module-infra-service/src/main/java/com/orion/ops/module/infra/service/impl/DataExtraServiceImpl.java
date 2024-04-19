package com.orion.ops.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.function.Functions;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.mybatis.core.query.ThenLambdaWrapper;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.module.infra.dao.DataExtraDAO;
import com.orion.ops.module.infra.define.cache.DataExtraCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.DataExtraDO;
import com.orion.ops.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.ops.module.infra.entity.request.data.DataExtraSetRequest;
import com.orion.ops.module.infra.service.DataExtraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据拓展信息 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Slf4j
@Service
public class DataExtraServiceImpl implements DataExtraService {

    @Resource
    private DataExtraDAO dataExtraDAO;

    @Override
    public Integer setExtraItem(DataExtraSetRequest request) {
        // 查询配置是否存在
        DataExtraDO extraItem = dataExtraDAO.of()
                .createWrapper()
                .eq(DataExtraDO::getUserId, request.getUserId())
                .eq(DataExtraDO::getRelId, request.getRelId())
                .eq(DataExtraDO::getType, request.getType())
                .eq(DataExtraDO::getItem, request.getItem())
                .then()
                .getOne();
        if (extraItem == null) {
            // 插入
            this.addExtraItem(request);
            return Const.N_1;
        } else {
            // 修改
            return this.updateExtraValue(extraItem.getId(), request.getValue());
        }
    }

    @Override
    public Long addExtraItem(DataExtraSetRequest request) {
        // 插入
        DataExtraDO insert = new DataExtraDO();
        insert.setUserId(request.getUserId());
        insert.setRelId(request.getRelId());
        insert.setType(request.getType());
        insert.setItem(request.getItem());
        insert.setValue(request.getValue());
        dataExtraDAO.insert(insert);
        // 删除缓存
        RedisMaps.delete(DataExtraCacheKeyDefine.DATA_EXTRA.format(request.getUserId(), request.getType(), request.getItem()));
        return insert.getId();
    }

    @Override
    public Integer updateExtraValue(Long id, String value) {
        // 查询数据
        DataExtraDO data = this.getCacheSelectWrapper()
                .eq(DataExtraDO::getId, id)
                .then()
                .get();
        if (data == null) {
            return Const.N_0;
        }
        DataExtraDO update = new DataExtraDO();
        update.setId(id);
        update.setValue(value);
        // 更新
        int effect = dataExtraDAO.updateById(update);
        // 删除缓存
        RedisMaps.delete(DataExtraCacheKeyDefine.DATA_EXTRA.format(data.getUserId(), data.getType(), data.getItem()));
        return effect;
    }

    @Override
    public void batchUpdateExtraValue(Map<Long, String> map) {
        if (Maps.isEmpty(map)) {
            return;
        }
        // 查询数据
        List<DataExtraDO> list = this.getCacheSelectWrapper()
                .in(DataExtraDO::getId, map.keySet())
                .then()
                .list();
        if (list.isEmpty()) {
            return;
        }
        // 批量更新
        List<DataExtraDO> update = map.entrySet()
                .stream()
                .map(s -> {
                    DataExtraDO extra = new DataExtraDO();
                    extra.setId(s.getKey());
                    extra.setValue(s.getValue());
                    return extra;
                }).collect(Collectors.toList());
        dataExtraDAO.updateBatch(update);
        // 删除缓存
        this.deleteCache(list);
    }

    @Override
    public String getExtraItemValue(DataExtraQueryRequest request) {
        return dataExtraDAO.of()
                .wrapper(this.buildWrapper(request))
                .optionalOne()
                .map(DataExtraDO::getValue)
                .orElse(null);
    }

    @Override
    public Map<Long, String> getExtraItemValues(DataExtraQueryRequest request) {
        return dataExtraDAO.of()
                .wrapper(this.buildWrapper(request))
                .stream()
                .collect(Collectors.toMap(DataExtraDO::getRelId,
                        DataExtraDO::getValue,
                        Functions.right()));
    }

    @Override
    public String getExtraItemValueByCache(Long userId, String type, String item, Long relId) {
        return this.getExtraItemValuesByCache(userId, type, item).get(relId);
    }

    @Override
    public Map<Long, String> getExtraItemValuesByCache(Long userId, String type, String item) {
        // 查询缓存
        String key = DataExtraCacheKeyDefine.DATA_EXTRA.format(userId, type, item);
        Map<String, String> entities = RedisMaps.entities(key);
        if (Maps.isEmpty(entities)) {
            // 查询数据库
            DataExtraQueryRequest request = DataExtraQueryRequest.builder()
                    .userId(userId)
                    .type(type)
                    .item(item)
                    .build();
            Map<Long, String> extras = this.getExtraItemValues(request);
            entities = Maps.map(extras, String::valueOf, String::valueOf);
            // 设置屏障 防止穿透
            CacheBarriers.MAP.check(entities);
            // 设置缓存
            RedisMaps.putAll(key, DataExtraCacheKeyDefine.DATA_EXTRA, entities);
        }
        // 删除屏障
        CacheBarriers.MAP.remove(entities);
        // 转换
        return Maps.map(entities, Long::valueOf, Function.identity());
    }

    @Override
    public List<Map<Long, String>> getExtraItemsValuesByCache(Long userId, String type, List<String> items) {
        return items.stream()
                .map(s -> this.getExtraItemValuesByCache(userId, type, s))
                .collect(Collectors.toList());
    }

    @Override
    public DataExtraDO getExtraItem(DataExtraQueryRequest request) {
        return dataExtraDAO.of()
                .wrapper(this.buildWrapper(request))
                .getOne();
    }

    @Override
    public List<DataExtraDO> getExtraItems(DataExtraQueryRequest request) {
        return dataExtraDAO.of()
                .wrapper(this.buildWrapper(request))
                .list();
    }

    @Override
    public Integer deleteByUserId(Long userId) {
        List<DataExtraDO> list = this.getCacheSelectWrapper()
                .eq(DataExtraDO::getUserId, userId)
                .then()
                .list();
        if (list.isEmpty()) {
            return Const.N_0;
        }
        // 删除数据
        int effect = dataExtraDAO.deleteByUserId(userId);
        // 删除缓存
        this.deleteCache(list);
        return effect;
    }

    @Override
    public Integer deleteByRelId(String type, Long relId) {
        List<DataExtraDO> list = this.getCacheSelectWrapper()
                .eq(DataExtraDO::getType, type)
                .eq(DataExtraDO::getRelId, relId)
                .then()
                .list();
        if (list.isEmpty()) {
            return Const.N_0;
        }
        // 删除数据
        int effect = dataExtraDAO.deleteByRelId(type, relId);
        // 删除缓存
        this.deleteCache(list);
        return effect;
    }

    /**
     * 获取查询缓存参数 wrapper 不查询 longtext 加速查询
     *
     * @return wrapper
     */
    private ThenLambdaWrapper<DataExtraDO> getCacheSelectWrapper() {
        return dataExtraDAO.of()
                .createWrapper()
                .select(DataExtraDO::getId, DataExtraDO::getUserId, DataExtraDO::getType, DataExtraDO::getItem);
    }

    /**
     * 删除缓存
     *
     * @param list list
     */
    private void deleteCache(List<DataExtraDO> list) {
        if (Lists.isEmpty(list)) {
            return;
        }
        List<String> keys = list.stream()
                .map(s -> DataExtraCacheKeyDefine.DATA_EXTRA.format(s.getUserId(), s.getType(), s.getItem()))
                .distinct()
                .collect(Collectors.toList());
        RedisMaps.delete(keys);
    }

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    private LambdaQueryWrapper<DataExtraDO> buildWrapper(DataExtraQueryRequest entity) {
        return dataExtraDAO.wrapper()
                .eq(DataExtraDO::getUserId, entity.getUserId())
                .eq(DataExtraDO::getRelId, entity.getRelId())
                .in(DataExtraDO::getRelId, entity.getRelIdList())
                .eq(DataExtraDO::getType, entity.getType())
                .eq(DataExtraDO::getItem, entity.getItem())
                .in(DataExtraDO::getItem, entity.getItems());
    }

}
