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

import cn.orionsec.kit.ext.process.ProcessAwaitExecutor;
import cn.orionsec.kit.lang.define.wrapper.Pair;
import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.support.Attempt;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.crypto.Keys;
import cn.orionsec.kit.lang.utils.crypto.RSA;
import cn.orionsec.kit.lang.utils.crypto.Signatures;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.spring.SpringHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.common.constant.AppConst;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.config.core.event.ConfigUpdateEvent;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
import org.dromara.visor.module.infra.dao.SystemSettingDAO;
import org.dromara.visor.module.infra.define.cache.SystemSettingKeyDefine;
import org.dromara.visor.module.infra.define.operator.SystemSettingOperatorType;
import org.dromara.visor.module.infra.entity.domain.SystemSettingDO;
import org.dromara.visor.module.infra.entity.request.system.SystemSettingUpdateBatchRequest;
import org.dromara.visor.module.infra.entity.request.system.SystemSettingUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.AppInfoVO;
import org.dromara.visor.module.infra.entity.vo.SystemSettingAggregateVO;
import org.dromara.visor.module.infra.enums.SystemSettingTypeEnum;
import org.dromara.visor.module.infra.handler.setting.model.EncryptSystemSettingModel;
import org.dromara.visor.module.infra.handler.setting.model.SftpSystemSettingModel;
import org.dromara.visor.module.infra.service.SystemSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
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
    public SystemSettingAggregateVO getSystemAggregateSetting() {
        // 查询缓存
        SystemSettingAggregateVO cache = RedisStrings.getJson(SystemSettingKeyDefine.SETTING);
        SystemSettingAggregateVO result = Objects1.def(cache, SystemSettingAggregateVO::new);
        if (cache == null) {
            // 查询数据库
            Map<String, List<SystemSettingDO>> typeGroup = systemSettingDAO.of()
                    .createWrapper()
                    .select(SystemSettingDO::getType,
                            SystemSettingDO::getItem,
                            SystemSettingDO::getValue)
                    .in(SystemSettingDO::getType,
                            SystemSettingTypeEnum.SFTP.name(),
                            SystemSettingTypeEnum.ENCRYPT.name())
                    .then()
                    .stream()
                    .collect(Collectors.groupingBy(SystemSettingDO::getType));
            // 数据组合
            typeGroup.forEach((k, v) -> {
                // 类型数据
                SystemSettingTypeEnum settingType = SystemSettingTypeEnum.of(k);
                Map<String, String> typeSettings = v.stream()
                        .collect(Collectors.toMap(
                                SystemSettingDO::getItem,
                                SystemSettingDO::getValue,
                                Functions.right()));
                Object setting = settingType.parseModel(typeSettings);
                if (SystemSettingTypeEnum.SFTP.equals(settingType)) {
                    // SFTP 设置
                    result.setSftp((SftpSystemSettingModel) setting);
                } else if (SystemSettingTypeEnum.ENCRYPT.equals(settingType)) {
                    // 加密设置
                    EncryptSystemSettingModel encryptSetting = (EncryptSystemSettingModel) setting;
                    encryptSetting.setPrivateKey(null);
                    result.setEncrypt(encryptSetting);
                }
            });
            // 设置缓存
            RedisStrings.setJson(SystemSettingKeyDefine.SETTING, result);
        }
        return result;
    }

    @Override
    public EncryptSystemSettingModel generatorKeypair() {
        // 生成密钥对
        Pair<RSAPublicKey, RSAPrivateKey> pair = RSA.generatorKeys();
        return EncryptSystemSettingModel.builder()
                .publicKey(Keys.getPublicKey(pair.getKey()))
                .privateKey(Keys.getPrivateKey(pair.getValue()))
                .build();
    }

    @Override
    public <T> T getSystemSettingByType(String type) {
        SystemSettingTypeEnum settingType = SystemSettingTypeEnum.of(type);
        Valid.notNull(settingType, ErrorMessage.ERROR_TYPE);
        // 查询数据库
        Map<String, String> settings = systemSettingDAO.of()
                .createWrapper()
                .eq(SystemSettingDO::getType, type)
                .then()
                .stream()
                .collect(Collectors.toMap(
                        SystemSettingDO::getItem,
                        SystemSettingDO::getValue,
                        Functions.right()));
        // 解析
        return settingType.parseModel(settings);
    }

    @Override
    public void updateSystemSetting(SystemSettingUpdateRequest request) {
        String type = request.getType();
        SystemSettingTypeEnum settingType = Valid.valid(SystemSettingTypeEnum::of, type);
        String item = request.getItem();
        String value = request.getValue();
        // 更新
        SystemSettingDO update = new SystemSettingDO();
        update.setValue(value);
        LambdaQueryWrapper<SystemSettingDO> wrapper = systemSettingDAO.lambda()
                .eq(SystemSettingDO::getType, type)
                .eq(SystemSettingDO::getItem, item);
        systemSettingDAO.update(update, wrapper);
        // 删除缓存
        RedisUtils.delete(SystemSettingKeyDefine.SETTING);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.TEXT, Strings.format(SystemSettingOperatorType.UPDATE_TEXT, type, item, value));
        // 触发修改事件
        SpringHolder.publishEvent(ConfigUpdateEvent.of(settingType.getConfigKey(item), value));
    }

    @Override
    public void updateSystemSettingBatch(SystemSettingUpdateBatchRequest request) {
        String type = request.getType();
        SystemSettingTypeEnum settingType = Valid.valid(SystemSettingTypeEnum::of, type);
        Map<String, String> settings = request.getSettings();
        // 删除
        LambdaQueryWrapper<SystemSettingDO> deleteWrapper = systemSettingDAO.lambda()
                .eq(SystemSettingDO::getType, type)
                .in(SystemSettingDO::getItem, settings.keySet());
        systemSettingDAO.delete(deleteWrapper);
        // 插入
        List<SystemSettingDO> rows = settings.entrySet()
                .stream()
                .map(s -> SystemSettingDO.builder()
                        .configKey(settingType.getConfigKey(s.getKey()))
                        .type(type)
                        .item(s.getKey())
                        .value(s.getValue())
                        .build())
                .collect(Collectors.toList());
        // 插入
        systemSettingDAO.insertBatch(rows);
        // 删除缓存
        RedisUtils.delete(SystemSettingKeyDefine.SETTING);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.TEXT, Strings.format(SystemSettingOperatorType.UPDATE_BATCH_TEXT, type));
        // 触发修改事件
        Map<String, String> eventConfig = rows.stream()
                .collect(Collectors.toMap(
                        SystemSettingDO::getConfigKey,
                        SystemSettingDO::getValue,
                        Functions.right()));
        SpringHolder.publishEvent(ConfigUpdateEvent.of(eventConfig));
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
