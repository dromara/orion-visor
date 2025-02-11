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

import cn.orionsec.kit.lang.define.wrapper.Pair;
import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.crypto.Keys;
import cn.orionsec.kit.lang.utils.crypto.RSA;
import cn.orionsec.kit.spring.SpringHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.common.constant.AppConst;
import org.dromara.visor.common.constant.ConfigKeys;
import org.dromara.visor.framework.config.core.event.ConfigUpdateEvent;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
import org.dromara.visor.module.infra.dao.SystemSettingDAO;
import org.dromara.visor.module.infra.define.cache.SystemSettingKeyDefine;
import org.dromara.visor.module.infra.entity.domain.SystemSettingDO;
import org.dromara.visor.module.infra.entity.request.system.SystemSettingUpdateBatchRequest;
import org.dromara.visor.module.infra.entity.request.system.SystemSettingUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.AppInfoVO;
import org.dromara.visor.module.infra.entity.vo.RsaKeyPairVO;
import org.dromara.visor.module.infra.service.SystemSettingService;
import org.dromara.visor.module.infra.utils.SystemUuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统服务 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/17 18:10
 */
@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    @Resource
    private SystemSettingDAO systemSettingDAO;

    @Override
    public AppInfoVO getAppInfo() {
        return AppInfoVO.builder()
                .version(AppConst.VERSION)
                .uuid(SystemUuidUtils.getSystemUuid())
                .build();
    }

    @Override
    public Map<String, String> getSystemAggregateSetting() {
        // 查询缓存
        Map<String, String> cache = RedisMaps.entities(SystemSettingKeyDefine.SETTING);
        if (Maps.isEmpty(cache)) {
            // 查询数据库
            cache = systemSettingDAO.of()
                    .createWrapper()
                    .select(SystemSettingDO::getConfigKey,
                            SystemSettingDO::getValue)
                    .in(SystemSettingDO::getConfigKey,
                            ConfigKeys.ENCRYPT_PUBLIC_KEY,
                            ConfigKeys.LOG_WEB_SCROLL_LINES)
                    .then()
                    .stream()
                    .collect(Collectors.toMap(SystemSettingDO::getConfigKey,
                            SystemSettingDO::getValue,
                            Functions.right()));
            // 设置缓存
            RedisMaps.putAll(SystemSettingKeyDefine.SETTING, cache);
        }
        return cache;
    }

    @Override
    public RsaKeyPairVO generatorKeypair() {
        // 生成密钥对
        Pair<RSAPublicKey, RSAPrivateKey> pair = RSA.generatorKeys();
        return RsaKeyPairVO.builder()
                .publicKey(Keys.getPublicKey(pair.getKey()))
                .privateKey(Keys.getPrivateKey(pair.getValue()))
                .build();
    }

    @Override
    public Map<String, String> getSystemSettingByType(String type) {
        return systemSettingDAO.of()
                .createWrapper()
                .eq(SystemSettingDO::getType, type)
                .then()
                .stream()
                .collect(Collectors.toMap(
                        SystemSettingDO::getConfigKey,
                        SystemSettingDO::getValue,
                        Functions.right()));
    }

    @Override
    public void updateSystemSetting(SystemSettingUpdateRequest request) {
        String type = request.getType();
        String configKey = request.getConfigKey();
        String value = request.getValue();
        // 删除
        systemSettingDAO.delete(Conditions.eq(SystemSettingDO::getConfigKey, configKey));
        // 插入
        SystemSettingDO insert = SystemSettingDO.builder()
                .type(type)
                .configKey(configKey)
                .value(Strings.def(value))
                .build();
        systemSettingDAO.insert(insert);
        // 更新
        SystemSettingDO update = new SystemSettingDO();
        update.setValue(value);
        LambdaQueryWrapper<SystemSettingDO> wrapper = systemSettingDAO.lambda()
                .eq(SystemSettingDO::getConfigKey, configKey);
        systemSettingDAO.update(update, wrapper);
        // 删除缓存
        RedisUtils.delete(SystemSettingKeyDefine.SETTING);
        // 触发修改事件
        SpringHolder.publishEvent(ConfigUpdateEvent.of(configKey, value));
    }

    @Override
    public void updateSystemSettingBatch(SystemSettingUpdateBatchRequest request) {
        String type = request.getType();
        Map<String, String> settings = request.getSettings();
        // 删除
        systemSettingDAO.delete(Conditions.in(SystemSettingDO::getConfigKey, settings.keySet()));
        // 插入
        List<SystemSettingDO> rows = settings.entrySet()
                .stream()
                .map(s -> SystemSettingDO.builder()
                        .type(type)
                        .configKey(s.getKey())
                        .value(Strings.def(s.getValue()))
                        .build())
                .collect(Collectors.toList());
        // 插入
        systemSettingDAO.insertBatch(rows);
        // 删除缓存
        RedisUtils.delete(SystemSettingKeyDefine.SETTING);
        // 触发修改事件
        Map<String, String> eventConfig = rows.stream()
                .collect(Collectors.toMap(
                        SystemSettingDO::getConfigKey,
                        SystemSettingDO::getValue,
                        Functions.right()));
        SpringHolder.publishEvent(ConfigUpdateEvent.of(eventConfig));
    }

}
