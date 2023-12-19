package com.orion.ops.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.function.Functions;
import com.orion.lang.utils.Refs;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.dao.PreferenceDAO;
import com.orion.ops.module.infra.define.cache.PreferenceCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.PreferenceDO;
import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdatePartialRequest;
import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdateRequest;
import com.orion.ops.module.infra.enums.PreferenceTypeEnum;
import com.orion.ops.module.infra.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户偏好 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
@Slf4j
@Service
public class PreferenceServiceImpl implements PreferenceService {

    @Resource
    private PreferenceDAO preferenceDAO;

    @Override
    public Integer updatePreference(PreferenceUpdateRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        String type = request.getType();
        String item = request.getItem();
        Valid.valid(PreferenceTypeEnum::of, type);
        // 查询
        PreferenceDO preference = preferenceDAO.of()
                .createWrapper()
                .eq(PreferenceDO::getUserId, userId)
                .eq(PreferenceDO::getType, type)
                .eq(PreferenceDO::getItem, item)
                .then()
                .getOne();
        int effect;
        if (preference == null) {
            // 插入
            PreferenceDO insertRecord = new PreferenceDO();
            insertRecord.setUserId(userId);
            insertRecord.setType(type);
            insertRecord.setItem(item);
            insertRecord.setValue(Refs.json(request.getValue()));
            effect = preferenceDAO.insert(insertRecord);
        } else {
            // 更新
            PreferenceDO updateRecord = new PreferenceDO();
            updateRecord.setId(preference.getId());
            updateRecord.setValue(Refs.json(request.getValue()));
            effect = preferenceDAO.updateById(updateRecord);
        }
        // 删除缓存
        RedisMaps.delete(PreferenceCacheKeyDefine.PREFERENCE.format(userId, type));
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreferencePartial(PreferenceUpdatePartialRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        String type = request.getType();
        Map<String, Object> config = request.getConfig();
        Valid.valid(PreferenceTypeEnum::of, type);
        // 查询配置
        LambdaQueryWrapper<PreferenceDO> wrapper = preferenceDAO.lambda()
                .eq(PreferenceDO::getUserId, userId)
                .eq(PreferenceDO::getType, type)
                .in(PreferenceDO::getItem, config.keySet());
        Map<String, PreferenceDO> items = preferenceDAO.selectList(wrapper)
                .stream()
                .collect(Collectors.toMap(
                        PreferenceDO::getItem,
                        Function.identity(),
                        Functions.right())
                );
        // 修改配置
        List<PreferenceDO> updateRecords = config.keySet()
                .stream()
                .filter(items::containsKey)
                .map(s -> {
                    PreferenceDO update = new PreferenceDO();
                    update.setId(items.get(s).getId());
                    update.setValue(Refs.json(config.get(s)));
                    return update;
                }).collect(Collectors.toList());
        preferenceDAO.updateBatch(updateRecords);
        // 插入配置
        List<PreferenceDO> insertRecords = config.keySet()
                .stream()
                .filter(s -> !items.containsKey(s))
                .map(s -> {
                    PreferenceDO insert = new PreferenceDO();
                    insert.setUserId(userId);
                    insert.setType(type);
                    insert.setItem(s);
                    insert.setValue(Refs.json(config.get(s)));
                    return insert;
                }).collect(Collectors.toList());
        preferenceDAO.insertBatch(insertRecords);
        // 删除缓存
        RedisMaps.delete(PreferenceCacheKeyDefine.PREFERENCE.format(userId, type));
    }

    @Override
    public Map<String, Object> getPreferenceByType(String type) {
        Long userId = SecurityUtils.getLoginUserId();
        PreferenceTypeEnum typeEnum = Valid.valid(PreferenceTypeEnum::of, type);
        return this.getPreferenceByCache(userId, typeEnum);
    }

    @Override
    @Async("asyncExecutor")
    public Future<Map<String, Object>> getPreferenceAsync(Long userId, PreferenceTypeEnum type) {
        Map<String, Object> config = this.getPreferenceByCache(userId, type);
        return CompletableFuture.completedFuture(config);
    }

    @Override
    public void deletePreferenceByUserId(Long userId) {
        // 删除
        int effect = preferenceDAO.deleteByUserId(userId);
        log.info("PreferenceService-deletePreferenceById userId: {}, effect: {}", userId, effect);
        // 删除缓存
        List<String> deleteKeys = Arrays.stream(PreferenceTypeEnum.values())
                .map(s -> PreferenceCacheKeyDefine.PREFERENCE.format(userId, s))
                .collect(Collectors.toList());
        RedisMaps.delete(deleteKeys);
    }

    /**
     * 通过缓存获取偏好
     *
     * @param userId userId
     * @param type   type
     * @return config
     */
    private Map<String, Object> getPreferenceByCache(Long userId, PreferenceTypeEnum type) {
        String typeValue = type.getType();
        // 查询缓存 用 string 防止数据类型丢失
        String key = PreferenceCacheKeyDefine.PREFERENCE.format(userId, type);
        Map<String, String> config = RedisMaps.entities(key);
        boolean setCache = Maps.isEmpty(config);
        // 查询数据库
        if (Maps.isEmpty(config)) {
            config = preferenceDAO.of()
                    .createWrapper()
                    .eq(PreferenceDO::getUserId, userId)
                    .eq(PreferenceDO::getType, type)
                    .then()
                    .stream()
                    .collect(Collectors.toMap(
                            PreferenceDO::getItem,
                            PreferenceDO::getValue,
                            Functions.right())
                    );
        }
        // 初始化
        if (Maps.isEmpty(config)) {
            // 获取默认值
            config = type.getStrategy()
                    .getDefault()
                    .toMap();
            // 插入默认值
            List<PreferenceDO> entities = config
                    .entrySet()
                    .stream()
                    .map(s -> {
                        PreferenceDO entity = new PreferenceDO();
                        entity.setUserId(userId);
                        entity.setType(typeValue);
                        entity.setItem(s.getKey());
                        entity.setValue(s.getValue());
                        return entity;
                    }).collect(Collectors.toList());
            preferenceDAO.insertBatch(entities);
        }
        // 设置缓存
        if (setCache) {
            RedisMaps.putAll(key, PreferenceCacheKeyDefine.PREFERENCE, config);
        }
        // unref
        return Maps.map(config, Function.identity(), Refs::unref);
    }

}
