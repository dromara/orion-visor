package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.dao.PreferenceDAO;
import com.orion.ops.module.infra.define.PreferenceCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.PreferenceDO;
import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdateRequest;
import com.orion.ops.module.infra.entity.vo.PreferenceVO;
import com.orion.ops.module.infra.enums.PreferenceTypeEnum;
import com.orion.ops.module.infra.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
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
    public Integer updatePreference(PreferenceUpdateRequest request, boolean partial) {
        Long userId = SecurityUtils.getLoginUserId();
        String type = request.getType();
        Valid.valid(PreferenceTypeEnum::of, type);
        // 查询
        PreferenceDO preference = preferenceDAO.of()
                .wrapper(this.buildQueryWrapper(userId, type))
                .getOne();
        int effect;
        if (preference == null) {
            // 直接插入
            PreferenceDO insertRecord = new PreferenceDO();
            insertRecord.setUserId(userId);
            insertRecord.setType(type);
            insertRecord.setConfig(JSON.toJSONString(request.getConfig()));
            effect = preferenceDAO.insert(insertRecord);
        } else {
            // 更新
            PreferenceDO updateRecord = new PreferenceDO();
            updateRecord.setId(preference.getId());
            if (partial) {
                // 部分更新
                JSONObject config = JSON.parseObject(preference.getConfig());
                config.putAll(request.getConfig());
                updateRecord.setConfig(JSON.toJSONString(config));
            } else {
                // 全部更新
                updateRecord.setConfig(JSON.toJSONString(request.getConfig()));
            }
            effect = preferenceDAO.updateById(updateRecord);
            // 删除缓存
            RedisStrings.delete(PreferenceCacheKeyDefine.PREFERENCE.format(userId, type));
        }
        return effect;
    }

    @Override
    public PreferenceVO getPreferenceByType(String type) {
        Long userId = SecurityUtils.getLoginUserId();
        PreferenceTypeEnum typeEnum = Valid.valid(PreferenceTypeEnum::of, type);
        Map<String, Object> config = this.getPreferenceByCache(userId, typeEnum);
        // 返回
        return PreferenceVO.builder()
                .config(config)
                .build();
    }

    @Override
    @Async("asyncExecutor")
    public Future<Map<String, Object>> getPreference(Long userId, PreferenceTypeEnum type) {
        Map<String, Object> config = this.getPreferenceByCache(userId, type);
        return CompletableFuture.completedFuture(config);
    }

    @Override
    @Async("asyncExecutor")
    public void deletePreferenceByUserId(Long userId) {
        // 删除
        int effect = preferenceDAO.deleteByUserId(userId);
        log.info("PreferenceService-deletePreferenceById userId: {}, effect: {}", userId, effect);
        // 删除缓存
        List<String> deleteKeys = Arrays.stream(PreferenceTypeEnum.values())
                .map(s -> PreferenceCacheKeyDefine.PREFERENCE.format(userId, s))
                .collect(Collectors.toList());
        RedisStrings.delete(deleteKeys);
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
        Map<String, Object> config = RedisStrings.getJson(key);
        boolean setCache = Maps.isEmpty(config);
        // 查询数据库
        if (Maps.isEmpty(config)) {
            config = preferenceDAO.of()
                    .wrapper(this.buildQueryWrapper(userId, typeValue))
                    .optionalOne()
                    .map(PreferenceDO::getConfig)
                    .map(JSON::parseObject)
                    .orElse(null);
        }
        // 初始化
        if (Maps.isEmpty(config)) {
            config = type.getStrategy()
                    .getDefault()
                    .toMap();
            // 插入
            PreferenceDO entity = new PreferenceDO();
            entity.setUserId(userId);
            entity.setType(typeValue);
            entity.setConfig(JSON.toJSONString(config));
            preferenceDAO.insert(entity);
        }
        // 设置缓存
        if (setCache) {
            RedisStrings.setJson(key, PreferenceCacheKeyDefine.PREFERENCE, config);
        }
        return config;
    }

    /**
     * 构建查询 wrapper
     *
     * @param userId userId
     * @param type   type
     * @return wrapper
     */
    private LambdaQueryWrapper<PreferenceDO> buildQueryWrapper(Long userId, String type) {
        return preferenceDAO.wrapper()
                .eq(PreferenceDO::getUserId, userId)
                .eq(PreferenceDO::getType, type);
    }

}
