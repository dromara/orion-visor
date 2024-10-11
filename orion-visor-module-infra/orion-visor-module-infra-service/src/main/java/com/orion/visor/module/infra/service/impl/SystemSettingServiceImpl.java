/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ext.process.ProcessAwaitExecutor;
import com.orion.lang.function.Functions;
import com.orion.lang.support.Attempt;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Refs;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.lang.utils.io.Streams;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.AppConst;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.redis.core.utils.RedisMaps;
import com.orion.visor.framework.redis.core.utils.RedisUtils;
import com.orion.visor.module.infra.dao.SystemSettingDAO;
import com.orion.visor.module.infra.define.cache.SystemSettingKeyDefine;
import com.orion.visor.module.infra.define.operator.SystemSettingOperatorType;
import com.orion.visor.module.infra.entity.domain.SystemSettingDO;
import com.orion.visor.module.infra.entity.request.system.SystemSettingUpdatePartialRequest;
import com.orion.visor.module.infra.entity.request.system.SystemSettingUpdateRequest;
import com.orion.visor.module.infra.entity.vo.AppInfoVO;
import com.orion.visor.module.infra.enums.SystemSettingTypeEnum;
import com.orion.visor.module.infra.service.SystemSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    private String uuid;

    @Resource
    private SystemSettingDAO systemSettingDAO;

    @Override
    public AppInfoVO getAppInfo() {
        return AppInfoVO.builder()
                .version(AppConst.VERSION)
                .uuid(this.getSystemUuid())
                .build();
    }

    @Override
    public Integer updateSystemSetting(SystemSettingUpdateRequest request) {
        String type = request.getType();
        String item = request.getItem();
        Object value = request.getValue();
        // 更新
        SystemSettingDO update = new SystemSettingDO();
        update.setValue(Refs.json(value));
        LambdaQueryWrapper<SystemSettingDO> wrapper = systemSettingDAO.lambda()
                .eq(SystemSettingDO::getType, type)
                .eq(SystemSettingDO::getItem, item);
        int effect = systemSettingDAO.update(update, wrapper);
        // 删除缓存
        RedisUtils.delete(SystemSettingKeyDefine.SETTING.format(type));
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.TEXT, Strings.format(SystemSettingOperatorType.UPDATE_TEXT, type, item, value));
        return effect;
    }

    @Override
    public void updatePartialSystemSetting(SystemSettingUpdatePartialRequest request) {
        String type = request.getType();
        Map<String, Object> settings = request.getSettings();
        // 删除
        LambdaQueryWrapper<SystemSettingDO> deleteWrapper = systemSettingDAO.lambda()
                .eq(SystemSettingDO::getType, type)
                .in(SystemSettingDO::getItem, settings.keySet());
        systemSettingDAO.delete(deleteWrapper);
        // 插入
        List<SystemSettingDO> rows = settings.entrySet()
                .stream()
                .map(s -> SystemSettingDO.builder()
                        .type(type)
                        .item(s.getKey())
                        .value(Refs.json(s.getValue()))
                        .build())
                .collect(Collectors.toList());
        // 插入
        systemSettingDAO.insertBatch(rows);
        // 删除缓存
        RedisUtils.delete(SystemSettingKeyDefine.SETTING.format(type));
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.TEXT, Strings.format(SystemSettingOperatorType.UPDATE_PARTIAL_TEXT, type));
    }

    @Override
    public Map<String, Object> getSystemSettingByType(String type) {
        SystemSettingTypeEnum settingType = SystemSettingTypeEnum.of(type);
        Valid.notNull(settingType, ErrorMessage.ERROR_TYPE);
        // 查询缓存
        String key = SystemSettingKeyDefine.SETTING.format(type);
        Map<String, String> settings = RedisMaps.entities(key);
        boolean setCache = Maps.isEmpty(settings);
        // 查询数据库
        if (Maps.isEmpty(settings)) {
            settings = systemSettingDAO.of()
                    .createWrapper()
                    .eq(SystemSettingDO::getType, type)
                    .then()
                    .stream()
                    .collect(Collectors.toMap(
                            SystemSettingDO::getItem,
                            SystemSettingDO::getValue,
                            Functions.right()));
        }
        // 初始化
        if (Maps.isEmpty(settings)) {
            // 获取默认值
            Map<String, Object> defaultConfig = settingType.getStrategy()
                    .getDefault()
                    .toMap();
            settings = Maps.map(defaultConfig, Function.identity(), Refs::json);
            // 插入默认值
            List<SystemSettingDO> entities = settings
                    .entrySet()
                    .stream()
                    .map(s -> {
                        SystemSettingDO entity = new SystemSettingDO();
                        entity.setType(type);
                        entity.setItem(s.getKey());
                        entity.setValue(s.getValue());
                        return entity;
                    }).collect(Collectors.toList());
            systemSettingDAO.insertBatch(entities);
        }
        // 设置缓存
        if (setCache) {
            RedisMaps.putAll(key, SystemSettingKeyDefine.SETTING, settings);
        }
        // unRef
        return Maps.map(settings, Function.identity(), Refs::unref);
    }

    /**
     * 获取系统 uuid
     *
     * @return uuid
     */
    private String getSystemUuid() {
        if (this.uuid != null) {
            return this.uuid;
        }
        String[][] cmd = new String[][]{
                new String[]{"/bin/sh", "-c", "cat /sys/class/dmi/id/product_serial"},
                new String[]{"/bin/bash", "-c", "cat /sys/class/dmi/id/product_serial"},
                new String[]{"/bin/sh", "-c", "dmidecode -s system-uuid"},
                new String[]{"/bin/bash", "-c", "dmidecode -s system-uuid"},
                new String[]{"cmd", "/c", "wmic csproduct get uuid"}
        };
        for (String[] s : cmd) {
            try {
                String uuid = this.getCommandOutput(s);
                if (Strings.isBlank(uuid)) {
                    continue;
                }
                // 去除符号并且转为大写
                uuid = uuid.replaceAll(Const.DASHED, Const.EMPTY)
                        .toUpperCase()
                        .trim();
                // 去除 \n
                String extraUuid = Arrays1.last(uuid.trim().split(Const.LF));
                if (!Strings.isBlank(extraUuid)) {
                    uuid = extraUuid.trim();
                }
                // 去除 :
                extraUuid = Arrays1.last(uuid.trim().split(Const.COLON));
                if (!Strings.isBlank(extraUuid)) {
                    uuid = extraUuid.trim();
                }
                return this.uuid = Signatures.md5(uuid);
            } catch (Exception e) {
                // IGNORED
            }
        }
        return this.uuid = Const.UNKNOWN;
    }

    /**
     * 获取输出结果
     *
     * @param command command
     * @return result
     */
    private String getCommandOutput(String[] command) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(command);
        try {
            executor.streamHandler(i -> Attempt.uncheck(Streams::transfer, i, out))
                    .waitFor()
                    .sync()
                    .exec();
            return out.toString();
        } finally {
            Streams.close(out);
            Streams.close(executor);
        }
    }

}
