/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.dao.PreferenceDAO;
import org.dromara.visor.module.infra.define.cache.PreferenceCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.PreferenceDO;
import org.dromara.visor.module.infra.entity.request.preference.PreferenceUpdateBatchRequest;
import org.dromara.visor.module.infra.entity.request.preference.PreferenceUpdateRequest;
import org.dromara.visor.module.infra.enums.PreferenceTypeEnum;
import org.dromara.visor.module.infra.service.PreferenceService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    public void updatePreference(PreferenceUpdateRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        String type = request.getType();
        String item = request.getItem();
        Assert.valid(PreferenceTypeEnum::of, type);
        // 查询
        PreferenceDO preference = preferenceDAO.of()
                .createWrapper()
                .eq(PreferenceDO::getUserId, userId)
                .eq(PreferenceDO::getType, type)
                .eq(PreferenceDO::getItem, item)
                .then()
                .getOne();
        if (preference == null) {
            // 插入
            PreferenceDO insertRecord = new PreferenceDO();
            insertRecord.setUserId(userId);
            insertRecord.setType(type);
            insertRecord.setItem(item);
            insertRecord.setValue(request.getValue());
            preferenceDAO.insert(insertRecord);
        } else {
            // 更新
            PreferenceDO updateRecord = new PreferenceDO();
            updateRecord.setId(preference.getId());
            updateRecord.setValue(request.getValue());
            preferenceDAO.updateById(updateRecord);
        }
        // 删除缓存
        RedisStrings.delete(PreferenceCacheKeyDefine.PREFERENCE.format(userId, type));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreferenceBatch(PreferenceUpdateBatchRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        String type = request.getType();
        Map<String, String> config = request.getConfig();
        Assert.valid(PreferenceTypeEnum::of, type);
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
                    update.setValue(config.get(s));
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
                    insert.setValue(config.get(s));
                    return insert;
                }).collect(Collectors.toList());
        preferenceDAO.insertBatch(insertRecords);
        // 删除缓存
        RedisStrings.delete(PreferenceCacheKeyDefine.PREFERENCE.format(userId, type));
    }

    @Override
    public <T> T getPreferenceByType(String type, List<String> items) {
        Long userId = SecurityUtils.getLoginUserId();
        PreferenceTypeEnum typeEnum = Assert.valid(PreferenceTypeEnum::of, type);
        // 查询缓存
        return this.getPreferenceByCache(userId, typeEnum, items);
    }

    @Override
    public <T> T getDefaultPreferenceByType(String type, List<String> items) {
        PreferenceTypeEnum preferenceType = Assert.valid(PreferenceTypeEnum::of, type);
        // 获取默认值
        JSONObject config = JSONObject.parseObject(preferenceType.getDefault().serial());
        // 解析
        return this.parsePreference(preferenceType, config, items);
    }

    @Override
    @Async("asyncExecutor")
    public <T> Future<T> getPreferenceAsync(Long userId, PreferenceTypeEnum type) {
        T config = this.getPreferenceByCache(userId, type, null);
        return CompletableFuture.completedFuture(config);
    }

    @Override
    public void deletePreferenceByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        // 删除
        this.deletePreferenceByUserIdList(Lists.singleton(userId));
    }

    @Override
    public void deletePreferenceByUserIdList(List<Long> userIdList) {
        if (Lists.isEmpty(userIdList)) {
            return;
        }
        // 删除
        int effect = preferenceDAO.deleteByUserIdList(userIdList);
        log.info("PreferenceService-deletePreferenceByUserIdList userIdList: {}, effect: {}", userIdList, effect);
        // 删除缓存 让他自动过期
        // List<String> deleteKeys = new ArrayList<>();
        // for (Long userId : userIdList) {
        //     Arrays.stream(PreferenceTypeEnum.values())
        //             .map(s -> PreferenceCacheKeyDefine.PREFERENCE.format(userId, s))
        //             .forEach(deleteKeys::add);
        // }
        // RedisStrings.delete(deleteKeys);
    }

    /**
     * 通过缓存获取偏好
     *
     * @param userId userId
     * @param type   type
     * @param items  items
     * @return config
     */
    private <T> T getPreferenceByCache(Long userId, PreferenceTypeEnum type, List<String> items) {
        String typeValue = type.getType();
        // 查询缓存
        String key = PreferenceCacheKeyDefine.PREFERENCE.format(userId, typeValue);
        JSONObject config = RedisStrings.getJson(key);
        boolean setCache = Maps.isEmpty(config);
        // 查询数据库
        if (config == null || Maps.isEmpty(config)) {
            config = preferenceDAO.of()
                    .createWrapper()
                    .eq(PreferenceDO::getUserId, userId)
                    .eq(PreferenceDO::getType, typeValue)
                    .then()
                    .stream()
                    .collect(Collectors.toMap(
                            PreferenceDO::getItem,
                            PreferenceDO::getValue,
                            Functions.right(),
                            JSONObject::new));
        }
        // 初始化
        if (Maps.isEmpty(config)) {
            // 获取默认值
            config = JSONObject.parseObject(type.getDefault().serial());
            // 插入默认值
            List<PreferenceDO> entities = config
                    .entrySet()
                    .stream()
                    .map(s -> {
                        PreferenceDO entity = new PreferenceDO();
                        entity.setUserId(userId);
                        entity.setType(typeValue);
                        entity.setItem(s.getKey());
                        entity.setValue(Objects1.toString(s.getValue()));
                        return entity;
                    }).collect(Collectors.toList());
            preferenceDAO.insertBatch(entities);
        }
        // 设置缓存
        if (setCache) {
            RedisStrings.setJson(key, PreferenceCacheKeyDefine.PREFERENCE, config);
        }
        // 获取偏好
        return this.parsePreference(type, config, items);
    }

    /**
     * 解析偏好
     *
     * @param type   type
     * @param config config
     * @param items  items
     * @param <T>    T
     * @return prefer
     */
    @SuppressWarnings("unchecked")
    private <T> T parsePreference(PreferenceTypeEnum type, JSONObject config, List<String> items) {
        // 设置返回的键
        if (!Lists.isEmpty(items)) {
            JSONObject configItems = new JSONObject();
            for (String item : items) {
                configItems.put(item, config.get(item));
            }
            config = configItems;
        }
        // 解析
        return (T) config.toJavaObject(type.getModelClass());
    }

}
